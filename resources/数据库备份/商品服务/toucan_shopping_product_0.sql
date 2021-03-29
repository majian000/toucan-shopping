/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : toucan_shopping_product_0

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 29/03/2021 16:25:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_attribute_key
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute_key`;
CREATE TABLE `t_attribute_key`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(0) NOT NULL COMMENT '所属类别',
  `attribute_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名',
  `attribute_sort` bigint(0) NULL DEFAULT NULL COMMENT '排序',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `create_user_id` bigint(0) NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性名表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_attribute_key
-- ----------------------------
INSERT INTO `t_attribute_key` VALUES (1, 4, '内存', 0, '2021-01-04 15:06:37', NULL, 0, 0);
INSERT INTO `t_attribute_key` VALUES (2, 4, '颜色', 1, '2021-01-04 15:06:49', NULL, 0, 0);
INSERT INTO `t_attribute_key` VALUES (3, 5, '内存', 0, '2021-01-04 15:07:13', NULL, 0, 0);
INSERT INTO `t_attribute_key` VALUES (4, 5, '颜色', 1, '2021-01-04 15:07:26', NULL, 0, 0);

-- ----------------------------
-- Table structure for t_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute_value`;
CREATE TABLE `t_attribute_value`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `attribute_key_id` bigint(0) NOT NULL COMMENT '所属属性KEY',
  `attribute_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名',
  `attribute_sort` bigint(0) NULL DEFAULT NULL COMMENT '排序',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `create_user_id` bigint(0) NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性值表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_attribute_value
-- ----------------------------
INSERT INTO `t_attribute_value` VALUES (1, 1, '8G', 0, '2021-01-04 15:08:35', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (2, 1, '12G', 1, '2021-01-04 15:08:43', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (4, 3, '8G+128G', 1, '2021-01-04 15:09:13', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (5, 3, '8G+256G', 2, '2021-01-04 15:09:22', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (6, 2, '蓝色', 0, '2021-01-04 15:09:40', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (7, 2, '黑色', 1, '2021-01-04 15:09:52', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (8, 4, '亮黑色', 0, '2021-01-04 15:10:08', NULL, 0, 0);
INSERT INTO `t_attribute_value` VALUES (9, 4, '8号色', 1, '2021-01-04 15:10:24', NULL, 0, 0);

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SPU的UUID',
  `category_id` bigint(0) NULL DEFAULT NULL COMMENT '所属类别',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品所有属性',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `create_user_id` bigint(0) NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, '6d60a39cf4cf4bccb1159fec61ba147b', 4, '小米11', '{\"内存\": [\"8G\",\"12G\"],\"颜色\":[\"蓝色\",\"黑色\"]}', '2021-01-04 15:23:39', NULL, 0, 0);
INSERT INTO `t_product` VALUES (2, '337c8f9148294a488e7aad2cf733632a', 5, '华为 HUAWEI nova 8', '{\"内存\": [\"8G+128G\",\"8G+256G\"],\"颜色\":[\"亮黑色\",\"8号色\"]}', '2021-01-04 15:48:06', NULL, 0, 0);

-- ----------------------------
-- Table structure for t_product_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_product_sku`;
CREATE TABLE `t_product_sku`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'SKU主键',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU的UUID',
  `product_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SPU(所属商品)',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '这个SKU的商品属性',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `status` smallint(0) NULL DEFAULT 0 COMMENT '是否上架 0:未上架 1:已上架',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `create_user_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SKU表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_product_sku
-- ----------------------------
INSERT INTO `t_product_sku` VALUES (1, '63b10f067dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', 4299.00, 1, '8G 蓝色 小米11手机', '2021-01-04 15:42:33', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (2, '65c2b8b77dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"12G\"],\"颜色\":[\"蓝色\"]}', 4699.00, 1, '12G 蓝色 小米11手机', '2021-01-04 15:43:29', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (3, '66e93ba77dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"黑色\"]}', 4299.00, 1, '8G 黑色 小米11手机', '2021-01-04 15:44:06', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (4, '6dbc4fcc7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"12G\"],\"颜色\":[\"黑色\"]}', 4699.00, 1, '12G 黑色 小米11手机', '2021-01-04 15:44:33', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (5, '6fb4cbcf7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+128G\"],\"颜色\":[\"亮黑色\"]}', 3299.00, 1, '8G+128G 亮黑色 华为 NOVA8手机', '2021-01-04 15:51:23', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (6, '7173995a7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+128G\"],\"颜色\":[\"8号色\"]}', 3299.00, 1, '8G+128G 8号色 华为 NOVA8手机', '2021-01-04 15:52:13', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (7, '7357066e7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+256G\"],\"颜色\":[\"亮黑色\"]}', 3699.00, 1, '8G+256G 亮黑色 华为 NOVA8手机', '2021-01-04 15:53:05', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (8, '74b367387dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+256G\"],\"颜色\":[\"8号色\"]}', 3699.00, 1, '8G+256G 8号色 华为 NOVA8手机', '2021-01-04 15:53:52', '10001001', 0, 0);
INSERT INTO `t_product_sku` VALUES (20, '7823949b7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', 999.99, 1, '小米手机备注', '2021-02-01 02:59:12', '10001001', 1, 0);
INSERT INTO `t_product_sku` VALUES (21, '7a010e297dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', 999.99, 1, '小米手机备注', '2021-02-01 03:21:45', '10001001', 1, 0);
INSERT INTO `t_product_sku` VALUES (22, '7b5817af7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', 999.99, 1, '华为手机备注', '2021-02-01 03:22:03', '10001001', 1, 0);
INSERT INTO `t_product_sku` VALUES (23, '7c999d147dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', 999.99, 1, '小米手机备注 主库', '2021-02-02 08:18:42', '10001001', 1, 0);
INSERT INTO `t_product_sku` VALUES (24, '7e6f27447dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', 999.99, 1, '小米手机备注 主库测试02', '2021-02-04 02:45:54', '10001001', 1, 0);

SET FOREIGN_KEY_CHECKS = 1;
