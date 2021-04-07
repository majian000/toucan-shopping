/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : toucan_shopping_admin_auth

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 07/04/2021 11:04:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用编码',
  `enable_status` tinyint(0) NULL DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_app
-- ----------------------------
INSERT INTO `t_app` VALUES (3, '用户应用权限中心', '10001001', 1, 1, '111', '2021-02-09 16:58:36.000000', 'bb155acbf5ef43dcac9aa892274fadd5', NULL, NULL);
INSERT INTO `t_app` VALUES (10, '用户中心', '10001002', 1, 0, '', '2021-02-12 14:34:25.000000', 'bb155acbf5ef43dcac9aa892274fadd5', NULL, NULL);
INSERT INTO `t_app` VALUES (11, '权限中心', '10001003', 1, 0, '', '2021-02-12 14:34:50.000000', 'bb155acbf5ef43dcac9aa892274fadd5', NULL, NULL);
INSERT INTO `t_app` VALUES (28, '11111111111111111111', '10001009', 1, 1, '123', '2021-04-04 23:41:52.000000', 'bb155acbf5ef43dcac9aa892274fadd5', NULL, NULL);
INSERT INTO `t_app` VALUES (29, 'black-bird-shopping', '11001010', 1, 1, '123', '2021-04-04 23:57:40.000000', 'bb155acbf5ef43dcac9aa892274fadd5', NULL, NULL);
INSERT INTO `t_app` VALUES (30, 'black-bird-shopping', '11001010', 1, 1, '123', '2021-04-04 23:58:41.000000', 'bb155acbf5ef43dcac9aa892274fadd5', NULL, NULL);

-- ----------------------------
-- Table structure for t_sa_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin`;
CREATE TABLE `t_sa_admin`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建用户ID -1初始化',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_admin
-- ----------------------------
INSERT INTO `t_sa_admin` VALUES (10, 'bb155acbf5ef43dcac9aa892274fadd5', 'sa', 'e10adc3949ba59abbe56e057f20f883e', 1, 0, NULL, NULL, '2021-02-04 16:33:16.000000', NULL, NULL);
INSERT INTO `t_sa_admin` VALUES (11, '44e9e4282d73452e96e1f53a15a5d675', 'sa', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, NULL, NULL, '2021-02-04 16:37:11.000000', NULL, NULL);

-- ----------------------------
-- Table structure for t_sa_admin_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_app`;
CREATE TABLE `t_sa_admin_app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1027 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户应用关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_admin_app
-- ----------------------------
INSERT INTO `t_sa_admin_app` VALUES (1003, 'bb155acbf5ef43dcac9aa892274fadd5', '10000001', 0, '2021-02-04 03:41:52.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES (1004, 'bb155acbf5ef43dcac9aa892274fadd5', '10000002', 0, '2021-02-04 03:41:52.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES (1005, 'd10590b318544049ba2104d1f5517a7d', '10000001', 0, '2021-02-04 03:47:09.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES (1006, 'd10590b318544049ba2104d1f5517a7d', '10000002', 0, '2021-02-04 03:47:09.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES (1010, 'bb155acbf5ef43dcac9aa892274fadd5', '10001002', 0, '2021-02-12 14:34:25.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1011, 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', 1, '2021-02-12 14:34:50.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1012, 'bb155acbf5ef43dcac9aa892274fadd5', '11001001', 0, '2021-02-13 17:35:01.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1013, 'bb155acbf5ef43dcac9aa892274fadd5', '11001002', 0, '2021-02-13 17:36:08.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1014, 'bb155acbf5ef43dcac9aa892274fadd5', '11001003', 0, '2021-02-13 17:37:09.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1015, 'bb155acbf5ef43dcac9aa892274fadd5', '11001004', 0, '2021-02-13 17:38:44.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1016, 'bb155acbf5ef43dcac9aa892274fadd5', '11001005', 0, '2021-02-13 17:38:58.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1017, 'bb155acbf5ef43dcac9aa892274fadd5', '11001006', 0, '2021-02-13 17:39:09.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1018, 'bb155acbf5ef43dcac9aa892274fadd5', '11001007', 0, '2021-02-13 17:39:17.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1019, 'bb155acbf5ef43dcac9aa892274fadd5', '11001008', 0, '2021-02-13 17:39:26.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1020, 'bb155acbf5ef43dcac9aa892274fadd5', '11001009', 0, '2021-02-13 17:39:35.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1021, 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', 1, '2021-02-13 17:39:43.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1022, 'bb155acbf5ef43dcac9aa892274fadd5', '11001011', 1, '2021-02-13 17:40:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1023, 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', 1, '2021-02-14 10:38:57.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1024, 'bb155acbf5ef43dcac9aa892274fadd5', '11001012', 1, '2021-02-14 10:39:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1025, 'bb155acbf5ef43dcac9aa892274fadd5', '11001013', 1, '2021-02-14 10:39:12.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES (1026, 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', 0, '2021-04-04 15:14:08.000000', 'bb155acbf5ef43dcac9aa892274fadd5');

-- ----------------------------
-- Table structure for t_sa_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_dept`;
CREATE TABLE `t_sa_admin_dept`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `dept_id` bigint(0) NOT NULL COMMENT '所属部门',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_admin_dept
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_role`;
CREATE TABLE `t_sa_admin_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `role_id` bigint(0) NOT NULL COMMENT '所属角色',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_admin_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_department
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_department`;
CREATE TABLE `t_sa_department`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `pid` bigint(0) NOT NULL COMMENT '上级部门 -1表示当前是顶级',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_department
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_department_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_department_app`;
CREATE TABLE `t_sa_department_app`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint(0) NOT NULL COMMENT '所属角色',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门应用关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_department_app
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_function`;
CREATE TABLE `t_sa_function`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接',
  `type` tinyint(0) NOT NULL DEFAULT 0 COMMENT '菜单类型 0目录 1菜单 2按钮',
  `pid` bigint(0) NOT NULL COMMENT '上级菜单 -1表示当前是顶级',
  `icon` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `enable_status` tinyint(0) NULL DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_function
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role`;
CREATE TABLE `t_sa_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `enable_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '启用状态 0:禁用 1启用',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_role_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role_function`;
CREATE TABLE `t_sa_role_function`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(0) NOT NULL COMMENT '所属角色',
  `menu_id` bigint(0) NOT NULL COMMENT '所属菜单',
  `delete_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sa_role_function
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
