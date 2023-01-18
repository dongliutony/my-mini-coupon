package com.dongliu.coupon.tryout;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MyMethodTimerAspect {

    // NOTE: here is the essential part. It uses Java reflection API to get @MyAnnotation instance
    // Similar to: object.getClass().isAnnotationPresent(MyAnnotation.class)
    // or: (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(MyAnnotation.class)
    @Pointcut("@annotation(com.dongliu.coupon.tryout.MyMethodTimer)")
    public void myPointCut(){
    }

    @Around("myPointCut()")
    public void around(ProceedingJoinPoint joinPoint){
        System.out.println("before method start...");
        try {
            StopWatch sw = new StopWatch("1");
            sw.start(joinPoint.toString());

            // get method arguments
            Object[] args = joinPoint.getArgs();
            MethodSignature sign = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = sign.getParameterNames();
            System.out.println("parameters list: ");
            for (int i = 0; i < parameterNames.length; i++) {
                System.out.println(parameterNames[i] + " = " + args[i]);
            }
            // get Annotation parameters
            MyMethodTimer annotation =  sign.getMethod().getAnnotation(MyMethodTimer.class);
            System.out.println("@MyAnnotation value = " + annotation.value());
            System.out.println("@MyAnnotation key = " + annotation.key());

            // run method
            joinPoint.proceed();

            //after advised method run
            sw.stop();
            System.out.println(sw.prettyPrint());

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
