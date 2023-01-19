INSERT INTO `coupon_role` VALUES (1, 'admin', 'ADMIN');
INSERT INTO `coupon_role` VALUES (2, 'super admin', 'SUPER_ADMIN');
INSERT INTO `coupon_role` VALUES (3, 'customer', 'CUSTOMER');

INSERT INTO `coupon_role_path_mapping` VALUES (1, 1, 1);
INSERT INTO `coupon_role_path_mapping` VALUES (2, 1, 2);
INSERT INTO `coupon_role_path_mapping` VALUES (3, 3, 2);

INSERT INTO `coupon_user_role_mapping` VALUES (1, 15, 1);
INSERT INTO `coupon_user_role_mapping` VALUES (2, 16, 3);
