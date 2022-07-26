/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80018
Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
Source Database       : toucan_shopping_stock_0

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-13 21:05:41
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
-- Table structure for t_product_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_product_sku_stock`;
CREATE TABLE `t_product_sku_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存表主键',
  `product_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SPU(所属商品)',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sku_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SKU(所属商品)',
  `stock_num` bigint(20) DEFAULT '0' COMMENT '库存数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品SKU库存表';

-- ----------------------------
-- Records of t_product_sku_stock
-- ----------------------------
INSERT INTO `t_product_sku_stock` VALUES ('16', '6d60a39cf4cf4bccb1159fec61ba147b', 'dfb5a0e07dc811eb85a380a5893fe2d8', '63b10f067dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('17', '6d60a39cf4cf4bccb1159fec61ba147b', 'ea042d337dc811eb85a380a5893fe2d8', '65c2b8b77dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('18', '6d60a39cf4cf4bccb1159fec61ba147b', 'eb65be307dc811eb85a380a5893fe2d8', '66e93ba77dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('19', '6d60a39cf4cf4bccb1159fec61ba147b', 'ecec8af97dc811eb85a380a5893fe2d8', '6dbc4fcc7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('20', '337c8f9148294a488e7aad2cf733632a', 'ee5a9f457dc811eb85a380a5893fe2d8', '6fb4cbcf7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('21', '337c8f9148294a488e7aad2cf733632a', '008e9e217dc911eb85a380a5893fe2d8', '7173995a7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('22', '337c8f9148294a488e7aad2cf733632a', '016c090f7dc911eb85a380a5893fe2d8', '7357066e7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('23', '337c8f9148294a488e7aad2cf733632a', '025b006f7dc911eb85a380a5893fe2d8', '74b367387dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('24', '6d60a39cf4cf4bccb1159fec61ba147b', '03bb58e77dc911eb85a380a5893fe2d8', '7823949b7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('25', '6d60a39cf4cf4bccb1159fec61ba147b', '04853b8c7dc911eb85a380a5893fe2d8', '7a010e297dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('26', '6d60a39cf4cf4bccb1159fec61ba147b', '0554c3967dc911eb85a380a5893fe2d8', '7b5817af7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('27', '6d60a39cf4cf4bccb1159fec61ba147b', '0c12efdc7dc911eb85a380a5893fe2d8', '7c999d147dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('28', '6d60a39cf4cf4bccb1159fec61ba147b', 'bae4588a7dc811eb85a380a5893fe2d8', '7e6f27447dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
