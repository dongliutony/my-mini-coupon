package com.dongliu.coupon.service;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.constant.CouponCategory;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.executor.ExecutorManager;
import com.dongliu.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

@Slf4j(topic = "ExecutorManagerTest")
@SpringBootTest
@RunWith(SpringRunner.class)
public class ExecutorManagerTest {

    private Long fakeUserId = 20001L;

    @Autowired
    private ExecutorManager manager;

    @Test
    public void testThresholdExecutorComputeRule() throws CouponException {
        log.info("Threshold Coupon Executor Test");
        SettlementInfo thresholdInfo = fakeThresholdCouponSettlement();
        SettlementInfo result = manager.computeWithRule(thresholdInfo);

        log.info("{}", result.getTotalPrice());
        log.info("{}", result.getCouponAndTemplateInfoList().size());
        log.info("{}", result.getCouponAndTemplateInfoList());
    }

    @Test
    public void testThresholdAndDiscountExecutorComputeRule() throws CouponException {
        log.info("Threshold and Discount Coupons Executor Test");
        SettlementInfo thresholdDiscountInfo = fakeThresholdAndDiscountCouponSettlement();

        SettlementInfo result = manager.computeWithRule(thresholdDiscountInfo);

        log.info("{}", result.getTotalPrice());
        log.info("{}", result.getCouponAndTemplateInfoList().size());
        log.info("{}", result.getCouponAndTemplateInfoList());
    }

    /**
     * <h2>fake(mock) ThresholdCoupon settlement info</h2>
     * */
    private SettlementInfo fakeThresholdCouponSettlement() {

        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setConsumed(false);
        info.setTotalPrice(0.0);

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(2);
        goodsInfo01.setPrice(10.88);
        goodsInfo01.setType(GoodsType.STATIONARY.getCode());

        GoodsInfo goodsInfo02 = new GoodsInfo();
        // meet Threshold coupon baseline.
        goodsInfo02.setCount(10);
        // not meet Threshold coupon baseline.
//        goodsInfo02.setCount(5);
        goodsInfo02.setPrice(20.88);
        goodsInfo02.setType(GoodsType.STATIONARY.getCode());

        info.setGoodsInfoList(Arrays.asList(goodsInfo01, goodsInfo02));

        SettlementInfo.CouponAndTemplateInfo ctInfo = new SettlementInfo.CouponAndTemplateInfo();
        ctInfo.setCouponId(1);

        CouponTemplateSDK templateSDK = new CouponTemplateSDK();
        templateSDK.setId(1);
        templateSDK.setCategory(CouponCategory.THRESHOLD.getCode());
        templateSDK.setKey("100120190801");

        TemplateRule rule = new TemplateRule();
        rule.setDiscount(new TemplateRule.Discount(20, 199));
        rule.setUsage(new TemplateRule.Usage("California", "Santa Cruz",
                JSON.toJSONString(Arrays.asList(
                        GoodsType.STATIONARY.getCode(),
                        GoodsType.FURNITURE.getCode()
                ))));
        templateSDK.setRule(rule);

        ctInfo.setTemplateSDK(templateSDK);

        info.setCouponAndTemplateInfoList(Collections.singletonList(ctInfo));

        return info;
    }

    private SettlementInfo fakeThresholdAndDiscountCouponSettlement() {

        SettlementInfo info = new SettlementInfo();
        info.setUserId(fakeUserId);
        info.setConsumed(false);
        info.setTotalPrice(0.0);

        GoodsInfo goodsInfo01 = new GoodsInfo();
        goodsInfo01.setCount(2);
        goodsInfo01.setPrice(10.88);
        goodsInfo01.setType(GoodsType.STATIONARY.getCode());

        GoodsInfo goodsInfo02 = new GoodsInfo();
        goodsInfo02.setCount(10);
        goodsInfo02.setPrice(20.88);
        goodsInfo02.setType(GoodsType.STATIONARY.getCode());

        info.setGoodsInfoList(Arrays.asList(goodsInfo01, goodsInfo02));

        // Threshold Coupon
        SettlementInfo.CouponAndTemplateInfo thresholdInfo = new SettlementInfo.CouponAndTemplateInfo();
        thresholdInfo.setCouponId(1);

        CouponTemplateSDK thresholdTemplate = new CouponTemplateSDK();
        thresholdTemplate.setId(1);
        thresholdTemplate.setCategory(CouponCategory.THRESHOLD.getCode());
        thresholdTemplate.setKey("100120190712");

        TemplateRule thresholdRule = new TemplateRule();
        thresholdRule.setDiscount(new TemplateRule.Discount(20, 199));
        thresholdRule.setUsage(new TemplateRule.Usage("安徽省", "桐城市",
                JSON.toJSONString(Arrays.asList(
                        GoodsType.STATIONARY.getCode(),
                        GoodsType.FURNITURE.getCode()
                ))));
        thresholdRule.setCombination(JSON.toJSONString(Collections.emptyList()));
        thresholdTemplate.setRule(thresholdRule);
        thresholdInfo.setTemplateSDK(thresholdTemplate);

        // Discount Coupon
        SettlementInfo.CouponAndTemplateInfo discountInfo = new SettlementInfo.CouponAndTemplateInfo();
        discountInfo.setCouponId(1);

        CouponTemplateSDK discountTemplate = new CouponTemplateSDK();
        discountTemplate.setId(2);
        discountTemplate.setCategory(CouponCategory.DISCOUNT.getCode());
        discountTemplate.setKey("100220190712");

        TemplateRule discountRule = new TemplateRule();
        discountRule.setDiscount(new TemplateRule.Discount(85, 1));
        discountRule.setUsage(new TemplateRule.Usage("California", "Cupertino",
                JSON.toJSONString(Arrays.asList(
                        GoodsType.STATIONARY.getCode(),
                        GoodsType.FURNITURE.getCode()
                ))));
        discountRule.setCombination(JSON.toJSONString(Collections.singletonList("1001201907120001")));
        discountTemplate.setRule(discountRule);
        discountInfo.setTemplateSDK(discountTemplate);

        info.setCouponAndTemplateInfoList(Arrays.asList(thresholdInfo, discountInfo));

        return info;
    }
}
