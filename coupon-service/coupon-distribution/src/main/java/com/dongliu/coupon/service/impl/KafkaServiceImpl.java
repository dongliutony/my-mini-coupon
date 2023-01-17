package com.dongliu.coupon.service.impl;

import com.dongliu.coupon.service.IKafkaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaServiceImpl implements IKafkaService {
    @Override
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> records) {

    }
}
