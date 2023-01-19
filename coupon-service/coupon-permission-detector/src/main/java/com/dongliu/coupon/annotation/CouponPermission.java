package com.dongliu.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>permission description: define an interface's required permission</h1>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CouponPermission {

    /**
     * <h2>describe the interface</h2>
     */
    String description() default "";

    /**
     * <h2>Is it read only</h2>
     */
    boolean readOnly() default true;

    /**
     * <h2>Reserved for future use</h2>
     */
    String extra() default "";
}
