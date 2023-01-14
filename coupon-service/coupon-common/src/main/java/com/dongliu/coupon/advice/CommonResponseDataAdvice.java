package com.dongliu.coupon.advice;

import com.dongliu.coupon.annotation.IgnoreResponseAdvice;
import com.dongliu.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * <h1>Global response for RestRequest</h1>
 * response as JSON. Don't use it for other response format.
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
    /**
     * <h2>Check if wrap the original response</h2>
     * @return true: use beforeBodyWrite() to wrap original response; false: not wrap
     */
    @Override
    public boolean supports(MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        // if the controller class has a @IgnoreResponseAdvice, don't wrap it
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        // if the controller method has a @IgnoreResponseAdvice, don't wrap it
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        // by default wrap a Rest request
        return true;
    }

    /**
     * Process a response before it returns
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // define the return object
        CommonResponse<Object> response = new CommonResponse<>(0, "");

        // if original response o is null, no need to set data. Return a minimal common response.
        if (null == o) {
            return response;
        // if o is already a CommonResponse, don't redundantly process it
        } else if(o instanceof CommonResponse) {
            return (CommonResponse<Object>) o;
        // if o is present and response is a raw one, wrap it
        } else {
            response.setData(o);
            return response;
        }
    }
}
