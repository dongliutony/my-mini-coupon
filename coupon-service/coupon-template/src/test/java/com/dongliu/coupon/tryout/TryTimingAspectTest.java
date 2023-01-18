package com.dongliu.coupon.tryout;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TryTimingAspectTest extends TestCase {

    @Autowired
    private PrintHello printHello;

    @Test
    public void testHello() throws InterruptedException {
        printHello.testAop();
    }

}
