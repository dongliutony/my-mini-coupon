package com.dongliu.coupon.feign.hystrix;

import com.dongliu.coupon.feign.ITemplateClient;
import com.dongliu.coupon.vo.CommonResponse;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <h1>CouponTemplate feign circuit breaker and fallback policies </h1>
 */
@Slf4j
@Component
public class TemplateClientHystrix implements ITemplateClient {
    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplates() {
        // log that a fallback happened.
        log.error("[eureka-client-coupon-template] findAllUsableTemplate request error");

        // return to caller an empty list of result
        return new CommonResponse<>(-1, "[eureka-client-coupon-template] request error",
                Collections.emptyList());
    }

    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>> findIds2TemplateSDK(Collection<Integer> ids) {
        // log that a fallback happened.
        log.error("[eureka-client-coupon-template] findIds2TemplateSDK request error");

        // return to caller an empty list of result
        return new CommonResponse<>(-1, "[eureka-client-coupon-template] request error",
                new HashMap<>());
    }
}
