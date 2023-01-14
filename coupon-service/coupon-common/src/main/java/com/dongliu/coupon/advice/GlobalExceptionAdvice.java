package com.dongliu.coupon.advice;

import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    // Handle CouponException
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handleCouponException(HttpServletRequest req, CouponException exception) {
        CommonResponse<String> response = new CommonResponse<>(-1, "coupon business error.");
        response.setData(exception.getMessage());

        return response;
    }
}
