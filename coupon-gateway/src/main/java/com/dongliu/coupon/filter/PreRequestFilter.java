package com.dongliu.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <h1>insert a request arriving timestamp in the ThreadLocal context</h1>
 */
@Slf4j
@Component
public class PreRequestFilter extends AbsPreZuulFilter{
    @Override
    protected Object cRun() {
        requestContext.set("startTime", System.currentTimeMillis());

        // hand request to the next filter
        return success();
    }

    /**
     * filterOrder() must be defined for a filter. Filters may have the same filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return 1;
    }
}
