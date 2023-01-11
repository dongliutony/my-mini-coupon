# my-mini-coupon

This is a Spring Cloud microservice project for coupon management.

## environment

* java 8
* Spring Cloud Greenwich
* MySQL 8.0.32
* Redis 6.0
* Kafka 2.2.2

## Spring Cloud modules

1. coupon-eureka
2. coupon-gateway

## business services

1. coupon-common
: for shared code base, configuration, response, and exception handling across the project

2. coupon-template
3. coupon-distribution
4. coupon-settlement
5. coupon-permission

## how to set up environment

1. boot up MySQL, and create db and tables
2. boot up Redis: redis-server
3. boot up Kafka: 
: run zookeeper first `/kafka_directory/bin/zookeeper-server-start.sh -daemon config/zookeeper.properties`
: use `jpa` and check if zookeeper Java ps is ready
: run kafka when zookeeper is ready `nohup /kafka_directory/bin/kafka-server-start.sh config/server.properties &`


## how to run microservices


1. install coupon-common: it needs to be installed to local .m2 directory so that other modules can use it
: navigate to coupon-common root direct and run `mvn clean install` to install it into local .m2.

2. start mysql, redis, and kafka as basic services
3. package coupon-eureka and run the eureka.jar
4. package all the business microservices and run jar files respectively

