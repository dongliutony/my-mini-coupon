package com.dongliu.coupon.repository;

import com.dongliu.coupon.constant.CouponStatus;
import com.dongliu.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    List<Coupon> findAllByUserIdAndStatus(Integer userId, CouponStatus status);

    List<Coupon> findAllByUserId(Integer userId);
}
