package com.dongliu.coupon;

import com.dongliu.coupon.annotation.CouponPermission;
import com.dongliu.coupon.annotation.IgnorePermission;
import com.dongliu.coupon.constant.HttpMethodEnum;
import com.dongliu.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <h1>Microservice Controller interface permission info scanner</h1>
 */
@Slf4j
public class AnnotationScanner {
    private String pathPrefix;
    private static final String COUPON_PKG = "com.dongliu.coupon";


    AnnotationScanner(String prefix) {
        this.pathPrefix = trimPath(prefix);
    }

    /**
     * <h2>entry point of scanner</h2>
     * @param mappingInfo Map<{@link RequestMappingInfo}, {@link HandlerMethod}> passed in controller info
     * @return calculated permission info
     */
    List<PermissionInfo> scanPermission(Map<RequestMappingInfo, HandlerMethod> mappingInfo) {
        List<PermissionInfo> result = new ArrayList<>();
        mappingInfo.forEach((mapInfo, method) -> result.addAll(buildPermission(mapInfo, method)));

        return result;
    }

    /**
     * <h2>build permissions for controller</h2>
     * @param mapInfo {@link RequestMappingInfo} spring mvc request, controller's request method
     * @param handlerMethod {@link HandlerMethod} spring mvc handler method, request method details,
     *                                           such as java method, class, argument, etc.
     * @return permissionInfoList {@link PermissionInfo} all permission infos of the controller
     */
    private List<PermissionInfo> buildPermission(RequestMappingInfo mapInfo, HandlerMethod handlerMethod) {
        Method javaMethod = handlerMethod.getMethod();
        Class baseClass = javaMethod.getDeclaringClass();

        // ignore Non com.dongliu.coupon packages
        if(!isCouponPackage(baseClass.getName())) {
            log.debug("ignore method outside of package: {}", javaMethod.getName());
            return Collections.emptyList();
        }

        // check if specified in annotation to "ignore"
        IgnorePermission ignorePermission = javaMethod.getAnnotation(IgnorePermission.class);
        if (null != ignorePermission) {
            log.debug("ignore method by annotation: {}", javaMethod.getName());
            return Collections.emptyList();
        }

        // fetch permission annotation values
        CouponPermission couponPermission = javaMethod.getAnnotation(CouponPermission.class);
        // in target controller, if a method doesn't have @IgnorePermission or @CouponPermission, tag error in log
        if (null == couponPermission) {
            log.error("lack @CouponPermission -> {}#{}",
                    javaMethod.getDeclaringClass().getName(), javaMethod.getName());
            return Collections.emptyList();
        }

        // get URL from request
        Set<String> urlSet = mapInfo.getPatternsCondition().getPatterns();

        // get HTTP method from request
        boolean isAllMethods = false;
        Set<RequestMethod> methodSet = mapInfo.getMethodsCondition().getMethods();
        if (CollectionUtils.isEmpty(methodSet)) {
            isAllMethods = true;
        }

        List<PermissionInfo> permissionInfoList = new ArrayList<>();

        for (String url : urlSet) {
            // if support ALL HTTP method
            if (isAllMethods) {
                PermissionInfo info = buildPermissionInfo(
                        HttpMethodEnum.ALL.name(),
                        javaMethod.getName(),
                        this.pathPrefix + url,
                        couponPermission.readOnly(),
                        couponPermission.description(),
                        couponPermission.extra()
                );
                permissionInfoList.add(info);
                continue;
            }

            // or support some of all methods
            for (RequestMethod method : methodSet) {
                PermissionInfo info = buildPermissionInfo(
                        method.name(),
                        javaMethod.getName(),
                        this.pathPrefix + url,
                        couponPermission.readOnly(),
                        couponPermission.description(),
                        couponPermission.extra()
                );
                permissionInfoList.add(info);
                log.info("permission detected: {}", info);
            }
        }

        return permissionInfoList;
    }

    /**
     * <h2>build 1 permission info</h2>
     * @param reqMethod HTTP method in request
     * @param javaMethod method's name
     * @param path URL in request
     * @param readOnly permission op_mode in annotation
     * @param desc  desc in annotation
     * @param extra extra in annotation
     * @return {@link PermissionInfo} modified permission info
     */
    private PermissionInfo buildPermissionInfo(String reqMethod, String javaMethod, String path,
                                               boolean readOnly, String desc, String extra) {
        PermissionInfo info = new PermissionInfo();
        info.setMethod(reqMethod);
        info.setUrl(path);
        info.setIsReadOnly(readOnly);
        // use annotation meta value, or method name if annotation desc absent
        info.setDescription(StringUtils.isEmpty(desc) ? javaMethod : desc);
        info.setExtra(extra);

        return info;
    }

    /**
     * <h2>check if className belongs to coupon package</h2>
     * NOTE: full className comes up with packagePath + className
     */
    private boolean isCouponPackage(String className) {
        return className.startsWith(COUPON_PKG);
    }


    /**
     * <h2>make path begins with "/" and not ending with "/"</h2>
     * TODO: comprehensive trim for edge case paths.
     */
    private String trimPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
