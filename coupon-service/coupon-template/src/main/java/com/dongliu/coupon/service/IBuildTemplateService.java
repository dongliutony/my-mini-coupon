package com.dongliu.coupon.service;

import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.vo.TemplateRequest;

/**
 * <h1>Build CouponTemplate</h1>
 */
public interface IBuildTemplateService {

    /**
     * <h2>Create a CouponTemplate</h2>
     * @param request {@link TemplateRequest} key info to create a template
     * @return {@link CouponTemplate} A template entity object (including id created by db)
     * @throws CouponException a default global Coupon Exception
     */
    CouponTemplate buildTemplate(TemplateRequest request) throws CouponException;
}
