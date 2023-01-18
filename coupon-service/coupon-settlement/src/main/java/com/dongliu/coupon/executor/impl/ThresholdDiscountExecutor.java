package com.dongliu.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.CouponCategory;
import com.dongliu.coupon.constant.RuleFlag;
import com.dongliu.coupon.executor.AbstractExecutor;
import com.dongliu.coupon.executor.RuleExecutor;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import com.dongliu.coupon.vo.GoodsInfo;
import com.dongliu.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ThresholdDiscountExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.THRESHOLD_DISCOUNT;
    }

    /**
     * <h2>check good types matches Threshold and Discount coupons using type requirements</h2>
     * NOTE: must override, because the base method is for single type coupon, not for combined coupons
     */
    @Override
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {
        log.debug("Checking purchasing goods types match Threshold and Discount coupons or not.");
        List<Integer> purchasingGoodsType = settlement.getGoodsInfoList()
                .stream()
                .map(GoodsInfo::getType)
                .collect(Collectors.toList());
        List<Integer> templateGoodsType = new ArrayList<>();
        settlement.getCouponAndTemplateInfoList().forEach(ct -> {
            templateGoodsType.addAll(JSON.parseObject(
                    ct.getTemplateSDK().getRule().getUsage().getGoodsType(),
                    List.class
            ));
        });

        // in order to use combined coupons, all purchasing goods types must be included in the templateTypes.
        // that is, the rest of purchasing goods type after removing template types must be empty.
        return CollectionUtils.isEmpty(
                CollectionUtils.subtract(purchasingGoodsType, templateGoodsType)
        );
    }

    @Override
    public SettlementInfo computeWithRules(SettlementInfo settlement) {

        double totalPrice = keep2Decimals(getTotalPrice(settlement.getGoodsInfoList()));

        SettlementInfo maybeNotQualify = processGoodsTypeNotSatisfy(settlement, totalPrice);
        if(null != maybeNotQualify) {
            log.info("Purchasing goods type not match Threshold & Discount coupon applicable types");
            return maybeNotQualify;
        }

        SettlementInfo.CouponAndTemplateInfo thresholdInfo = null;
        SettlementInfo.CouponAndTemplateInfo discountInfo = null;

        for (SettlementInfo.CouponAndTemplateInfo ct : settlement.getCouponAndTemplateInfoList()) {
            if (CouponCategory.of(ct.getTemplateSDK().getCategory()) == CouponCategory.THRESHOLD) {
                thresholdInfo = ct;
            } else if (CouponCategory.of(ct.getTemplateSDK().getCategory()) == CouponCategory.DISCOUNT) {
                discountInfo = ct;
            }
        }

        assert null != thresholdInfo;
        assert null != discountInfo;

        // if the Threshold coupon and Discount coupon cannot be used together, clear coupon and return original price.
        if (!canUseThresholdAndDiscountTogether(thresholdInfo, discountInfo)) {
            log.debug("Cannot use Threshold coupon and Discount coupon together at this time.");
            settlement.setTotalPrice(totalPrice);
            settlement.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlement;
        }

        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double thresholdBase = (double) thresholdInfo.getTemplateSDK().getRule().getDiscount().getBase();
        double thresholdCredit = (double) thresholdInfo.getTemplateSDK().getRule().getDiscount().getCredit();

        // final total price
        double targetTotal = totalPrice;

        // fist, use Threshold coupon
        if(targetTotal > thresholdBase) {
            targetTotal -= thresholdCredit;
            ctInfos.add(thresholdInfo);
        }

        // second, use Discount coupon
        double discountRemainPortion = (double) discountInfo.getTemplateSDK().getRule().getDiscount().getCredit();
        targetTotal *= discountRemainPortion / 100;
        ctInfos.add(discountInfo);

        settlement.setCouponAndTemplateInfoList(ctInfos);
        settlement.setTotalPrice(keep2Decimals(Math.max(targetTotal, minPrice())));

        log.debug("Use Threshold and Discount coupon to reduce total price from {} to {}",
                totalPrice, settlement.getTotalPrice());

        return settlement;
    }

    /**
     * <h2>Check whether Threshold and Discount coupons can be used together</h2>
     * TODO: Generalize the function to check more coupon combinations.
     * @param threshold
     * @param discount
     * @return
     */
    private boolean canUseThresholdAndDiscountTogether(
            SettlementInfo.CouponAndTemplateInfo threshold, SettlementInfo.CouponAndTemplateInfo discount) {
        String thresholdKey = threshold.getTemplateSDK().getKey()
                + String.format("%04d", threshold.getTemplateSDK().getId());
        String discountKey = discount.getTemplateSDK().getKey()
                + String.format("%04d", discount.getTemplateSDK().getId());

        // allSharedKeysForThreshold includes: thresholdKey + its combination keys
        List<String> allSharedKeysForThreshold = new ArrayList<>();
        allSharedKeysForThreshold.add(thresholdKey);
        allSharedKeysForThreshold.addAll(
                JSON.parseObject(threshold.getTemplateSDK().getRule().getCombination(), List.class));

        // allSharedKeysForDiscount includes: discountKey + its combination keys
        List<String> allSharedKeysForDiscount = new ArrayList<>();
        allSharedKeysForDiscount.add(discountKey);
        allSharedKeysForDiscount.addAll(
                JSON.parseObject(discount.getTemplateSDK().getRule().getCombination(), List.class));

        // isSubCollection(sub, super)
        List<String> keys = Arrays.asList(thresholdKey, discountKey);
        return CollectionUtils.isSubCollection(keys, allSharedKeysForThreshold)
                || CollectionUtils.isSubCollection(keys, allSharedKeysForDiscount);
    }
}
