package com.dongliu.coupon.tryout;

import org.springframework.stereotype.Component;

@Component
public class PrintHello {
    public void testAop() throws InterruptedException {
        System.out.println("Start ...");
        Thread.sleep(3000);
        System.out.println("Done");
    }
}
