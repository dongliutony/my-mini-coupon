package com.dongliu.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>Rate Limit filter</h1>
 */
@Slf4j
@Component
public class RateLimitFilter extends AbsPreZuulFilter {

    /**
     * <h2>Google's token bucket rate limiter</h2>
     */
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();

        if(rateLimiter.tryAcquire()) {
            log.info("got rate token successfully.");
            return success();
        } else {
            log.error("reached rate limit: {}", request.getRequestURI());
            return fail(429, "error: too many requests");
        }
    }

    // enforce rate limit as early as possible
    @Override
    public int filterOrder() {
        return 2;
    }
}
