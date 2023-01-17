package com.dongliu.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.Constant;
import com.dongliu.coupon.constant.CouponStatus;
import com.dongliu.coupon.entity.Coupon;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.feign.ISettlementClient;
import com.dongliu.coupon.feign.ITemplateClient;
import com.dongliu.coupon.repository.CouponRepository;
import com.dongliu.coupon.service.IRedisService;
import com.dongliu.coupon.service.IUserService;
import com.dongliu.coupon.vo.*;
import com.google.common.base.Functions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <h1>UserService</h1>
 * Save all operation status into Redis, then send to MySQL through Kafka.
 * 1. need to communicate with CouponTemplate and CouponSettlement services through Hystrix/Feign
 * 2. need to cache
 * 3. need to use Kafka to change time-costly sync requests into async requests, such as expired coupon
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final CouponRepository couponRepository;
    private final IRedisService redisService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ITemplateClient templateClient;
    private final ISettlementClient settlementClient;

    @Autowired
    public UserServiceImpl(CouponRepository couponRepository,
                           IRedisService redisService,
                           KafkaTemplate<String, String> kafkaTemplate,
                           ITemplateClient templateClient,
                           ISettlementClient settlementClient) {
        this.couponRepository = couponRepository;
        this.redisService = redisService;
        this.kafkaTemplate = kafkaTemplate;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
    }


    /**
     * <h2>Find Coupon by UserId and Status</h2>
     * @param userId
     * @param status
     * @return
     * @throws CouponException
     */
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {

        // existing coupons in Redis
        List<Coupon> preCached = redisService.getCachedCoupons(userId, status);
        // target coupons before modification. Will check expiration and usage, and modify status.
        List<Coupon> preTarget;

        if (CollectionUtils.isNotEmpty(preCached)) {
            log.debug("Loaded coupons from cache: {}, {}", userId, status);
            preTarget = preCached;
        } else {
            log.debug("coupon cache is empty, fetching coupon from db: {}, {} ...", userId, status);
            List<Coupon> dbCoupons = couponRepository.findAllByUserIdAndStatus(userId, CouponStatus.of(status));

            // if no records in db, return null. Service will insert into Redis a placeholder coupon record.
            if(CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupons;
            }

            // fill in dbCoupon's CouponTemplateSDK filed.
            Map<Integer, CouponTemplateSDK> id2TemplateSDK = templateClient.findIds2TemplateSDK(
                    dbCoupons.stream()
                            .map(Coupon::getTemplateId)
                            .collect(Collectors.toList())
                    ).getData();
            dbCoupons.forEach(dbCoupon -> dbCoupon.setTemplateSDK(id2TemplateSDK.get(dbCoupon.getId())));

            // write coupons into Redis
            preTarget = dbCoupons;
            redisService.addCouponToCache(userId, preTarget, status);
        }

        // remove placeholder coupon if it exists.
        preTarget = preTarget.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());
        // if the coupon status is "usable", we need to check if one is expired, and update MySQL asyncly through
        // Kafka message if it expires.
        if (CouponStatus.of(status) == CouponStatus.USABLE) {
            CouponStatusGroups statusGroups = CouponStatusGroups.classify(preTarget);
            // if there are expired coupons, async handle them
            if(CollectionUtils.isNotEmpty(statusGroups.getExpired())) {
                log.info("add expired coupons to Cache from FindCouponsByStatus: {}, {}", userId, status);
                redisService.addCouponToCache(userId, statusGroups.getExpired(), CouponStatus.EXPIRED.getCode());
                // send to Kafka message
                kafkaTemplate.send(Constant.COUPON_OP_TOPIC,
                        JSON.toJSONString(new CouponKafkaMessage(
                                CouponStatus.EXPIRED.getCode(),
                                statusGroups.getExpired().stream()
                                        .map(Coupon::getId)
                                        .collect(Collectors.toList())
                        ))
                );
            }
            return statusGroups.getUsable();

        }

        // if status != USABLE, we just return what we got from db/redis
        return preTarget;
    }

    @Override
    public List<CouponTemplateSDK> findAvailableTemplates(Long userId) throws CouponException {
        Long curTime = new Date().getTime();
        List<CouponTemplateSDK> templateSDKs = templateClient.findAllUsableTemplates().getData();

        log.debug("Found all Template(from remote TemplateClient) count: {}", templateSDKs.size());

        // filter out expired template
        templateSDKs = templateSDKs.stream()
                .filter(sdk -> sdk.getRule().getExpiration().getDeadline() > curTime)
                .collect(Collectors.toList());
        log.info("Find Usable Template count: {}", templateSDKs.size());

        // key: templateId, value: left: TemplateLimit, right: CouponTemplate
        Map<Integer, Pair<Integer, CouponTemplateSDK>> limit2Template = new HashMap<>(templateSDKs.size());
        templateSDKs.forEach(
                t -> limit2Template.put(t.getId(), Pair.of(t.getRule().getLimit(), t))
        );

        List<CouponTemplateSDK> result = new ArrayList<>(limit2Template.size());
        List<Coupon> userUsableCoupons = findCouponsByStatus(userId, CouponStatus.USABLE.getCode());

        log.debug("User already has Usable Coupons: {}, {}", userId, userUsableCoupons.size());

        // calculate coupons for each templateId from the userUsableCoupons
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        // Check if can apply coupons from a template based on rule() and existed coupons.
        limit2Template.forEach((k, v) -> {
            int limitCount = v.getLeft();
            CouponTemplateSDK templateSDK = v.getRight();

            // what a user has already exceeds limit, cannot apply more.
            if(templateId2Coupons.containsKey(k) && templateId2Coupons.get(k).size() >= limitCount) {
                return;
            }
            result.add(templateSDK);
        });

        return result;
    }

    /**
     * <h2>User apply coupons using acquire request</h2>
     * 1. Get coupon from TemplateClient, check if expired
     * 2. Check if user has quota by limit
     * 3. save to db
     * 4. fill in CouponTemplateSDK
     * 5. save to cache
     */
    @Override
    public Coupon applyByTemplate(AcquireTemplateRequest request) throws CouponException {
        Map<Integer, CouponTemplateSDK> id2Template = templateClient.findIds2TemplateSDK(
                Collections.singletonList(request.getTemplateSDK().getId())
        ).getData();
        if(id2Template.size() == 0) {
            log.error("Cannot acquire Template from TemplateClient: {}", request.getTemplateSDK().getId());
            throw new CouponException("Cannot acquire Template from TemplateClient");
        }

        // check if user can acquire coupons from template
        List<Coupon> userUsableCoupons = findCouponsByStatus(request.getUserId(), CouponStatus.USABLE.getCode());
        // already issued coupons for each templateId
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));
        // already issued coupons exceed template quota of coupons, cannot issue more
        if(templateId2Coupons.containsKey(request.getTemplateSDK().getId())
                && templateId2Coupons.get(request.getTemplateSDK().getId()).size() >=
                    request.getTemplateSDK().getRule().getLimit()) {
            log.error("Exceed template's quota of issue coupons: {}", request.getTemplateSDK().getId());
            throw new CouponException("Exceed Template issue limit");
        }

        // try to acquire coupons from Redis
        String couponCode = redisService.applyCouponCodeFromCache(request.getTemplateSDK().getId());
        if(StringUtils.isEmpty(couponCode)) {
            log.error("Cannot acquire Coupon Code: {}", request.getTemplateSDK().getId());
            throw new CouponException("Cannot acquire Coupon Code");
        }
        Coupon newCoupon = new Coupon(request.getTemplateSDK().getId(), request.getUserId(),
                couponCode, CouponStatus.USABLE);
        newCoupon = couponRepository.save(newCoupon);

        // fill in CouponTemplateSDK for newCoupon before save it into Redis
        newCoupon.setTemplateSDK(newCoupon.getTemplateSDK());

        // save into Redis
        redisService.addCouponToCache(request.getUserId(),
                Collections.singletonList(newCoupon), CouponStatus.USABLE.getCode());

        return newCoupon;
    }

    /**
     * <h2>Evaluate Final Price with coupons</h2>
     * NOTE: settlement process the calculation. Current method only validate the request for settlement.
     */
    @Override
    public SettlementInfo evaluateSettlement(SettlementInfo info) throws CouponException {
        // if coupons absent, return original total price.
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = info.getCouponAndTemplateInfoList();
        if(CollectionUtils.isEmpty(ctInfos)) {
            log.info("No Coupon for settlement.");
            double goodsSum = 0.0;
            for (GoodsInfo gi : info.getGoodsInfoList()) {
                goodsSum += gi.getPrice() * gi.getCount();
            }

            // return original total price
            info.setTotalPrice(retain2Decimals(goodsSum));
        }

        // validate: the coupon belongs to the customer
        List<Coupon> coupons = findCouponsByStatus(info.getUserId(), CouponStatus.USABLE.getCode());
        Map<Integer, Coupon> id2Coupon = coupons.stream()
                .collect(Collectors.toMap(Coupon::getId, Function.identity()));
        // Usable coupons don't include all coupons passed in from settlement info
        if(MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getCouponId).collect(Collectors.toList()),
                id2Coupon.keySet()
        )) {
            log.info("{}", id2Coupon.keySet());
            log.info("{}", ctInfos.stream()
                    .map(SettlementInfo.CouponAndTemplateInfo::getCouponId).collect(Collectors.toList()));
            log.error("User Coupon has some problems. It is not subCollection of Coupons!");
            throw new CouponException("User Coupon has some problems. It is not subCollection of Coupons!");
        }

        log.debug("Current Settlement coupons belong to user: {} coupons", ctInfos.size());

        List<Coupon> settlementCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settlementCoupons.add(id2Coupon.get(ci.getCouponId())));

        // get evaluated price by calling Settlement service
        SettlementInfo processedInfo = settlementClient.computeRule(info).getData();
        // user pay with coupon
        if(processedInfo.getConsumed() && CollectionUtils.isNotEmpty(processedInfo.getCouponAndTemplateInfoList())) {
            log.info("Settle User Coupon: {}, {}", info.getUserId(), JSON.toJSONString(settlementCoupons));
            // update Redis
            redisService.addCouponToCache(info.getUserId(), settlementCoupons, CouponStatus.USED.getCode());
            // update db through Kafka
            kafkaTemplate.send(
                    Constant.COUPON_OP_TOPIC,
                    JSON.toJSONString(new CouponKafkaMessage(
                            CouponStatus.USED.getCode(),
                            settlementCoupons.stream().map(Coupon::getId).collect(Collectors.toList())
                    ))
            );
        }

        return processedInfo;
    }

    /**
     * <h2>Round Up with two decimals</h2>
     */
    private double retain2Decimals(double value) {
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
