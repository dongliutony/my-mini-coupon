package com.dongliu.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface IKafkaService {

    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> records);

}
