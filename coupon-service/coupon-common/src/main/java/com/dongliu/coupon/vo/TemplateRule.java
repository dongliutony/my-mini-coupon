
package com.dongliu.coupon.vo;

import com.dongliu.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {

    // coupon expiration information
    private Expiration expiration;

    // discount information
    private Discount discount;

    // how many coupon of this kind one user can apply
    private Integer limit;

    // usage scope, including location and goods types.
    private Usage usage;

    // list the other coupons that can be used together with current coupon.
    private String combination;

    public boolean validate() {

        return expiration.validate() && discount.validate()
                && limit > 0 && usage.validate()
                && StringUtils.isNotEmpty(combination);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expiration{

        // correspond to PeriodType code.
        private Integer period;

        // coupon usage period. Only valid for SWIFT PeriodType.
        private Integer gap;

        // coupon expiration date. Valid for both PeriodType.
        private Long deadline;
        boolean validate() {
            return null != PeriodType.of(period) && gap > 0 && deadline > 0;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Discount{

        // discount amount: e.g. THRESHOLD(20) means return $20 when total price exceeds a base line,
        // DISCOUNT(85) means 15% off,
        // INSTANT(20) means return $20 without extra condition required.
        private Integer credit;

        // valid for EXCEED_RETURN type. the minimum total price that makes a coupon valid to be used.
        private Integer base;

        boolean validate() {
            return credit > 0 && base > 0;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {

        // coupon can be used in which State
        private String state;
        // which city
        private String city;
        // coupon can be used on which goods type
        private String goodsType;

        boolean validate() {
            return StringUtils.isNotEmpty(state) && StringUtils.isNotEmpty(city) && StringUtils.isNotEmpty(goodsType);
        }
    }
}
