package com.dongliu.coupon.service.impl;

import com.dongliu.coupon.entity.CouponTemplate;
import com.dongliu.coupon.exception.CouponException;
import com.dongliu.coupon.repository.CouponTemplateRepository;
import com.dongliu.coupon.service.ITemplateBaseService;
import com.dongliu.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateRepository templateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateRepository templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * <h2>get Template info by coupon template id</h2>
     */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {

        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()) {
            throw new CouponException("Template Is Not Exist: " + id);
        }

        return template.get();
    }

    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {

        List<CouponTemplate> templates =
                templateDao.findAllByAvailableAndExpired(
                        true, false);

        return templates.stream()
                .map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    /**
     * <h2>get template ids to CouponTemplateSDK mapping</h2>
     */
    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(
            Collection<Integer> ids) {

        List<CouponTemplate> templates = templateDao.findAllById(ids);

        return templates.stream().map(this::template2TemplateSDK)
                .collect(Collectors.toMap(
                        CouponTemplateSDK::getId, Function.identity()
                ));
    }

    /**
     * <h2>convert CouponTemplate to CouponTemplateSDK</h2>
     * */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template) {

        return new CouponTemplateSDK(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getDesc(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                template.getKey(),  // it is unfinished Template Key
                template.getTarget().getCode(),
                template.getRule()
        );
    }
}
