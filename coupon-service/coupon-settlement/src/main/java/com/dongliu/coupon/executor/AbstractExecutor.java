package com.dongliu.coupon.executor;

import com.alibaba.fastjson.JSON;
import com.dongliu.coupon.vo.GoodsInfo;
import com.dongliu.coupon.vo.SettlementInfo;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>Rule executor abstract class</h1>
 */
public abstract class AbstractExecutor {

    /**
     * <h2>verify types match</h2>
     * NOTE:
     * 1. here is the implementation of single coupon. Override the method for combined coupons.
     * 2. Purchasing goods type matches one of the coupons is good enough here.
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlementInfo) {
        List<Integer> goodsTypes = settlementInfo.getGoodsInfoList()
                .stream()
                .map(GoodsInfo::getType)
                .collect(Collectors.toList());

        List<Integer> templateGoodsTypes = JSON.parseObject(
                settlementInfo.getCouponAndTemplateInfoList().get(0)
                        .getTemplateSDK().getRule().getUsage().getGoodsType(),
                List.class
        );

        // return True if purchasing goods types fall in template allowed types.
        // NOTE: not need to be a subset, but only need to have some types in common is good enough.
        return CollectionUtils.isNotEmpty(
                CollectionUtils.intersection(goodsTypes, templateGoodsTypes)
        );
    }

    /**
     * <h2>handle settlement if types not match</h2>
     * @param settlementInfo original settlement info
     * @param totalPrice  original price
     * @return {@link SettlementInfo} final settlement info
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo settlementInfo, double totalPrice) {
        boolean isTypeMatch = isGoodsTypeSatisfy(settlementInfo);

        // if types not match, clean coupon and return original total price in the final settlement info.
        if(!isTypeMatch) {
            settlementInfo.setTotalPrice(totalPrice);
            settlementInfo.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlementInfo;
        }

        // if type match, this method does nothing (it is to handle unmatch cases)
        return null;
    }

    /**
     * <h2>calculate total price</h2>
     */
    protected double getTotalPrice(List<GoodsInfo> goodsInfoList) {
        return goodsInfoList.stream()
                .mapToDouble(info -> info.getPrice() * info.getCount())
                .sum();
    }

    /**
     * <h2>handle price scale</h2>
     */
    protected double keep2Decimals(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * <h2>minimum price</h2>
     * To avoid 0 or negative final price.
     */
    protected double minPrice() {
        return 0.1;
    }
}
