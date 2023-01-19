package com.dongliu.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN("Administrator"),
    SUPER_ADMIN("Super Administrator"),
    CUSTOMER("Customer");

    private final String roleName;
}
