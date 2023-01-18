package com.dongliu.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.executor.ExecutorManager;
import com.dongliu.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SettlementController {

    private final ExecutorManager executorManager;

    @Autowired
    public SettlementController(ExecutorManager executorManager) {
        this.executorManager = executorManager;
    }

    public SettlementInfo computeWithRule(SettlementInfo settlementInfo) throws CouponException {
        log.info("settlement: {}", JSON.toJSONString(settlementInfo));
        return executorManager.computeWithRule(settlementInfo);
    }
}
