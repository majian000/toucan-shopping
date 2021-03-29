/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : toucan_shopping_category

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 29/03/2021 18:21:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(0) NOT NULL COMMENT '父类别ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类别名称',
  `type` smallint(0) NULL DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `category_sort` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
  `show_status` tinyint(0) NULL DEFAULT 1 COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '点击类别跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 324 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品类别表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (324, -1, '家用电器', 1, 0, 1, 'www.baidu.com', '类别备注', '2021-03-29 16:46:12', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (325, 324, '电视', 1, 0, 1, 'www.163com', '类别备注', '2021-03-29 16:46:51', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (326, 325, '超薄电视', 1, 0, 1, 'www.taobao.com', '类别备注', '2021-03-29 16:47:12', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (327, -1, '手机;运营商;数码', 1, 0, 1, 'www.taobao.com;www.jd.com;www.163.com', NULL, '2021-03-29 17:01:52', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (328, 327, '手机通讯', 1, 0, 1, 'www.taobao.com', NULL, '2021-03-29 17:03:24', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (329, 328, '手机', 1, 0, 1, 'www.taobao.com', NULL, '2021-03-29 17:05:04', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_category_area
-- ----------------------------
DROP TABLE IF EXISTS `t_category_area`;
CREATE TABLE `t_category_area`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(0) NOT NULL COMMENT '类别主键',
  `area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区编码',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类别地区表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category_area
-- ----------------------------
INSERT INTO `t_category_area` VALUES (1, 324, '110000', '2021-03-29 17:15:22', NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (2, 325, '110000', '2021-03-29 17:16:24', NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
