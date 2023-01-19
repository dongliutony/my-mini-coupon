package com.dongliu.coupon;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>config to use PermissionSDK</h1>
 * NOTE: essential part is enabling the Feign Clients, so that it can use PermissionSDK's feign client interfaces
 */
@Configuration
@EnableFeignClients
public class PermissionSDKConfig {
}
