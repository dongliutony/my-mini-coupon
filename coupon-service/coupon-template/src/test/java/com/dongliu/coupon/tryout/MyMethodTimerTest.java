package com.dongliu.coupon.tryout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyMethodTimerTest {

    @Autowired
    private PocUseMyMethodTimer pocUseMyMethodTimer;

    @Test
    public void testMyAnnotation() throws InterruptedException {
        pocUseMyMethodTimer.printSomething();
    }
}
