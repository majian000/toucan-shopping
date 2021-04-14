/*
 Navicat Premium Data Transfer

 Source Server         : rds
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
 Source Schema         : toucan_shopping_admin_auth

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 09/04/2021 18:39:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用编码',
  `enable_status` tinyint(4) NULL DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '应用表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin`;
CREATE TABLE `t_sa_admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建用户ID -1初始化',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_admin_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_app`;
CREATE TABLE `t_sa_admin_app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1027 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户应用关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_dept`;
CREATE TABLE `t_sa_admin_dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `dept_id` bigint(20) NOT NULL COMMENT '所属部门',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_role`;
CREATE TABLE `t_sa_admin_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属角色',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_department
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_department`;
CREATE TABLE `t_sa_department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `pid` bigint(20) NOT NULL COMMENT '上级部门 -1表示当前是顶级',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_department_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_department_app`;
CREATE TABLE `t_sa_department_app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint(20) NOT NULL COMMENT '所属角色',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门应用关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_function`;
CREATE TABLE `t_sa_function`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `function_id` varbinary(32) NULL DEFAULT NULL COMMENT '功能项ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '菜单类型 0目录 1菜单 2按钮',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单 -1表示当前是顶级',
  `icon` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `enable_status` tinyint(4) NULL DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) NULL DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role`;
CREATE TABLE `t_sa_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色ID',
  `enable_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '启用状态 0:禁用 1启用',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sa_role_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role_function`;
CREATE TABLE `t_sa_role_function`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属角色',
  `function_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属菜单',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
