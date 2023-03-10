package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponCategory {

    THRESHOLD("Return some money with exceeding fixed price", "001"),
    DISCOUNT("Give discount", "002"),
    INSTANT("Unconditional return some money", "003");

    private final String description;
    private final String code;

    public static CouponCategory of(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("CouponCategory " + code + " not exists!"));
    }
}
