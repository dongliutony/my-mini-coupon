package com.dongliu.coupon.service;

import com.dongliu.coupon.entity.Coupon;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.vo.AcquireTemplateRequest;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import com.dongliu.coupon.vo.SettlementInfo;

import java.util.List;

/**
 * <h1>User Services</h1>
 * 1. user's coupon display with status
 * 2. check coupons that a user can apply
 * 3. user apply coupons
 * 4. user calculate final price with coupons. This feature needs to work with Coupon-Settlement service
 */
public interface IUserService {
    List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException;

    List<CouponTemplateSDK> findAvailableTemplates(Long userId) throws CouponException;

    Coupon applyByTemplate(AcquireTemplateRequest request) throws CouponException;

    SettlementInfo evaluateSettlement(SettlementInfo info) throws CouponException;

}
