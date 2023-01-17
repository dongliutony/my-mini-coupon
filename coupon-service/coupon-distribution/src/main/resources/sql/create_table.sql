CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto increment Primary Key',
    `template_id` int(11) NOT NULL DEFAULT '0' COMMENT 'CouponTemplate ID',
    `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'End user ID to apply coupons',
    `coupon_code` varchar(64) NOT NULL DEFAULT '' COMMENT 'Coupon Code',
    `assign_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT 'Issued time',
    `status` int(11) NOT NULL DEFAULT '0' COMMENT 'Coupon Status',
    PRIMARY KEY (`id`),
    KEY `idx_template_id` (`template_id`),
    KEY `idx_user_id` (`user_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='Coupon(issue record)';
