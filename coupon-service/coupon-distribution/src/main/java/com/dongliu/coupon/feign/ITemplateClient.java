package com.dongliu.coupon.feign;

import com.dongliu.coupon.feign.hystrix.TemplateClientHystrix;
import com.dongliu.coupon.vo.CommonResponse;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <h1>CouponTemplate service feign interface</h1>
 * use service_name and url to locate target downstream service.
 * fallback to handle downstream service issues.
 */
@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface ITemplateClient {

    /**
     * <h2>Find Template from CouponTemplate service</h2>
     * service_name + reqeust_URL locates the target microservice, not the method name
     */
    @RequestMapping(value = "/coupon-template/template/sdk/all", method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplates();

    /**
     * <h2>Find Template info with ID</h2>
     * service_name + reqeust_URL locates the target microservice, not the method name
     */
    @RequestMapping(value = "/coupon-template/template/sdk/infos", method = RequestMethod.GET)
    CommonResponse<Map<Integer, CouponTemplateSDK>> findIds2TemplateSDK(@RequestParam("ids") Collection<Integer> ids);

}
