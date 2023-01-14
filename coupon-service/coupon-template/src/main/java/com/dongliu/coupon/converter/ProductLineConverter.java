package com.dongliu.coupon.converter;

import com.dongliu.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;

public class ProductLineConverter implements AttributeConverter<ProductLine, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductLine productLine) {
        return productLine.getCode();
    }

    @Override
    public ProductLine convertToEntityAttribute(Integer code) {
        return ProductLine.of(code);
    }
}
