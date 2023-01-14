package com.dongliu.coupon.service;

import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ITemplateBaseService {

    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    List<CouponTemplateSDK> findAllUsableTemplate();

    Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);
}
