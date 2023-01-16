# Coupon Distribution service
Coupon Distribution service provides searching, applying, and settlement features to end users.


## business services

### 1. Search an end user's Coupon records.
Find all of a user's coupon records based on userId and coupon status.

### 2. Search CouponTemplate the user can apply
Find out all CouponTemplate that a user can apply to get coupons based on userId.

### 3. A user apply coupons
A user apply coupons that she or he can apply.

### 4. Evaluate or use a coupon with settlement
Settle a coupon by calculating the final total price when using coupons.


## data schema and cache design

* coupon table
  * id
  * template_id
  * user_id
  * coupon_code
  * assign_at
  * status


* cache-key


## code structure


## coding


## how to test

