package com.dongliu.coupon.repository;

import com.dongliu.coupon.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Integer> {

    UserRoleMapping findByUserId(Long userId);
}
