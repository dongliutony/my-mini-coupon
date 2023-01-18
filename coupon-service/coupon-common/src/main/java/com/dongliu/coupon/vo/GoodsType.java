package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum GoodsType {
    STATIONARY("Stationary", 1),
    DAIRY("Dairy", 2),
    FURNITURE("Furniture", 3),
    OTHERS("Others", 4),
    ALL("All", 5);

    private final String description;
    private final Integer code;

    public static GoodsType of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("GoodsType " + code + " not exists."));
    }

}
