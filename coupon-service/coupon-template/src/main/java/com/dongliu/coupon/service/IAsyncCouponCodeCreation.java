package com.dongliu.coupon.service;


import com.dongliu.coupon.entity.CouponTemplate;

/**
 * <h1>asyncly create coupon codes</h1>
 */
public interface IAsyncCouponCodeCreation {

    void asyncCreateCouponByTemplate(CouponTemplate template);
}
