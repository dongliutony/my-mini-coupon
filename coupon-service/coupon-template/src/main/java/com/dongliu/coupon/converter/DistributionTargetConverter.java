package com.dongliu.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.DistributionTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DistributionTargetConverter implements AttributeConverter<DistributionTarget, String> {
    @Override
    public String convertToDatabaseColumn(DistributionTarget target) {
        return JSON.toJSONString(target);
    }

    @Override
    public DistributionTarget convertToEntityAttribute(String s) {
        return JSON.parseObject(s, DistributionTarget.class);
    }
}
