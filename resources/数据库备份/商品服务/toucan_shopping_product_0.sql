/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80018
Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
Source Database       : toucan_shopping_product_0

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-13 21:05:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_attribute_key
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute_key`;
CREATE TABLE `t_attribute_key` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '所属类别',
  `attribute_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '属性名',
  `attribute_sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品属性名表';

-- ----------------------------
-- Records of t_attribute_key
-- ----------------------------
INSERT INTO `t_attribute_key` VALUES ('1', '4', '内存', '0', null, '0', '2021-01-04 15:06:37.000000', '0', null, null);
INSERT INTO `t_attribute_key` VALUES ('2', '4', '颜色', '1', null, '0', '2021-01-04 15:06:49.000000', '0', null, null);
INSERT INTO `t_attribute_key` VALUES ('3', '5', '内存', '0', null, '0', '2021-01-04 15:07:13.000000', '0', null, null);
INSERT INTO `t_attribute_key` VALUES ('4', '5', '颜色', '1', null, '0', '2021-01-04 15:07:26.000000', '0', null, null);

-- ----------------------------
-- Table structure for t_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute_value`;
CREATE TABLE `t_attribute_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `attribute_key_id` bigint(20) NOT NULL COMMENT '所属属性KEY',
  `attribute_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '属性名',
  `attribute_sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品属性值表';

-- ----------------------------
-- Records of t_attribute_value
-- ----------------------------
INSERT INTO `t_attribute_value` VALUES ('1', '1', '8G', '0', null, '0', '2021-01-04 15:08:35.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('2', '1', '12G', '1', null, '0', '2021-01-04 15:08:43.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('4', '3', '8G+128G', '1', null, '0', '2021-01-04 15:09:13.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('5', '3', '8G+256G', '2', null, '0', '2021-01-04 15:09:22.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('6', '2', '蓝色', '0', null, '0', '2021-01-04 15:09:40.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('7', '2', '黑色', '1', null, '0', '2021-01-04 15:09:52.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('8', '4', '亮黑色', '0', null, '0', '2021-01-04 15:10:08.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('9', '4', '8号色', '1', null, '0', '2021-01-04 15:10:24.000000', '0', null, null);

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
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SPU的UUID',
  `category_id` bigint(20) DEFAULT NULL COMMENT '所属类别',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '商品所有属性',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES ('1', '6d60a39cf4cf4bccb1159fec61ba147b', '4', '小米11', '{\"内存\": [\"8G\",\"12G\"],\"颜色\":[\"蓝色\",\"黑色\"]}', null, '0', '2021-01-04 15:23:39.000000', '0', null, null);
INSERT INTO `t_product` VALUES ('2', '337c8f9148294a488e7aad2cf733632a', '5', '华为 HUAWEI nova 8', '{\"内存\": [\"8G+128G\",\"8G+256G\"],\"颜色\":[\"亮黑色\",\"8号色\"]}', null, '0', '2021-01-04 15:48:06.000000', '0', null, null);

-- ----------------------------
-- Table structure for t_product_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_product_sku`;
CREATE TABLE `t_product_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU主键',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SKU的UUID',
  `product_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SPU(所属商品)',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '这个SKU的商品属性',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  `status` smallint(6) DEFAULT '0' COMMENT '是否上架 0:未上架 1:已上架',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品SKU表';

-- ----------------------------
-- Records of t_product_sku
-- ----------------------------
INSERT INTO `t_product_sku` VALUES ('1', '63b10f067dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '4299.00', '1', '8G 蓝色 小米11手机', '10001001', '0', '2021-01-04 15:42:33.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('2', '65c2b8b77dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"12G\"],\"颜色\":[\"蓝色\"]}', '4699.00', '1', '12G 蓝色 小米11手机', '10001001', '0', '2021-01-04 15:43:29.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('3', '66e93ba77dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"黑色\"]}', '4299.00', '1', '8G 黑色 小米11手机', '10001001', '0', '2021-01-04 15:44:06.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('4', '6dbc4fcc7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"12G\"],\"颜色\":[\"黑色\"]}', '4699.00', '1', '12G 黑色 小米11手机', '10001001', '0', '2021-01-04 15:44:33.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('5', '6fb4cbcf7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+128G\"],\"颜色\":[\"亮黑色\"]}', '3299.00', '1', '8G+128G 亮黑色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:51:23.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('6', '7173995a7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+128G\"],\"颜色\":[\"8号色\"]}', '3299.00', '1', '8G+128G 8号色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:52:13.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('7', '7357066e7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+256G\"],\"颜色\":[\"亮黑色\"]}', '3699.00', '1', '8G+256G 亮黑色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:53:05.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('8', '74b367387dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+256G\"],\"颜色\":[\"8号色\"]}', '3699.00', '1', '8G+256G 8号色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:53:52.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('20', '7823949b7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注', '10001001', '0', '2021-02-01 02:59:12.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('21', '7a010e297dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注', '10001001', '0', '2021-02-01 03:21:45.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('22', '7b5817af7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '华为手机备注', '10001001', '0', '2021-02-01 03:22:03.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('23', '7c999d147dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注 主库', '10001001', '0', '2021-02-02 08:18:42.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('24', '7e6f27447dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注 主库测试02', '10001001', '0', '2021-02-04 02:45:54.000000', '1', null, null);
