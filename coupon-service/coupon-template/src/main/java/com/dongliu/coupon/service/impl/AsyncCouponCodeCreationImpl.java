package com.dongliu.coupon.service.impl;

import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.repository.CouponTemplateRepository;
import com.dongliu.coupon.service.IAsyncCouponCodeCreation;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.dongliu.coupon.constant.Constant;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsyncCouponCodeCreationImpl implements IAsyncCouponCodeCreation {
    private final CouponTemplateRepository templateRepository;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AsyncCouponCodeCreationImpl(CouponTemplateRepository templateRepository, StringRedisTemplate redisTemplate) {
        this.templateRepository = templateRepository;
        this.redisTemplate = redisTemplate;
    }

    @Async("getAsyncExecutor")
    @Override
    public void asyncCreateCouponByTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> couponCodes = buildCouponCode(template);

        // coupon_template_code_1
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE_CODE_, template.getId().toString());
        log.info("Pushed CouponCode to redis: {}",  redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));

        // update template to "available" because we have created coupons based on it
        template.setAvailable(true);
        templateRepository.save(template);

        watch.stop();
        log.info("Created CouponCode by template cost: {}ms", watch.elapsed(TimeUnit.MILLISECONDS));

        // TODO send email or notification for that the coupons are ready for use
        log.info("CouponTemplate({}) is available for use!", template.getId());
    }

    /**
     * <h2>create Coupon Code</h2>
     * A coupon code belongs to one coupon. it contains 18 digits:
     *  first 4 digits: productLine + type
     *  next 6 digits: shuffle of date (yyMMdd)
     *  last 8 digits: 0 ~ 9 random
     * @param template {@link CouponTemplate} entity
     * @return Set<String> its size is template.count
     * */
    @SuppressWarnings("all")
    private Set<String> buildCouponCode(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> result = new HashSet<>(template.getCount());

        // first 4digits
        String prefix4 = template.getProductLine().getCode().toString() + template.getCategory().getCode();
        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());

        for (int i = 0; i != template.getCount(); ++i) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        while (result.size() < template.getCount()) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        assert result.size() == template.getCount();

        watch.stop();
        log.info("Build Coupon Code Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));

        return result;
    }


    /**
     * <h2>build last 14 digits of coupon code</h2>
     * */
    private String buildCouponCodeSuffix14(String date) {

        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        // middle 6 digits
        List<Character> chars = date.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String mid6 = chars.stream().map(Object::toString).collect(Collectors.joining());

        // last 8 digits
        String suffix8 = RandomStringUtils.random(1, bases) + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;
    }
}
