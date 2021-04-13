/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80018
Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
Source Database       : toucan_shopping_order_0

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-13 21:05:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_event_process
-- ----------------------------
DROP TABLE IF EXISTS `t_event_process`;
CREATE TABLE `t_event_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息状态 0:待发送 1:已发送',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '消息内容',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务ID',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事务ID,UUID',
  `table_name` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务表名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14073 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='收消息记录表';

-- ----------------------------
-- Records of t_event_process
-- ----------------------------

-- ----------------------------
-- Table structure for t_event_publish
-- ----------------------------
DROP TABLE IF EXISTS `t_event_publish`;
CREATE TABLE `t_event_publish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息状态 0:待发送 1:已发送',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '消息内容',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务ID',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事务ID,UUID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13080 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='发消息表';

-- ----------------------------
-- Records of t_event_publish
-- ----------------------------

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '付款金额',
  `total_amount` decimal(10,2) NOT NULL COMMENT '商品最终金额(折扣算完)',
  `pay_status` smallint(6) NOT NULL COMMENT '支付状态 0未支付 1已支付 3线下支付已到账 4取消支付',
  `trade_status` smallint(6) NOT NULL COMMENT '交易状态 0进行中 1已完成 2已取消交易 3已结算',
  `pay_method` smallint(6) DEFAULT NULL COMMENT '支付方式 1线上支付 2线下支付',
  `pay_type` smallint(6) NOT NULL COMMENT '交易类型 0微信 1支付宝',
  `outer_trade_no` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '交易订单号(微信支付宝交易流水号)',
  `best_date` datetime DEFAULT NULL COMMENT '最佳送货时间',
  `pay_date` datetime DEFAULT NULL COMMENT '订单支付时间',
  `src_type` smallint(6) DEFAULT NULL COMMENT '用户购买渠道 1:pc  2:app',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单备注 买家填写',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单信息表';

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('16093', '22011615175364235000000000059818', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 03:49:26.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16094', '22011615175368686000000000082625', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 03:49:29.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16095', '22011615175426513000000000097192', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 03:50:28.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16096', '22011615179502383000000000072065', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 04:58:23.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16097', '22011615179504853000000000015988', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 04:58:25.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16098', '22011615559556940000000000048979', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:32:47.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16099', '22011615559644795000000000082736', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:34:05.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16100', '22011615559653361000000000051477', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:34:14.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16101', '22011615559656692000000000046201', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:34:17.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16102', '22011615565862391000000000000455', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 16:17:50.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16103', '22011615567626417000000000058849', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 16:47:14.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16104', '22011615567700628000000000081238', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 16:48:21.000000', '10001001', '1', '0');

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `order_id` bigint(20) NOT NULL COMMENT '订单主表ID',
  `sku_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品SKUID',
  `delivery_status` smallint(6) NOT NULL COMMENT '配送状态 0未收货 1送货中 2已收货',
  `seller_status` smallint(6) NOT NULL COMMENT '卖家备货状态 0备货中 1备货完成 2缺货',
  `buyer_status` smallint(6) NOT NULL COMMENT '买家状态 0待收货 1已收货 2换货 3退货',
  `product_num` bigint(20) NOT NULL COMMENT '购买商品数量',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `order_item_amount` decimal(10,2) NOT NULL COMMENT '订单单项总金额',
  `delivery_money` decimal(10,2) NOT NULL COMMENT '配送费用',
  `delivery_receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `delivery_finish_time` datetime DEFAULT NULL COMMENT '配送人员完成时间',
  `seller_finish_time` datetime DEFAULT NULL COMMENT '卖家完成时间',
  `buyer_finish_time` datetime DEFAULT NULL COMMENT '买家完成时间',
  `src_type` smallint(6) DEFAULT NULL COMMENT '用户购买渠道 1:pc  2:app',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单备注 买家填写',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '所属应用',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17510 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单项信息表';

-- ----------------------------
-- Records of t_order_item
-- ----------------------------
INSERT INTO `t_order_item` VALUES ('17486', '1', '22011615175364235000000000059818', '16093', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:26.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17487', '1', '22011615175364235000000000059818', '16093', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:26.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17488', '1', '22011615175368686000000000082625', '16094', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:29.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17489', '1', '22011615175368686000000000082625', '16094', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:29.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17490', '1', '22011615175426513000000000097192', '16095', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:50:28.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17491', '1', '22011615175426513000000000097192', '16095', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:50:28.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17492', '1', '22011615179502383000000000072065', '16096', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:23.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17493', '1', '22011615179502383000000000072065', '16096', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:23.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17494', '1', '22011615179504853000000000015988', '16097', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:25.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17495', '1', '22011615179504853000000000015988', '16097', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:25.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17496', '1', '22011615559556940000000000048979', '16098', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:32:48.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17497', '1', '22011615559556940000000000048979', '16098', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:32:48.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17498', '1', '22011615559644795000000000082736', '16099', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:05.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17499', '1', '22011615559644795000000000082736', '16099', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:05.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17500', '1', '22011615559653361000000000051477', '16100', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17501', '1', '22011615559653361000000000051477', '16100', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17502', '1', '22011615559656692000000000046201', '16101', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:17.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17503', '1', '22011615559656692000000000046201', '16101', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:17.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17504', '1', '22011615565862391000000000000455', '16102', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:17:51.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17505', '1', '22011615565862391000000000000455', '16102', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:17:51.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17506', '1', '22011615567626417000000000058849', '16103', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:47:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17507', '1', '22011615567626417000000000058849', '16103', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:47:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17508', '1', '22011615567700628000000000081238', '16104', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:48:21.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17509', '1', '22011615567700628000000000081238', '16104', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:48:21.000000', '10001001', '1', '0');

-- ----------------------------
-- Table structure for t_order_region
-- ----------------------------
DROP TABLE IF EXISTS `t_order_region`;
CREATE TABLE `t_order_region` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `region_code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地区编码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单区域信息表';

-- ----------------------------
-- Records of t_order_region
-- ----------------------------
