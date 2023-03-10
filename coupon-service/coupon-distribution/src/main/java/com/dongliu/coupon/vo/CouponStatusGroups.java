package com.dongliu.coupon.vo;

import com.dongliu.coupon.constant.CouponStatus;
import com.dongliu.coupon.constant.PeriodType;
import com.dongliu.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponStatusGroups {
    private List<Coupon> usable;
    private List<Coupon> used;
    private List<Coupon> expired;

    public static CouponStatusGroups classify(List<Coupon> coupons) {
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {
            boolean isTimeExpired;
            long curTime = new Date().getTime();

            if (c.getTemplateSDK().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                isTimeExpired = c.getTemplateSDK().getRule().getExpiration().getPeriod() <= curTime;
            } else {
                isTimeExpired = DateUtils.addDays(c.getAssignTime(),
                        c.getTemplateSDK().getRule().getExpiration().getGap()).getTime() <= curTime;
            }

            if (c.getStatus() == CouponStatus.USED) {
                used.add(c);
            } else if(c.getStatus() == CouponStatus.EXPIRED || isTimeExpired) {
                expired.add(c);
            } else {
                usable.add(c);
            }
        });

        return new CouponStatusGroups(usable, used, expired);
    }
}
