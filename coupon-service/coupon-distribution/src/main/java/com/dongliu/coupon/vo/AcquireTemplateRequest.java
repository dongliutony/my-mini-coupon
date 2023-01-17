package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {
    private Long userId;

    private CouponTemplateSDK templateSDK;
}
