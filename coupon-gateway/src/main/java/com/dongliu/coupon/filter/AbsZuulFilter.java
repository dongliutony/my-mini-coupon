package com.dongliu.coupon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * <h1>abstract Zuul Filter</h1>
 */
public abstract class AbsZuulFilter extends ZuulFilter {

    /** Passing messages among filters, and store data in each request's ThreadLocal ctx variable. It is a Map.  */
    RequestContext requestContext;

    private final static String NEXT = "next";

    /**
     * If an incoming request should trigger this filter and be checked by run().
     * @return true if the run() method should be invoked. false will not invoke the run() method
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) ctx.getOrDefault(NEXT, true); // by default, a request should be checked by a filter
    }

    /**
     * If shouldFilter() returns true, this method will be invoked. The run() is the core method of a ZuulFilter
     * @return Some arbitrary artifact may be returned. Current implementation ignores it.
     * @throws ZuulException if an error occurs during execution.
     */
    @Override
    public Object run() throws ZuulException {
        requestContext = RequestContext.getCurrentContext();

        return cRun();
    }

    protected abstract Object cRun();

    Object fail(int code, String message) {
        requestContext.set(NEXT, false);
        requestContext.setSendZuulResponse(false);
        requestContext.getResponse().setContentType("text/html; charset=UTF-8");
        requestContext.setResponseStatusCode(code);
        requestContext.setResponseBody(String.format("{\"result\": \"%s!\"}", message));

        return null;
    }

    Object success(){
        requestContext.set(NEXT, true);

        return null;
    }

}
