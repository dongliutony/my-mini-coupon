package com.dongliu.coupon.service.impl;

import com.dongliu.coupon.entity.Coupon;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.service.IUserService;
import com.dongliu.coupon.vo.ApplyTemplateRequest;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import com.dongliu.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {
        return null;
    }

    @Override
    public List<CouponTemplateSDK> findAvailableTemplates(Long userId) throws CouponException {
        return null;
    }

    @Override
    public Coupon applyTemplate(ApplyTemplateRequest request) throws CouponException {
        return null;
    }

    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {
        return null;
    }
}
