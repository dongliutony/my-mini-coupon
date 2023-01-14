package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PeriodType {
    REGULAR("Expire at a fixed date", 1),
    SHIFT("An expiration window(from issue date)", 2);

    private final String description;
    private final Integer code;

    public static PeriodType of(Integer code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("PeriodType " + code + " not exits."));
    }
}
