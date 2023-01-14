package com.dongliu.coupon.controller;

import com.dongliu.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class HealthCheck {

    private final DiscoveryClient client;
    private final Registration registration;

    @Autowired
    public HealthCheck(DiscoveryClient client, Registration registration) {
        this.client = client;
        this.registration = registration;
    }

    /**
     * <h2>健康检查接口</h2>
     * 127.0.0.1:7001/coupon-template/health
     * 127.0.0.1:9000/my-coupon/coupon-template/health
     */
    @GetMapping("/health")
    public String health() {
        log.debug("view health api");
        return "CouponTemplate Is OK!";
    }

    /**
     * <h2>异常测试接口</h2>
     * 127.0.0.1:7001/coupon-template/exception
     * 127.0.0.1:9000/my-coupon/coupon-template/exception
     */
    @GetMapping("/exception")
    public String exception() throws CouponException {

        log.debug("view exception api");
        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * <h2>Eureka Server microsevice's meta info</h2>
     * 127.0.0.1:7001/coupon-template/info
     * 127.0.0.1:9000/my-coupon/coupon-template/info
     */
    @GetMapping("/info")
    public List<Map<String, Object>> info() {

        // it takes 2 minutes to pull up registration info
        List<ServiceInstance> instances =
                client.getInstances(registration.getServiceId());

        List<Map<String, Object>> result =
                new ArrayList<>(instances.size());

        instances.forEach(i -> {
            Map<String, Object> info = new HashMap<>();
            info.put("serviceId", i.getServiceId());
            info.put("instanceId", i.getInstanceId());
            info.put("port", i.getPort());

            result.add(info);
        });

        return result;
    }

}
