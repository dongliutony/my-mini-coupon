package com.dongliu.coupon;

import com.dongliu.coupon.constant.OpModeEnum;
import com.dongliu.coupon.permission.PermissionClient;
import com.dongliu.coupon.vo.CommonResponse;
import com.dongliu.coupon.vo.CreatePathRequest;
import com.dongliu.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>permission registry</h1>
 */
@Slf4j
public class PermissionRegistry {

    /**
     * permission SDK client
     */
    private PermissionClient permissionClient;

    /**
     * service name
     */
    private String serviceName;

    PermissionRegistry(PermissionClient permissionClient, String serviceName) {
        this.permissionClient = permissionClient;
        this.serviceName = serviceName;
    }

    /**
     * <h2>register permission</h2>
     */
    boolean register(List<PermissionInfo> infoList) {
        if (CollectionUtils.isEmpty(infoList)) {
            return false;
        }

        List<CreatePathRequest.PathInfo> pathInfos = infoList.stream()
                .map(info -> CreatePathRequest.PathInfo.builder()
                        .pathPattern(info.getUrl())
                        .httpMethod(info.getMethod())
                        .pathName(info.getDescription())
                        .serviceName(serviceName)
                        .opMode(info.getIsReadOnly() ? OpModeEnum.READ.name() : OpModeEnum.WRITE.name())
                        .build()
                ).collect(Collectors.toList());

        CommonResponse<List<Integer>> response = permissionClient.createPath(new CreatePathRequest(pathInfos));

        if (!CollectionUtils.isEmpty(response.getData())) {
            log.info("register path info: {}", response.getData());
            return true;
        }

        return false;
    }
}
