package com.dongliu.coupon.service.impl;

import com.dongliu.coupon.entity.Coupon;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {
    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {
        return null;
    }

    @Override
    public void saveEmptyCouponListToCache(Long userId, List<Integer> statusList) {

    }

    @Override
    public String applyCouponCodeFromCache(Integer templateId) {
        return null;
    }

    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        return null;
    }
}
