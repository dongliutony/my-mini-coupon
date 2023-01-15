package com.dongliu.coupon.constant;


/**
 * <h1>shared Constant across the whole project and its modules</h1>
 */
public class Constant {

    /**
     * <h2>Kafka topics</h2>
     */
    public static final String COUPON_OP_TOPIC = "user_coupon_op";

    /**
     * <h2>Redis Key prefix</h2>
     */
    public static class RedisPrefix {
        public static final String COUPON_TEMPLATE_CODE_ = "coupon_template_code_";
        public static final String USER_COUPON_USABLE = "user_coupon_usable_";
        public static final String USER_COUPON_USED = "user_coupon_used_";
        public static final String USER_COUPON_EXPIRED = "user_coupon_expired_";
    }
}
