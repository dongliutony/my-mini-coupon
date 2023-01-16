# Coupon Template service
Coupon template service provides Coupon Template and coupon's asset management to other modules that use coupon 
to fulfill a variety of operations, such as coupon applying, price evaluation, and coupon usage.


## business services

### 1. Coupon Templates Creation
Create coupon templates based on PM/Operation team's input information, such as name, category, product line, count, 
usage rules, etc. The usage rules could include rules like when can use, discount, combination with other coupons, etc.

### 2. Generate Coupons
It is not a good idea to generate a coupon triggered by user's applying request. That approach is not scalable, with 
high latency. We will use a centralized service to batch generate all the coupons at one time.
To generate a coupon, we need to generate a coupon code. The coupon code and its template id can represent a coupon.

### 3. Clear expired Coupon Templates
The coupon template service proactively clear expired templates periodically. 
And other modules will also do a expiration check when they call the coupon template service.


## data schema and cache design
* coupon_template table:
  * id
  * available
  * expired
  * name
  * logo
  * intro
  * category
  * product_line
  * count
  * created_at
  * created_by
  * template_key
  * target
  * rule


* cache-key
  * coupon_code_(template_id), value: List<CouponCode>
  * user_coupon_usable_(), value: {coupon_id: coupon_info}
  * user_coupon_used_(), value: {coupon_id: coupon_info}
  * user_coupon_expired_(), value: {coupon_id: coupon_info}


## code structure


## coding 

### async tasks
1. **why async tasks:**

    We create many coupon codes based on a Coupon Template in a batch. It is time costly. If use sync operation, the thread
    will be blocked for a while. That is not ideal. Using async tasks can increase service performance.

2. **how to add async task:**
    ```
    1. add /config/AsyncPoolConfig implements AsyncConfigurer -> configure custom async task thread pool
    2. add /service/AsyncCouponCodeCreateImpl asyncService ->
    3. call asyncService in /IBuildTemplateService -> 
    ```

### scheduled tasks
1. **why scheduled tasks:**

   We create many coupon codes based on a Coupon Template in a batch. It is time costly. If use sync operation, the thread
   will be blocked for a while. That is not ideal. Using async tasks can increase service performance.

2. **how to add scheduled task:**

    Enable at the entry class Application `@Enable Scheduling`.
    Create a @Component class, add scheduled task as a method with `@Scheduled(fixedRate = 60 * 60 * 1000)`.

## how to test
1. run `test/.../BuildTemplateTest` to insert a couponTemplate into db and 10000 coupon code into redis
2. check db `select * from coupon_template`. Using `truncate coupon_template` if we need to manually redo the test
3. check redis keys `keys *`; check key type `type coupon_template_code_1`; 
   check values `lrange coupon_template_code_1 0 -1`. Using `flushall` if we need to redo tests and clean redis.
