package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponCategory {

    EXCEED_RETURN("Return some money with exceeding fixed price", "001"),
    DISCOUNT("Give discount", "002"),
    INSTANT_RETURN("Unconditional return some money", "003");

    private String description;
    private String code;

    public static CouponCategory of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("CouponCategory " + code + " not exists!"));
    }
}
