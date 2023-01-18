package com.dongliu.coupon.tryout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PocUseMyMethodTimer {

    @MyMethodTimer(key = "key1", value = "value1")
    public void printSomething() throws InterruptedException {
        log.info("Print something ...");
        Thread.sleep(3000);
        log.info("Print something, Done.");
    }
}
