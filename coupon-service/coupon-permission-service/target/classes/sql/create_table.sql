
CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon_path` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'path ID, Primary Key, auto increment',
    `path_pattern` varchar(200) NOT NULL DEFAULT '' COMMENT 'path patter, URI',
    `http_method` varchar(20) NOT NULL DEFAULT '' COMMENT 'http Method',
    `path_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'path description',
    `service_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'micro service name',
    `op_mode` varchar(20) NOT NULL DEFAULT '' COMMENT 'operation mode, READ/WRITE',
    PRIMARY KEY (`id`),
    KEY `idx_path_pattern` (`path_pattern`),
    KEY `idx_servivce_name` (`service_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='path table';

CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon_role` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'role ID, Primary Key, auto increment',
    `role_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'role name',
    `role_tag` varchar(128) NOT NULL DEFAULT '' COMMENT 'role TAG',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='role table';

CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon_role_path_mapping` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID, Primary Key, auto increment',
    `role_id` int(11) NOT NULL DEFAULT '0' COMMENT 'role ID',
    `path_id` int(11) NOT NULL DEFAULT '0' COMMENT 'path ID',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_path_id` (`path_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='role & path mapping table';

CREATE TABLE IF NOT EXISTS `coupon_data`.`coupon_user_role_mapping` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID, Primary Key, auto increment',
    `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'user ID',
    `role_id` int(11) NOT NULL DEFAULT '0' COMMENT 'role ID',
    PRIMARY KEY (`id`),
    KEY `key_role_id` (`role_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='user & role mapping table';
