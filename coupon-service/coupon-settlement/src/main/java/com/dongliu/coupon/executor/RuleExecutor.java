package com.dongliu.coupon.executor;

import com.dongliu.coupon.constant.RuleFlag;
import com.dongliu.coupon.vo.SettlementInfo;

/**
 * <h1>CouponTemplate rule executor</h1>
 */
public interface RuleExecutor {

    /**
     * <h2>Tag rule type</h2>
     */
    RuleFlag ruleConfig();

    /**
     * <h2>Calculate final price with coupons</h2>
     * @param info original settlement info with original price
     * @return {@link SettlementInfo} settlement info with final price.
     */
    SettlementInfo computeWithRules(SettlementInfo info);
}
