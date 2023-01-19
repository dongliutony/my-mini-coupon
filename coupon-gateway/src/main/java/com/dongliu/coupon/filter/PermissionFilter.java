package com.dongliu.coupon.filter;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.permission.PermissionClient;
import com.dongliu.coupon.vo.CheckPermissionRequest;
import com.dongliu.coupon.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class PermissionFilter extends AbsSecurityFilter{
    private final PermissionClient permissionClient;

    @Autowired
    public PermissionFilter(PermissionClient permissionClient) {
        this.permissionClient = permissionClient;
    }

    @Override
    protected Boolean interceptCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // get userId from Header
        Long userId = Long.valueOf(request.getHeader("userId"));
        String uri = request.getRequestURI().substring("/imooc".length());
        String httpMethod = request.getMethod();

        return permissionClient.checkPermission(new CheckPermissionRequest(userId, uri, httpMethod)
        );
    }

    @Override
    protected int getHttpStatus() {
        return HttpStatus.OK.value();
    }

    @Override
    protected String getErrorMsg() {
        CommonResponse<Object> response = new CommonResponse<>();
        response.setCode(-2);
        response.setMessage("No privileges to do operations.");

        return JSON.toJSONString(response);
    }
}
