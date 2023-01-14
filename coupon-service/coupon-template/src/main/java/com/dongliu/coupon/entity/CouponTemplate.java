package com.dongliu.coupon.entity;

import com.dongliu.coupon.constant.CouponCategory;
import com.dongliu.coupon.constant.DistributionTarget;
import com.dongliu.coupon.constant.ProductLine;
import com.dongliu.coupon.converter.CouponCategoryConverter;
import com.dongliu.coupon.converter.DistributionTargetConverter;
import com.dongliu.coupon.converter.ProductLineConverter;
import com.dongliu.coupon.converter.RuleConverter;
import com.dongliu.coupon.serializer.CouponTemplateSerializer;
import com.dongliu.coupon.vo.TemplateRule;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.netflix.discovery.provider.Serializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon_template")
@JsonSerialize(using = CouponTemplateSerializer.class)
public class CouponTemplate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo", nullable = false)
    private String logo;

    @Column(name = "intro", nullable = false)
    private String desc;

    @Column(name = "category", nullable = false)
    @Convert(converter = CouponCategoryConverter.class)
    private CouponCategory category;

    @Column(name = "product_line", nullable = false)
    @Convert(converter = ProductLineConverter.class)
    private ProductLine productLine;

    @Column(name = "coupon_count", nullable = false)
    private Integer count;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "template_key", nullable = false)
    private String key;

    @Column(name = "target", nullable = false)
    @Convert(converter = DistributionTargetConverter.class)
    private DistributionTarget target;

    @Column(name = "rule", nullable = false)
    @Convert(converter = RuleConverter.class)
    private TemplateRule rule;

    public CouponTemplate(String name, String logo, String desc, String category,
                          Integer productLine, Integer count, Long userId,
                          Integer target, TemplateRule rule) {

        this.available = false;
        this.expired = false;
        this.name = name;
        this.logo = logo;
        this.desc = desc;
        this.category = CouponCategory.of(category);
        this.productLine = ProductLine.of(productLine);
        this.count = count;
        this.userId = userId;
        // CouponTemplate key: 4(productLine + type) + 8(date:yyyyMMdd) + id(extend to 4 bit)
        this.key = productLine.toString() + category +
                new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.target = DistributionTarget.of(target);
        this.rule = rule;
    }
}
