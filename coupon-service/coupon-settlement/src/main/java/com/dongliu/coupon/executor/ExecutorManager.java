package com.dongliu.coupon.executor;

import com.dongliu.coupon.constant.CouponCategory;
import com.dongliu.coupon.constant.RuleFlag;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.management.ManagementPermission;
import java.util.*;

/**
 * <h1>BPP: Rule executor manager</h1>
 * Find a proper RuleExecutor based on request SettlementInfo
 * It will be called after bean creation and before/after bean initialization
 */
@Slf4j
@Component
public class ExecutorManager implements BeanPostProcessor {

    private static Map<RuleFlag, RuleExecutor> executorIndex = new HashMap<>(RuleFlag.values().length);

    /**
     * <h2>Coupon executor calculation entry point</h2>
     * NOTE: this is a normal method to be called by a request. It is NOT a BPP method.
     */
    public SettlementInfo computeWithRule(SettlementInfo settlement) throws CouponException {
        if (CollectionUtils.isEmpty(settlement.getCouponAndTemplateInfoList())) {
            throw new CouponException("Must include at least 1 coupon");
        }
        SettlementInfo result = null;
        // for single coupon
        if (settlement.getCouponAndTemplateInfoList().size() == 1) {
            // get Coupon category
            CouponCategory category = CouponCategory.of(
                    settlement.getCouponAndTemplateInfoList().get(0).getTemplateSDK().getCategory());
            switch(category) {
                case THRESHOLD:
                    result = executorIndex.get(RuleFlag.THRESHOLD).computeWithRules(settlement);
                    break;
                case DISCOUNT:
                    result = executorIndex.get(RuleFlag.DISCOUNT).computeWithRules(settlement);
                    break;
                case INSTANT:
                    result = executorIndex.get(RuleFlag.INSTANT).computeWithRules(settlement);
                    break;
            }
        } else { // for combined coupons
            List<CouponCategory> categories = new ArrayList<>(settlement.getCouponAndTemplateInfoList().size());
            settlement.getCouponAndTemplateInfoList().forEach(ct ->
                    categories.add(CouponCategory.of(ct.getTemplateSDK().getCategory())));
            if (categories.size() != 2) {
                throw new CouponException("Not support using 3 coupons together at this time");
            } else {
                if(categories.contains(CouponCategory.THRESHOLD) && categories.contains(CouponCategory.DISCOUNT)) {
                    result = executorIndex.get(RuleFlag.THRESHOLD_DISCOUNT).computeWithRules(settlement);
                } else {
                    throw new CouponException("Not support combinations other than Threshold + Discount");
                }
            }
        }
        return result;
    }

    /**
     * <h2>Register rule when a Executor bean is created by Spring</h2>
     * This method will be only called before the initialization of an RuleExecutor class.
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof RuleExecutor)) {
            return bean;
        }

        RuleExecutor executor = (RuleExecutor) bean;
        RuleFlag ruleFlag = executor.ruleConfig();

        // ERROR: the same class are registered more than once.
        if (executorIndex.containsKey(ruleFlag)) {
            throw new IllegalStateException("There is already an existing rule for rule flag " + ruleFlag);
        }

        log.info("Load Executor {} for rule flag {}", executor.getClass(), ruleFlag);
        executorIndex.put(ruleFlag, executor);

        return null;
    }

    /**
     * <h2>No rule ops after bean initialization</h2>
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
