
-- create table coupon_template
CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon_template` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Auto increment Primary Key',
    `available` boolean NOT NULL DEFAULT false COMMENT 'if the template is valid for use',
    `expired` boolean NOT NULL DEFAULT false COMMENT 'if the template is expired',
    `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'coupon name',
    `logo` varchar(256) NOT NULL DEFAULT '' COMMENT 'coupon logo',
    `intro` varchar(256) NOT NULL DEFAULT '' COMMENT 'coupon description',
    `category` varchar(64) NOT NULL DEFAULT '' COMMENT 'coupon category',
    `product_line` int(11) NOT NULL DEFAULT '0' COMMENT 'product line for coupon issuer',
    `coupon_count` int(11) NOT NULL DEFAULT '0' COMMENT 'total number of coupons to be issued of this template',
    `create_time` datetime NOT NULL DEFAULT '0000-01-01 00:00:00' COMMENT 'Creation time',
    `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Creator user id',
    `template_key` varchar(128) NOT NULL DEFAULT '' COMMENT 'coupon template code',
    `target` int(11) NOT NULL DEFAULT '0' COMMENT 'target end users',
    `rule` varchar(1024) NOT NULL DEFAULT '' COMMENT 'Coupon usage rules: TemplateRule data in JSON format',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_user_id` (`user_id`),
    UNIQUE KEY `name` (`name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='Coupon template table';

-- clear the whole table
-- truncate coupon_template;
