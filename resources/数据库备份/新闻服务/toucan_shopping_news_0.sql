/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : toucan_shopping_news_0

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 29/03/2021 14:18:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_news_0
-- ----------------------------
DROP TABLE IF EXISTS `t_news_0`;
CREATE TABLE `t_news_0`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容路径,如/content/news/new1.html',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `show_date` datetime(0) NULL DEFAULT NULL COMMENT '开始显示时间',
  `news_sort` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_news_0
-- ----------------------------

-- ----------------------------
-- Table structure for t_news_1
-- ----------------------------
DROP TABLE IF EXISTS `t_news_1`;
CREATE TABLE `t_news_1`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容路径,如/content/news/new1.html',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `show_date` datetime(0) NULL DEFAULT NULL COMMENT '开始显示时间',
  `news_sort` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '新闻表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_news_1
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
