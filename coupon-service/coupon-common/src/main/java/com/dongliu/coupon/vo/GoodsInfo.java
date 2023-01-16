package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfo {
    private Integer type;
    private Double price;
    private Integer count;
}
