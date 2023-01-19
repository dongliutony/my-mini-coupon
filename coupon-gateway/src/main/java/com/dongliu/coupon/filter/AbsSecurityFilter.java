package com.dongliu.coupon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class AbsSecurityFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();

        // if previous filter return false, then don't filter
        return response.getStatus() == 0 || response.getStatus() == HttpStatus.OK.value();
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        log.info("filter {} begin check request {}.", this.getClass().getSimpleName(), request.getRequestURI());

        Boolean result = null;
        try {
            result = interceptCheck(request, response);
        } catch (Exception ex) {
            log.error("filter {} check request {}, throws exception {}.",
                    this.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage());
            throw new RuntimeException(ex);
        }

        log.info("filter {} finish check, result {}", this.getClass().getSimpleName(), result);

        if (result == null) {
            log.debug("Filter {} finish check, result is null.", this.getClass().getSimpleName());

            // Not pass permission check. Don't route request
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(getHttpStatus());
            return null;
        }

        if (!result) {
            try {
                // Not pass permission check. Don't route request
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(getHttpStatus());
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(getErrorMsg());
                ctx.setResponse(response);
            } catch (IOException ex) {
                log.error("filter {} check request {}, result is false, setResponse throws Exception {}",
                        this.getClass().getSimpleName(), request.getRequestURI(), ex.getMessage());
            }
        }

        return null;
    }

    /**
     * <h2> leave below methods abstract. let sub-filter to implement them</h2>
     */
    protected abstract Boolean interceptCheck(HttpServletRequest request, HttpServletResponse response) throws Exception;
    protected abstract int getHttpStatus();
    protected abstract String getErrorMsg();
}
