package com.dongliu.coupon.service;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.CouponCategory;
import com.dongliu.coupon.constant.DistributionTarget;
import com.dongliu.coupon.constant.PeriodType;
import com.dongliu.coupon.constant.ProductLine;
import com.dongliu.coupon.vo.TemplateRequest;
import com.dongliu.coupon.vo.TemplateRule;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math.distribution.Distribution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;


@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {

    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception {

        System.out.println(JSON.toJSONString(buildTemplateService.buildTemplate(fakeTemplateRequest())
        ));

        // Must sleep. To make sure there is enough time for the async tasks. Otherwise, it will early terminate without
        // allowing the async task to finish.
        Thread.sleep(5000);
    }

    /**
     * <h2>fake TemplateRequest</h2>
     * */
    private TemplateRequest fakeTemplateRequest() {

        TemplateRequest request = new TemplateRequest();
        request.setName("CouponTemplate-" + new Date().getTime());
        request.setLogo("http://www.mycoupon.com/logo/test1");
        request.setDesc("This is a testing template");
        request.setCategory(CouponCategory.EXCEED_RETURN.getCode());
        request.setProductLine(ProductLine.Kitty.getCode());
        request.setCount(10000);
        request.setUserId(10001L);  // fake user id
        request.setTarget(DistributionTarget.SINGLE.getCode());

        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(
                PeriodType.SHIFT.getCode(),
                1, DateUtils.addDays(new Date(), 60).getTime()
        ));
        rule.setDiscount(new TemplateRule.Discount(5, 1));
        rule.setLimit(1);
        rule.setUsage(new TemplateRule.Usage(
                "Hawaii", "Maui",
                JSON.toJSONString(Arrays.asList("Entertainment", "Living"))
        ));
        rule.setCombination(JSON.toJSONString(Collections.EMPTY_LIST));

        request.setRule(rule);

        return request;
    }
}
