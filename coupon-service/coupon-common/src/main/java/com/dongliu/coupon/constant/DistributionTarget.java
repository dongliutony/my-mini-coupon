package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DistributionTarget {

    SINGLE("For Single User", 1),
    GROUP("For Group User", 2);

    private final String description;
    private final Integer code;

    public static DistributionTarget of(Integer code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("DistributionTarget " + code + " not exists."));
    }
}
