package com.dongliu.coupon.tryout;

import com.dongliu.coupon.exception.CouponException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(3)
@Component
public class TryTimingAspect {

    /**
     * <h2>AOP interceptor</h2>
     * must specify the joinPoint(target/advised method) in the "execution(...)" statement
     * @param joinPoint the target/advised method to be intercepted
     * @return target/advised method returned object
     * @throws Throwable
     */
    @Around("execution(* com.dongliu.coupon.tryout.PrintHello.testAop())")
    public Object logExecutionTime(@NotNull(value = "cannot be null", exception = IllegalArgumentException.class) ProceedingJoinPoint joinPoint) throws Throwable {
        // before method(joinPoint) is proceeded
        long start = System.currentTimeMillis();
        // let method run
        Object proceed = joinPoint.proceed();
        // after method is proceeded
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }

}
