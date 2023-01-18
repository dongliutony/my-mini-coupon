package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleFlag {
    THRESHOLD("Calculation rules for minimum return coupon"),
    DISCOUNT("Calculation rules for discount coupon"),
    INSTANT("Calculation rules for instant return coupon"),
    THRESHOLD_DISCOUNT("Calculation rules for the combination use of minimum return and discount coupons");

    // TODO: more combinations of coupons

    private String description;

}
