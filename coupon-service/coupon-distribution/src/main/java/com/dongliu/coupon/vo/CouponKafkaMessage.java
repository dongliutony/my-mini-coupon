package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponKafkaMessage {
    // Coupon status
    private Integer status;

    // Coupon db Primary Key
    private List<Integer> ids;
}
