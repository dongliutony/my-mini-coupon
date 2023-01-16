package com.dongliu.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>Settlement Info</h1>
 * 1. userId
 * 2. goods info list
 * 3. coupon list
 * 4. final price
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {
    private Long userId;
    private List<GoodsInfo> goodsInfoList;
    private List<CouponAndTemplateInfo> couponAndTemplateInfoList;

    /** If the coupon is used */
    private Boolean consumed;

    /** Total price after using coupon */
    private Double totalPrice;

    /**
     * <h2>CouponId and Template info</h2>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo{
        private Integer couponId;
        private CouponTemplateSDK templateSDK;
    }

}
