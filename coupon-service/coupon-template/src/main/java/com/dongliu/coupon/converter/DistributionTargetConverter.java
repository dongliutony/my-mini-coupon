package com.dongliu.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.DistributionTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DistributionTargetConverter implements AttributeConverter<DistributionTarget, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DistributionTarget target) {
        return target.getCode();
    }

    @Override
    public DistributionTarget convertToEntityAttribute(Integer code) {
        return DistributionTarget.of(code);
    }
}
