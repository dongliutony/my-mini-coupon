package com.dongliu.coupon.serializer;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.entity.Coupon;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CouponSerializer extends JsonSerializer<Coupon> {
    @Override
    public void serialize(Coupon coupon, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject();

        generator.writeStringField("templateIdm", coupon.getId().toString());
        generator.writeStringField("userId", coupon.getUserId().toString());
        generator.writeStringField("couponCode", coupon.getCouponCode());
        generator.writeStringField("assignTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coupon.getAssignTime()));
        generator.writeStringField("name", coupon.getTemplateSDK().getName());
        generator.writeStringField("logo", coupon.getTemplateSDK().getLogo());
        generator.writeStringField("desc", coupon.getTemplateSDK().getDesc());
        generator.writeStringField("expiration",
                JSON.toJSONString(coupon.getTemplateSDK().getRule().getExpiration()));
        generator.writeStringField("discount",
                JSON.toJSONString(coupon.getTemplateSDK().getRule().getDiscount()));
        generator.writeStringField("usage",
                JSON.toJSONString(coupon.getTemplateSDK().getRule().getUsage()));

        generator.writeEndObject();
    }
}
