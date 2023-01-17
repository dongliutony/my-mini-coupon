package com.dongliu.coupon.feign;

import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.feign.hystrix.SettlementClientHystrix;
import com.dongliu.coupon.feign.hystrix.TemplateClientHystrix;
import com.dongliu.coupon.vo.CommonResponse;
import com.dongliu.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "eureka-client-coupon-settlement", fallback = SettlementClientHystrix.class)
public interface ISettlementClient {

    // TODO: add ISettleMentClient and fallback handler after Settlement Module
    @RequestMapping(value = "/coupon-settlement/settlement/compute",
            method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(
            @RequestBody SettlementInfo settlement) throws CouponException;
}
