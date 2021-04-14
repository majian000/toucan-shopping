/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : toucan_shopping_column

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 07/04/2021 11:15:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_column
-- ----------------------------
DROP TABLE IF EXISTS `t_column`;
CREATE TABLE `t_column`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `type` smallint(0) NULL DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `show_status` tinyint(0) NULL DEFAULT NULL COMMENT '显示状态 0隐藏 1显示',
  `position` smallint(0) NULL DEFAULT NULL COMMENT '栏目位置 1首页',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_area
-- ----------------------------
DROP TABLE IF EXISTS `t_column_area`;
CREATE TABLE `t_column_area`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column_id` bigint(0) NOT NULL COMMENT '栏目主键',
  `area_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区编码',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目地区表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column_area
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_column_banner`;
CREATE TABLE `t_column_banner`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `banner_id` bigint(0) NOT NULL COMMENT '轮播图主键',
  `column_id` bigint(0) NOT NULL COMMENT '栏目主键',
  `position` smallint(0) NULL DEFAULT NULL COMMENT '位置 1:栏目左侧',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目轮播图关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column_banner
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_img
-- ----------------------------
DROP TABLE IF EXISTS `t_column_img`;
CREATE TABLE `t_column_img`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column_id` bigint(0) NOT NULL COMMENT '栏目ID',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `type` smallint(0) NULL DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `img_sort` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
  `show_status` tinyint(0) NULL DEFAULT 1 COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '点击跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column_img
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_sku_category
-- ----------------------------
DROP TABLE IF EXISTS `t_column_sku_category`;
CREATE TABLE `t_column_sku_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类别名称',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '点击类别跳转连接',
  `column_id` bigint(0) NOT NULL COMMENT '栏目主键',
  `position` smallint(0) NULL DEFAULT NULL COMMENT '位置 1:栏目顶部 2:栏目左侧',
  `category_sort` bigint(0) NULL DEFAULT NULL COMMENT '类别排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目推荐商品类别表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column_sku_category
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
