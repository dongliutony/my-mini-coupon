package com.dongliu.coupon.service.impl;

import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.repository.CouponTemplateRepository;
import com.dongliu.coupon.service.IBuildTemplateService;
import com.dongliu.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {
    private CouponTemplateRepository templateRepository;

    @Autowired
    public BuildTemplateServiceImpl(CouponTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws CouponException {
        // check template request params are valid
        if (!request.validate()) {
            throw new CouponException("BuildTemplate params are not valid.");
        }

        // Check if a template with the name already exists.
        if (null != templateRepository.findByName(request.getName())) {
            throw new CouponException("The coupon template exists. Try to use another name.");
        }

        // Build CouponTemplate and save it to db
        CouponTemplate template = requestToTemplate(request);
        templateRepository.save(template);

        // TODO: async generate coupon codes. It is a time costly task.

        return template;
    }


    /**
     * <h2>convert TemplateRequest to CouponTemplate</h2>
     * from VO to entity obj
     */
    private CouponTemplate requestToTemplate(TemplateRequest request) {

        return new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getDesc(),
                request.getCategory(),
                request.getProductLine(),
                request.getCount(),
                request.getUserId(),
                request.getTarget(),
                request.getRule()
        );
    }
}
