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
public class DiscountExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.DISCOUNT;
    }

    @Override
    public SettlementInfo computeWithRules(SettlementInfo settlementInfo) {

        double totalPrice = keep2Decimals(getTotalPrice(settlementInfo.getGoodsInfoList()));

        SettlementInfo maybeNotQualify = processGoodsTypeNotSatisfy(settlementInfo, totalPrice);
        if(null != maybeNotQualify) {
            log.info("Purchased goods type not match DISCOUNT coupon applicable types");
            return maybeNotQualify;
        }

        // use DISCOUNT coupon without precondition
        CouponTemplateSDK templateSDK = settlementInfo.getCouponAndTemplateInfoList().get(0).getTemplateSDK();
        double credit  = (double) templateSDK.getRule().getDiscount().getCredit();

        // evaluate the final total price after using coupons
        settlementInfo.setTotalPrice(
                keep2Decimals(Math.max((totalPrice * credit / 100), minPrice())));
        log.debug("Use THRESHOLD coupon to reduce total price from {} to {}", totalPrice, settlementInfo.getTotalPrice());

        return settlementInfo;
    }
}
