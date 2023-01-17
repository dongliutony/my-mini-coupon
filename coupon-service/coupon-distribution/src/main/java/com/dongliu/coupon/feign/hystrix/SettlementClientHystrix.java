package com.dongliu.coupon.feign.hystrix;

import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.feign.ISettlementClient;
import com.dongliu.coupon.vo.CommonResponse;
import com.dongliu.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SettlementClientHystrix implements ISettlementClient {

    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlement) throws CouponException {

        log.error("[eureka-client-coupon-settlement] computeRule request error");

        settlement.setConsumed(false);
        settlement.setTotalPrice(-1.0);

        return new CommonResponse<>(
                -1,
                "[eureka-client-coupon-settlement] request error",
                settlement
        );
    }
}
