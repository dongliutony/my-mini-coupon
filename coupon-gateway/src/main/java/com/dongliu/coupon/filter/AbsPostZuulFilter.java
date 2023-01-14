package com.dongliu.coupon.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public abstract class AbsPostZuulFilter extends AbsZuulFilter{

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
