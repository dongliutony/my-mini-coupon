package com.dongliu.coupon.service;

import com.dongliu.coupon.constant.RoleEnum;
import com.dongliu.coupon.entity.Path;
import com.dongliu.coupon.entity.Role;
import com.dongliu.coupon.entity.RolePathMapping;
import com.dongliu.coupon.entity.UserRoleMapping;
import com.dongliu.coupon.repository.PathRepository;
import com.dongliu.coupon.repository.RolePathMappingRepository;
import com.dongliu.coupon.repository.RoleRepository;
import com.dongliu.coupon.repository.UserRoleMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PermissionService {

    private final PathRepository pathRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final RolePathMappingRepository rolePathMappingRepository;

    @Autowired
    public PermissionService(PathRepository pathRepository,
                             RoleRepository roleRepository,
                             UserRoleMappingRepository userRoleMappingRepository,
                             RolePathMappingRepository rolePathMappingRepository) {
        this.pathRepository = pathRepository;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
        this.rolePathMappingRepository = rolePathMappingRepository;
    }

    /**
     * <h2>valid a user has permission to access the API</h2>
     * @return true/false
     * */
    public Boolean checkPermission(Long userId, String uri, String httpMethod) {

        UserRoleMapping userRoleMapping = userRoleMappingRepository.findByUserId(userId);

        // if no user_role record, return false.
        // there is some error. tag a log for future investigation
        if (null == userRoleMapping) {
            log.error("userId not exist is UserRoleMapping: {}", userId);
            return false;
        }

        // no record for specific role, return false
        Optional<Role> role = roleRepository.findById(userRoleMapping.getRoleId());
        if (!role.isPresent()) {
            log.error("roleId not exist in Role: {}", userRoleMapping.getRoleId());
            return false;
        }

        // if user is Super_admin, return True
        if (role.get().getRoleTag().equals(RoleEnum.SUPER_ADMIN.name())) {
            return true;
        }

        // if specific path not registered, the permission check is ignored, and return true
        Path path = pathRepository.findByPathPatternAndHttpMethod(uri, httpMethod);
        if (null == path) {
            return true;
        }

        RolePathMapping rolePathMapping = rolePathMappingRepository.findByRoleIdAndPathId(role.get().getId(), path.getId());

        return rolePathMapping != null;
    }
}

