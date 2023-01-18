package com.dongliu.coupon.executor.impl;

import com.dongliu.coupon.constant.RuleFlag;
import com.dongliu.coupon.executor.AbstractExecutor;
import com.dongliu.coupon.executor.RuleExecutor;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import com.dongliu.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class ThresholdExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.THRESHOLD;
    }

    @Override
    public SettlementInfo computeWithRules(SettlementInfo settlementInfo) {

        double totalPrice = keep2Decimals(getTotalPrice(settlementInfo.getGoodsInfoList()));

        SettlementInfo maybeNotQualify = processGoodsTypeNotSatisfy(settlementInfo, totalPrice);
        if(null != maybeNotQualify) {
            log.info("Purchased goods type not match THRESHOLD coupon applicable types");
            return maybeNotQualify;
        }

        // check if the threshold coupon usage condition is met
        CouponTemplateSDK templateSDK = settlementInfo.getCouponAndTemplateInfoList().get(0).getTemplateSDK();
        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double credit = (double) templateSDK.getRule().getDiscount().getCredit();

        // if baseline price not met, return original total price, and clear coupons in the settlement info
        if (totalPrice < base) {
            log.debug("Current total original price is less than required base price to use the coupon.");
            settlementInfo.setTotalPrice(totalPrice);
            settlementInfo.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlementInfo;
        }

        // evaluate the final total price after using coupons
        settlementInfo.setTotalPrice(
                keep2Decimals(Math.max((totalPrice - credit), minPrice())));
        log.debug("Use THRESHOLD coupon to reduce total price from {} to {}", totalPrice, settlementInfo.getTotalPrice());

        return settlementInfo;
    }
}
