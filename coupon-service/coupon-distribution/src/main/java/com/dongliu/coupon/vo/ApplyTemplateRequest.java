package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyTemplateRequest {
    private Long userId;

    private CouponTemplateSDK templateSDK;
}
