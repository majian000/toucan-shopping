/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : black_bird_shopping_user_center

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-03-15 21:26:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bbs_app
-- ----------------------------
DROP TABLE IF EXISTS `bbs_app`;
CREATE TABLE `bbs_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用编码',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `enable_status` tinyint DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='应用表';

-- ----------------------------
-- Records of bbs_app
-- ----------------------------
INSERT INTO `bbs_app` VALUES ('3', '用户应用权限中心', '10001001', '2021-02-09 16:58:36', '0', '111', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('10', '订单中心', '10001002', '2021-02-12 14:34:25', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('11', '商品中心', '10001003', '2021-02-12 14:34:50', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('12', 'toucan-shopping-001', '11001001', '2021-02-13 17:35:01', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('13', '11001001', '11001002', '2021-02-13 17:36:08', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('14', '11001003', '11001003', '2021-02-13 17:37:09', '0', '11001003', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('15', '11001004', '11001004', '2021-02-13 17:38:43', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('16', '11001005', '11001005', '2021-02-13 17:38:58', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('17', '11001006', '11001006', '2021-02-13 17:39:09', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('18', '11001007', '11001007', '2021-02-13 17:39:16', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('19', '11001008', '11001008', '2021-02-13 17:39:26', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('20', '11001009', '11001009', '2021-02-13 17:39:35', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('21', '11001010', '11001010', '2021-02-13 17:39:43', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('22', '11001011', '11001011', '2021-02-13 17:40:06', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('23', '11001010', '11001010', '2021-02-14 10:38:57', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('24', '11001012', '11001012', '2021-02-14 10:39:06', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');
INSERT INTO `bbs_app` VALUES ('25', '11001013', '11001013', '2021-02-14 10:39:12', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '1');

-- ----------------------------
-- Table structure for bbs_sa_admin
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_admin`;
CREATE TABLE `bbs_sa_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建用户ID -1初始化',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户主表';

-- ----------------------------
-- Records of bbs_sa_admin
-- ----------------------------
INSERT INTO `bbs_sa_admin` VALUES ('10', 'sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '2021-02-04 16:33:16', '0', null, null, 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin` VALUES ('11', 'sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '2021-02-04 16:37:11', '1', null, null, '44e9e4282d73452e96e1f53a15a5d675');

-- ----------------------------
-- Table structure for bbs_sa_admin_app
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_admin_app`;
CREATE TABLE `bbs_sa_admin_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1026 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of bbs_sa_admin_app
-- ----------------------------
INSERT INTO `bbs_sa_admin_app` VALUES ('1003', 'bb155acbf5ef43dcac9aa892274fadd5', '10000001', '2021-02-04 03:41:52', '0', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `bbs_sa_admin_app` VALUES ('1004', 'bb155acbf5ef43dcac9aa892274fadd5', '10000002', '2021-02-04 03:41:52', '0', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `bbs_sa_admin_app` VALUES ('1005', 'd10590b318544049ba2104d1f5517a7d', '10000001', '2021-02-04 03:47:09', '0', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `bbs_sa_admin_app` VALUES ('1006', 'd10590b318544049ba2104d1f5517a7d', '10000002', '2021-02-04 03:47:09', '0', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `bbs_sa_admin_app` VALUES ('1010', 'bb155acbf5ef43dcac9aa892274fadd5', '10001002', '2021-02-12 14:34:25', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1011', 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', '2021-02-12 14:34:50', '1', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1012', 'bb155acbf5ef43dcac9aa892274fadd5', '11001001', '2021-02-13 17:35:01', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1013', 'bb155acbf5ef43dcac9aa892274fadd5', '11001002', '2021-02-13 17:36:08', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1014', 'bb155acbf5ef43dcac9aa892274fadd5', '11001003', '2021-02-13 17:37:09', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1015', 'bb155acbf5ef43dcac9aa892274fadd5', '11001004', '2021-02-13 17:38:44', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1016', 'bb155acbf5ef43dcac9aa892274fadd5', '11001005', '2021-02-13 17:38:58', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1017', 'bb155acbf5ef43dcac9aa892274fadd5', '11001006', '2021-02-13 17:39:09', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1018', 'bb155acbf5ef43dcac9aa892274fadd5', '11001007', '2021-02-13 17:39:17', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1019', 'bb155acbf5ef43dcac9aa892274fadd5', '11001008', '2021-02-13 17:39:26', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1020', 'bb155acbf5ef43dcac9aa892274fadd5', '11001009', '2021-02-13 17:39:35', '0', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1021', 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', '2021-02-13 17:39:43', '1', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1022', 'bb155acbf5ef43dcac9aa892274fadd5', '11001011', '2021-02-13 17:40:06', '1', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1023', 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', '2021-02-14 10:38:57', '1', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1024', 'bb155acbf5ef43dcac9aa892274fadd5', '11001012', '2021-02-14 10:39:06', '1', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `bbs_sa_admin_app` VALUES ('1025', 'bb155acbf5ef43dcac9aa892274fadd5', '11001013', '2021-02-14 10:39:12', '1', 'bb155acbf5ef43dcac9aa892274fadd5');

-- ----------------------------
-- Table structure for bbs_sa_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_admin_dept`;
CREATE TABLE `bbs_sa_admin_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `dept_id` bigint NOT NULL COMMENT '所属部门',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员部门关联表';

-- ----------------------------
-- Records of bbs_sa_admin_dept
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sa_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_admin_role`;
CREATE TABLE `bbs_sa_admin_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `role_id` bigint NOT NULL COMMENT '所属角色',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员角色关联表';

-- ----------------------------
-- Records of bbs_sa_admin_role
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sa_department
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_department`;
CREATE TABLE `bbs_sa_department` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `pid` bigint NOT NULL COMMENT '上级部门 -1表示当前是顶级',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of bbs_sa_department
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sa_department_app
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_department_app`;
CREATE TABLE `bbs_sa_department_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint NOT NULL COMMENT '所属角色',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门应用关联表';

-- ----------------------------
-- Records of bbs_sa_department_app
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sa_menu
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_menu`;
CREATE TABLE `bbs_sa_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '菜单类型 0目录 1菜单 2按钮',
  `pid` bigint NOT NULL COMMENT '上级菜单 -1表示当前是顶级',
  `icon` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `enable_status` tinyint DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='菜单表';

-- ----------------------------
-- Records of bbs_sa_menu
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sa_role
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_role`;
CREATE TABLE `bbs_sa_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `enable_status` tinyint NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of bbs_sa_role
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sa_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sa_role_menu`;
CREATE TABLE `bbs_sa_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '所属角色',
  `menu_id` bigint NOT NULL COMMENT '所属菜单',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of bbs_sa_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_sms
-- ----------------------------
DROP TABLE IF EXISTS `bbs_sms`;
CREATE TABLE `bbs_sms` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '短信标题',
  `msg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '短信内容',
  `send_status` tinyint NOT NULL DEFAULT '0' COMMENT '发送状态 0:未发送 1:已发送',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint DEFAULT NULL COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='短信表';

-- ----------------------------
-- Records of bbs_sms
-- ----------------------------

-- ----------------------------
-- Table structure for bbs_user_app
-- ----------------------------
DROP TABLE IF EXISTS `bbs_user_app`;
CREATE TABLE `bbs_user_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of bbs_user_app
-- ----------------------------
INSERT INTO `bbs_user_app` VALUES ('1001', 'b0ec72880b894b09b5c803dd8fa78518', '10001001', '2021-02-04 05:40:09', '0');
INSERT INTO `bbs_user_app` VALUES ('1002', 'b0ec72880b894b09b5c803dd8fa78518', '10001002', '2021-02-04 05:40:09', '0');
