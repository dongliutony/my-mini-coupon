# my-mini-coupon

This is a Spring Cloud microservice project for coupon management.

Test using Jetbrains Remote Development...

## 1. environment

Below are the workable versions, and higher versions may need to do some double check to make things work together.

* java 8
* Spring Cloud Greenwich.SR6
* SpringBoot 2.1.10.RELEASE
* MySQL 8.0.32
* Redis 6.0
* Kafka 2.2.2

## 2. Spring Cloud modules

* **coupon-eureka:**
* **coupon-gateway:**

## 3. business services

   1. #### coupon-common
      for shared code base, configuration, response, and exception handling across the project

   2. #### coupon-template
       1. **build coupon-template:**
       2. **generate coupon-code:**
       3. **clean expired coupon-template:**

   3. #### coupon-distribution
       1. **fetch a user's coupon records:**
       2. **fetch a user's applicable coupons:**
       3. **user apply coupons:**
       4. **pre-calculate/use a coupon:**

   4. #### coupon-settlement
       1. **calculate with coupons:**

   5. #### coupon-permission
      1. **path cration:**
      
         Add other microservice controller's URL/path into permission module. It provides APIs to offload
      such management from developers to PM or operation teams.
         ```
         fas 
         ```
         
      2. **permission validation:**

## 4. how to set up environment

   1. **boot up MySQL**, and create db and tables
   2. **boot up Redis:** `redis-server` on a server, or run it in a docker container
   3. **boot up Kafka:** run it on a server
       - run zookeeper first `/kafka_directory/bin/zookeeper-server-start.sh -daemon config/zookeeper.properties`
       - use `jpa` and check if zookeeper Java ps is ready
       - run kafka when zookeeper is ready `nohup /kafka_directory/bin/kafka-server-start.sh config/server.properties &`
   4. **boot up Eureka:** For Eureka server cluster deployment on one server node, run
      `mvn clean package -Dmaven.test.skip=true -U` to build jar, add items with format `127.0.0.1 eureka-server1`
      in the /etc/hosts file (make sure that a microservice's application.yml has configuration like eureka:
      client: service-url: defaultZone: http://eureka-server1:8001/eureka/). 
      Then run `java -jar coupon-eureka-1.0-SNAPSHOP.jar --spring.profiles.active=eureka-server1` to
      boot up 1st node, then boot up the rest.
   5. **boot up Zuul:** 

## 5. how to run business service modules

### 1) run modules separately
1. **install coupon-common:** it needs to be installed to local .m2 directory so that other modules can use it
          - navigate to coupon-common root direct and run `mvn clean install` to install it into local .m2.
2. **start basic services:** mysql, redis, and kafka
3. **package spring cloud services:** run `mvn clean build -Dspring.active.profile=prod --skip-test=true` for
coupon-eureka, coupon-gateway and run the jar files with `java -jar xx.jar`.
4. **package business modules:** package all the business microservices and run jar files respectively

### 2) run modules with docker composer



## 6. how to develop a module

   ### 1) think about the business logic and feature requirements

   ### 2) design data structure, database schema, and cache KV

   ### 3) design code architecture

   ### 4) start to code


## 7. key techniques 

- ### Spring Cloud architecture

   -  

- ### SpringBoot & Starter

- ### universal Response Advice & Exception Handler

- ### JPA, Field converter, & object serialization
  - JPA is the standard for ORM, while Hibernate is an implementation. We write JPA interface and let
  AOP do the magic. 
  ```
  // define entity and PrimaryKey type, JPA will do the match and query.
  public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {
         
       CouponTemplate findByName(String name);
    
       // params math method names respectivelv. e.g ByAvailable -> available, Expired -> expired
       List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);
    
       List<CouponTemplate> findAllByExpired(Boolean expired);
  }
  ```
  - Entity's partArgsConstructor: we normally define ID as a PrimaryKey and created by db, not by entity constructor.

- ### Feign, inter modules communication, SDK, and VO

- ### Async tasks

- ### Scheduling tasks

- ### Redis

- ### Kafka

- ### Bean Post Processor (BPP)

- ### Spring eventListener

- ### integration test environment for Spring Cloud project

- ### some pitfalls

  1. All packages in different module should have the same name. Otherwise, Spring cannot scan and manage the beans in a
    different package than your working package.
  2. Version compatible: SpringCloud vs SpringBoot version matrix. MySQL and connector version

## 8. plans
   - [ ] enhance gateway with more filters: check by url to do validation, query result cache, etc
   - [ ] custom Kafka pipeline: directly send KafkaMessage into MQ without manually serialize/deserialize ops.
   - [ ] enhance validation
   - [ ] request tracing
   - [ ] in-memory db, redis, kafka for Unittest
   - [ ] distributed logging ELK
   - [ ] monitor module
   - [ ] docker composer configuration
   - [ ] front end
