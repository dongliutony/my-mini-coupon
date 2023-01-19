package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpModeEnum {
    READ("Read Only"),
    WRITE("Read Write");

    private final String mode;
}
