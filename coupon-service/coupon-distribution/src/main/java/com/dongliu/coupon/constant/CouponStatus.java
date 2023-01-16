package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponStatus {
    USABLE("Coupon is usable", 1),
    USED("Coupon has been used", 2),
    EXPIRED("Coupon is expired without using", 3);

    private String description;
    private Integer code;

    public static CouponStatus of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("CouponStatus " + code + " not exist"));
    }
}
