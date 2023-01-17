package com.dongliu.coupon.service;

import com.dongliu.coupon.entity.Coupon;
import com.dongliu.coupon.exception.CouponException;

import java.util.List;

public interface IRedisService {

    List<Coupon> getCachedCoupons(Long userId, Integer status);

    void saveEmptyCouponListToCache(Long userId, List<Integer> statusList);

    String applyCouponCodeFromCache(Integer templateId);

    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException;

}
