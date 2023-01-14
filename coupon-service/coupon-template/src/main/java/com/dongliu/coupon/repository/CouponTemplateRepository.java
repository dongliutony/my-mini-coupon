package com.dongliu.coupon.repository;

import com.dongliu.coupon.entity.CouponTemplate;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <h1>CouponTemplate Repository</h1>
 */
public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Integer> {

    /**
     * <h2>find a CouponTemplate by its name</h2>
     * where name = xxx
     */
    CouponTemplate findByName(String name);

    /**
     * <h2>find all valid CouponTemplate by available and expired status</h2>
     * where available = xxx and expired = xxx
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);

    /**
     * <h2>find all CouponTemplate by expired status</h2>
     * where expired = xxx
     */
    List<CouponTemplate> findAllByExpired(Boolean expired);
}
