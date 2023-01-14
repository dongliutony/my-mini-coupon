package com.dongliu.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory, String> {
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    @Override
    public CouponCategory convertToEntityAttribute(String code) {
        return CouponCategory.of(code);
    }
}
