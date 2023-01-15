package com.dongliu.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.service.IBuildTemplateService;
import com.dongliu.coupon.service.ITemplateBaseService;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import com.dongliu.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CouponTemplateController {

    private final IBuildTemplateService buildTemplateService;
    private final ITemplateBaseService templateBaseService;

    @Autowired
    public CouponTemplateController(IBuildTemplateService buildTemplateService,
                                    ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    /**
     * <h2>build template</h2>
     * 127.0.0.1:7001/coupon-template/template/build
     * 127.0.0.1:9000/my-coupon/coupon-template/template/build
     * */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request)
            throws CouponException {
        log.info("Build Template: {}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }

    /**
     * <h2>构造优惠券模板详情</h2>
     * 127.0.0.1:7001/coupon-template/template/info?id=1
     * 127.0.0.1:9000/my-coupon/coupon-template/template/info?id=1
     * */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id)
            throws CouponException {
        log.info("Build Template Info For: {}", id);
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * <h2>find all templates</h2>
     * 127.0.0.1:7001/coupon-template/template/sdk/all
     * 127.0.0.1:9000/my-coupon/coupon-template/template/sdk/all
     * */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        log.info("Find All Usable Template.");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * <h2>find templates by IDs</h2>
     * 127.0.0.1:7001/coupon-template/template/sdk/infos?ids=1,2,3
     * 127.0.0.1:9000/my-coupon/coupon-template/template/sdk/infos?ids=1,2,3
     * */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(
            @RequestParam("ids") Collection<Integer> ids
    ) {
        log.info("FindIds2TemplateSDK: {}", JSON.toJSONString(ids));
        return templateBaseService.findIds2TemplateSDK(ids);
    }
}
