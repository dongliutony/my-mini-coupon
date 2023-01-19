package com.dongliu.coupon.controller;

import com.dongliu.coupon.annotation.IgnoreResponseAdvice;
import com.dongliu.coupon.service.PathService;
import com.dongliu.coupon.service.PermissionService;
import com.dongliu.coupon.vo.CheckPermissionRequest;
import com.dongliu.coupon.vo.CreatePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PermissionController {

    private final PathService pathService;
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PathService pathService, PermissionService permissionService) {
        this.pathService = pathService;
        this.permissionService = permissionService;
    }

    /**
     * <h2>Create Path</h2>
     * */
    @PostMapping("/create/path")
    public List<Integer> createPath(@RequestBody CreatePathRequest request) {

        log.info("createPath: {}", request.getPathInfos().size());

        return pathService.createPath(request);
    }

    /**
     * <h2>valid permission</h2>
     * */
    @IgnoreResponseAdvice
    @PostMapping("/check/permission")
    public Boolean checkPermission(@RequestBody CheckPermissionRequest request) {

        log.info("checkPermission for args: {}, {}, {}", request.getUserId(), request.getUri(), request.getHttpMethod());

        return permissionService.checkPermission(request.getUserId(), request.getUri(), request.getHttpMethod());
    }
}
