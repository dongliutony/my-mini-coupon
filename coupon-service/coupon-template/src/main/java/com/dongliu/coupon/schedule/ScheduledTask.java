package com.dongliu.coupon.schedule;

import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.repository.CouponTemplateRepository;
import com.dongliu.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ScheduledTask {

    private final CouponTemplateRepository templateRepository;

    @Autowired
    public ScheduledTask(CouponTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * <h2>Scheduled task: offline expired template</h2>
     * Periodically invoked in a separated thread to check if CouponTemplates expired. If one template is expired,
     * modify its expired to "true" in db.
     *
     * Be noted: we don't periodically check if an issued Coupon is expired until users attempt to use it. Coupon in
     * Redis will be kept forever.
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void offlineExpiredCouponTemplate() {
        log.info("Start to offline expired Coupon Template...");

        // check previously unexpired templates. If a template is marked as "expired" in db, it was offline before.
        List<CouponTemplate> templates = templateRepository.findAllByExpired(false);
        if(CollectionUtils.isEmpty(templates)) {
            log.info("Done to offline expired CouponTemplate");
            return;
        }

        Date curTime = new Date();
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());
        templates.forEach(t -> {
            TemplateRule rule = t.getRule();
            if (rule.getExpiration().getDeadline() < curTime.getTime()) {
                t.setExpired(true);
                expiredTemplates.add(t);
            }
        });

        if (CollectionUtils.isNotEmpty(expiredTemplates)) {
            log.info("Expired CouponTemplate number: {}", templateRepository.saveAll(expiredTemplates));
        }

        log.info("Done to offline expired CouponTemplate");
    }
}
