package com.dongliu.coupon.vo;

import com.dongliu.coupon.constant.CouponCategory;
import com.dongliu.coupon.constant.DistributionTarget;
import com.dongliu.coupon.constant.ProductLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Target;

/**
 * <h1>Data for CouponTemplate creation</h1>
 * VO: value object passed among internal modules/methods
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {
    private String name;
    private String logo;
    private String desc;
    private String category;
    private Integer productLine;
    private Integer count;
    private Long userId;
    private Integer target;
    private TemplateRule rule;

    public boolean validate() {
        boolean stringValid = StringUtils.isNotEmpty(name)
                && StringUtils.isNotEmpty(logo)
                && StringUtils.isNotEmpty(desc);
        boolean enumValid = null != CouponCategory.of(category)
                && null != ProductLine.of(productLine)
                && null != DistributionTarget.of(target);
        boolean numValid = count > 0 && userId > 0;

        return stringValid && enumValid && numValid && rule.validate();
    }

}
