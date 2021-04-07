/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : toucan_shopping_stock_0

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 07/04/2021 11:20:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_product_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_product_sku_stock`;
CREATE TABLE `t_product_sku_stock`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '库存表主键',
  `product_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SPU(所属商品)',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sku_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU(所属商品)',
  `stock_num` bigint(0) NULL DEFAULT 0 COMMENT '库存数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_user_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品SKU库存表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_product_sku_stock
-- ----------------------------
INSERT INTO `t_product_sku_stock` VALUES (16, '6d60a39cf4cf4bccb1159fec61ba147b', 'dfb5a0e07dc811eb85a380a5893fe2d8', '63b10f067dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (17, '6d60a39cf4cf4bccb1159fec61ba147b', 'ea042d337dc811eb85a380a5893fe2d8', '65c2b8b77dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (18, '6d60a39cf4cf4bccb1159fec61ba147b', 'eb65be307dc811eb85a380a5893fe2d8', '66e93ba77dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (19, '6d60a39cf4cf4bccb1159fec61ba147b', 'ecec8af97dc811eb85a380a5893fe2d8', '6dbc4fcc7dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (20, '337c8f9148294a488e7aad2cf733632a', 'ee5a9f457dc811eb85a380a5893fe2d8', '6fb4cbcf7dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (21, '337c8f9148294a488e7aad2cf733632a', '008e9e217dc911eb85a380a5893fe2d8', '7173995a7dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (22, '337c8f9148294a488e7aad2cf733632a', '016c090f7dc911eb85a380a5893fe2d8', '7357066e7dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (23, '337c8f9148294a488e7aad2cf733632a', '025b006f7dc911eb85a380a5893fe2d8', '74b367387dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (24, '6d60a39cf4cf4bccb1159fec61ba147b', '03bb58e77dc911eb85a380a5893fe2d8', '7823949b7dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (25, '6d60a39cf4cf4bccb1159fec61ba147b', '04853b8c7dc911eb85a380a5893fe2d8', '7a010e297dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (26, '6d60a39cf4cf4bccb1159fec61ba147b', '0554c3967dc911eb85a380a5893fe2d8', '7b5817af7dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (27, '6d60a39cf4cf4bccb1159fec61ba147b', '0c12efdc7dc911eb85a380a5893fe2d8', '7c999d147dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);
INSERT INTO `t_product_sku_stock` VALUES (28, '6d60a39cf4cf4bccb1159fec61ba147b', 'bae4588a7dc811eb85a380a5893fe2d8', '7e6f27447dbf11eb85a380a5893fe2d8', 9999, NULL, '10001001', 0, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
