package com.dongliu.coupon.filter;


import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public abstract class AbsPreZuulFilter extends AbsZuulFilter{

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
}
