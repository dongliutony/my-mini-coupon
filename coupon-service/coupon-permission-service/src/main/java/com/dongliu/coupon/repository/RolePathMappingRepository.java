package com.dongliu.coupon.repository;

import com.dongliu.coupon.entity.RolePathMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePathMappingRepository extends JpaRepository<RolePathMapping, Integer> {

    RolePathMapping findByRoleIdAndPathId(Integer roleId, Integer pathId);
}
