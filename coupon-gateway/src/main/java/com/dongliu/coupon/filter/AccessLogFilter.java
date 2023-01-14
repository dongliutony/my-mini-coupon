package com.dongliu.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>A PRE filter to handle common log</h1>
 */
@Slf4j
@Component
public class AccessLogFilter extends AbsPostZuulFilter{

    /**
     * <h2>the concrete cRun() method to handle Access log</h2>
     * @return
     */
    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();

        // get startTime from context (which is inserted by PreRequestFilter)
        Long startTime = (Long) requestContext.get("startTime");
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;

        log.info("uri: {}, duration: {}ms", uri, duration);

        return success();
    }

    // second last POST filter to calculate whole request processing duration.
    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
