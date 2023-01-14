package com.dongliu.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>Token param presence checking filter</h1>
 * Can disable this filter by comment out the @Component annotation.
 */
@Slf4j
//@Component
public class TokenFilter extends AbsPreZuulFilter{
    @Override
    protected Object cRun() {
        HttpServletRequest request = requestContext.getRequest();
        log.info("request {}: {}", request.getMethod(), request.getRequestURI().toString());

        Object token = request.getParameter("token");
        if (null == token) {
            log.error("error: token param is missing.");
            return fail(401, "token missing");
        }

        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
