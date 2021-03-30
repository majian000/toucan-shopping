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

 Date: 30/03/2021 15:24:26
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
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '-1',
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
) ENGINE = InnoDB AUTO_INCREMENT = 330 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品类别表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (324, -1, '家用电器', '/static/images/nav9.png', 1, 0, 1, 'www.baidu.com', '类别备注', '2021-03-29 16:46:12', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (325, 324, '电视', '-1', 1, 0, 1, 'www.163com', '类别备注', '2021-03-29 16:46:51', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (326, 325, '超薄电视', '-1', 1, 0, 1, 'www.taobao.com', '类别备注', '2021-03-29 16:47:12', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (327, -1, '手机;运营商;数码', '/static/images/nav9.png', 1, 0, 1, 'www.taobao.com;www.jd.com;www.163.com', NULL, '2021-03-29 17:01:52', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (328, 327, '手机通讯', '-1', 1, 0, 1, 'www.taobao.com', NULL, '2021-03-29 17:03:24', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (329, 328, '手机', '-1', 1, 0, 1, 'www.taobao.com', NULL, '2021-03-29 17:05:04', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (330, -1, '电脑;办公', '/static/images/nav9.png', 1, 0, 1, 'www.163.com;www.163.com', NULL, '2021-03-30 14:11:04', NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (331, 330, '电脑整机', '-1', NULL, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category` VALUES (332, 331, '笔记本', '-1', NULL, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类别地区表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category_area
-- ----------------------------
INSERT INTO `t_category_area` VALUES (22, 324, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (23, 325, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (24, 326, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (25, 327, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (26, 328, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (27, 329, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (28, 330, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (29, 331, '110000', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_area` VALUES (30, 332, '110000', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_category_img
-- ----------------------------
DROP TABLE IF EXISTS `t_category_img`;
CREATE TABLE `t_category_img`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(0) NOT NULL COMMENT '类别ID',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `type` smallint(0) NULL DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `category_sort` bigint(0) UNSIGNED NULL DEFAULT 0 COMMENT '排序',
  `show_status` tinyint(0) NULL DEFAULT 1 COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '点击跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(0) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(0) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 332 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类别广告图片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category_img
-- ----------------------------
INSERT INTO `t_category_img` VALUES (330, 324, 'https://file06.16sucai.com/2016/0329/d5165ca7147d15d44cc17424c28c49e4.jpg', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_category_img` VALUES (331, 324, 'https://p0.ssl.qhimgs1.com/t01b8ad8aa5018be314.jpg', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
