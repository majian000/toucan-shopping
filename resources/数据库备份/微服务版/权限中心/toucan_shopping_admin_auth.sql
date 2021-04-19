/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80018
Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
Source Database       : toucan_shopping_admin_auth

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-19 22:48:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用编码',
  `enable_status` tinyint(4) DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='应用表';

-- ----------------------------
-- Records of t_app
-- ----------------------------
INSERT INTO `t_app` VALUES ('10', '用户中心', '10001002', '1', '0', '', '2021-02-12 14:34:25.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_app` VALUES ('11', '权限中心', '10001003', '1', '0', '', '2021-02-12 14:34:50.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);

-- ----------------------------
-- Table structure for t_sa_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin`;
CREATE TABLE `t_sa_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建用户ID -1初始化',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户主表';

-- ----------------------------
-- Records of t_sa_admin
-- ----------------------------
INSERT INTO `t_sa_admin` VALUES ('10', 'bb155acbf5ef43dcac9aa892274fadd5', 'sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', null, null, '2021-02-04 16:33:16.000000', null, null);
INSERT INTO `t_sa_admin` VALUES ('11', '44e9e4282d73452e96e1f53a15a5d675', 'sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '1', null, null, '2021-02-04 16:37:11.000000', null, null);

-- ----------------------------
-- Table structure for t_sa_admin_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_app`;
CREATE TABLE `t_sa_admin_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `enable_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1027 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of t_sa_admin_app
-- ----------------------------
INSERT INTO `t_sa_admin_app` VALUES ('1003', 'bb155acbf5ef43dcac9aa892274fadd5', '10000001', '0', '0', '2021-02-04 03:41:52.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1004', 'bb155acbf5ef43dcac9aa892274fadd5', '10000002', '0', '0', '2021-02-04 03:41:52.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1005', 'd10590b318544049ba2104d1f5517a7d', '10000001', '0', '0', '2021-02-04 03:47:09.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1006', 'd10590b318544049ba2104d1f5517a7d', '10000002', '0', '0', '2021-02-04 03:47:09.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1010', 'bb155acbf5ef43dcac9aa892274fadd5', '10001002', '0', '0', '2021-02-12 14:34:25.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1011', 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', '0', '1', '2021-02-12 14:34:50.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1012', 'bb155acbf5ef43dcac9aa892274fadd5', '11001001', '0', '0', '2021-02-13 17:35:01.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1013', 'bb155acbf5ef43dcac9aa892274fadd5', '11001002', '0', '0', '2021-02-13 17:36:08.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1014', 'bb155acbf5ef43dcac9aa892274fadd5', '11001003', '0', '0', '2021-02-13 17:37:09.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1015', 'bb155acbf5ef43dcac9aa892274fadd5', '11001004', '0', '0', '2021-02-13 17:38:44.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1016', 'bb155acbf5ef43dcac9aa892274fadd5', '11001005', '0', '0', '2021-02-13 17:38:58.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1017', 'bb155acbf5ef43dcac9aa892274fadd5', '11001006', '0', '0', '2021-02-13 17:39:09.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1018', 'bb155acbf5ef43dcac9aa892274fadd5', '11001007', '0', '0', '2021-02-13 17:39:17.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1019', 'bb155acbf5ef43dcac9aa892274fadd5', '11001008', '0', '0', '2021-02-13 17:39:26.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1020', 'bb155acbf5ef43dcac9aa892274fadd5', '11001009', '0', '0', '2021-02-13 17:39:35.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1021', 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', '0', '1', '2021-02-13 17:39:43.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1022', 'bb155acbf5ef43dcac9aa892274fadd5', '11001011', '0', '1', '2021-02-13 17:40:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1023', 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', '0', '1', '2021-02-14 10:38:57.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1024', 'bb155acbf5ef43dcac9aa892274fadd5', '11001012', '0', '1', '2021-02-14 10:39:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1025', 'bb155acbf5ef43dcac9aa892274fadd5', '11001013', '0', '1', '2021-02-14 10:39:12.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1026', 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', '0', '0', '2021-04-04 15:14:08.000000', 'bb155acbf5ef43dcac9aa892274fadd5');

-- ----------------------------
-- Table structure for t_sa_admin_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_dept`;
CREATE TABLE `t_sa_admin_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `dept_id` bigint(20) NOT NULL COMMENT '所属部门',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员部门关联表';

-- ----------------------------
-- Records of t_sa_admin_dept
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_role`;
CREATE TABLE `t_sa_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `role_id` varchar(32) NOT NULL COMMENT '所属角色',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员角色关联表';

-- ----------------------------
-- Records of t_sa_admin_role
-- ----------------------------
INSERT INTO `t_sa_admin_role` VALUES ('1', 'bb155acbf5ef43dcac9aa892274fadd5', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '0', '10001002', '2021-04-19 14:58:36.000000', 'bb155acbf5ef43dcac9aa892274fadd5');

-- ----------------------------
-- Table structure for t_sa_department
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_department`;
CREATE TABLE `t_sa_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `pid` bigint(20) NOT NULL COMMENT '上级部门 -1表示当前是顶级',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of t_sa_department
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_department_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_department_app`;
CREATE TABLE `t_sa_department_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint(20) NOT NULL COMMENT '所属角色',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门应用关联表';

-- ----------------------------
-- Records of t_sa_department_app
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_function`;
CREATE TABLE `t_sa_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `function_id` varbinary(32) DEFAULT NULL COMMENT '功能项ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接',
  `function_text` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '功能内容',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '菜单类型 0目录 1菜单 2按钮 3工具条按钮',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单 -1表示当前是顶级',
  `icon` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `function_sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `enable_status` tinyint(4) DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='功能项';

-- ----------------------------
-- Records of t_sa_function
-- ----------------------------
INSERT INTO `t_sa_function` VALUES ('5', 0x3339633833663839396465333462663539623231633638383635383236333261, '系统管理', '/', '', 'system', '0', '21', '', '11', '1', '0', '', '10001003', '2021-04-17 22:15:04.321000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-19 21:10:50.633000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('9', 0x6636616433363764623565343431393161643234613936303830336365613637, '应用列表', '/system/applist', null, 'system:appList', '0', '5', null, '11', '1', '0', '', '10001003', '2021-04-17 22:20:42.623000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('10', 0x6430333433653335623239643430373738376538346130613238663066313165, '账号列表', '/system/user', null, 'system:user', '0', '5', null, '12', '1', '0', '1', '10001003', '2021-04-17 22:21:19.116000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('11', 0x3834633934623539613165353464353338323166386431663237643633633536, '角色列表', '/system/role', null, 'system:role', '1', '5', null, '11', '1', '0', '', '10001003', '2021-04-17 22:21:44.153000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('12', 0x3661333730323661323863363435363238633163633262366434346163363161, '功能项列表', '/system/functionlist', null, 'system:function', '1', '5', null, '11', '1', '0', '', '10001003', '2021-04-17 22:22:18.880000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('21', 0x3563363463363065303831333434343238323161626339616437653362346338, '权限中心目录', '/system', '', 'system', '0', '-1', '', '11', '1', '0', '', '10001003', '2021-04-19 21:09:33.600000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);

-- ----------------------------
-- Table structure for t_sa_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role`;
CREATE TABLE `t_sa_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色ID',
  `enable_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of t_sa_role
-- ----------------------------
INSERT INTO `t_sa_role` VALUES ('1', '管理员', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '1', '10001002', '0', '用户中心管理员', '', '2021-04-17 01:00:20.599000', null, null);
INSERT INTO `t_sa_role` VALUES ('2', 'asdasd', 'b473cb41683b48af8b3ea6fe8db09127', '1', '10001002', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-17 16:16:54.937000', null, null);
INSERT INTO `t_sa_role` VALUES ('3', 'asdasd', '139333807abd4319ab43b62119703ef4', '1', '10001002', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-17 16:16:54.937000', null, null);
INSERT INTO `t_sa_role` VALUES ('4', '系统管理', 'e293c5082a354f79a5a8dac31e7e4dcc', '1', '10001002', '1', '11111', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-17 21:59:29.472000', null, null);
INSERT INTO `t_sa_role` VALUES ('5', '管理员', 'c81e574bb9d245719244228a0e65d1db', '1', '', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-18 18:47:04.474000', null, null);
INSERT INTO `t_sa_role` VALUES ('6', '用户中心2', 'ca8f909fe744488da42efd328384db0b', '1', '10001002', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-18 18:47:43.788000', null, null);
INSERT INTO `t_sa_role` VALUES ('7', '管理员22', '96484aee63504ab4a175ce685610adf8', '1', '10001002', '1', '222', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-18 18:57:03.852000', null, null);
INSERT INTO `t_sa_role` VALUES ('8', '权限中心管理员', 'f98da8a4f7ef412fa345225da51d14fb', '1', '10001003', '1', '1231231', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-18 19:00:10.473000', null, null);
INSERT INTO `t_sa_role` VALUES ('9', '测试禁用角色', 'a97fb5bceb744dfc94956fccb1f79717', '1', '10001002', '1', '44444111111222', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-18 19:09:32.328000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-18 19:10:05.588000');
INSERT INTO `t_sa_role` VALUES ('10', '系统管理员', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-19 21:05:40.028000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-19 21:06:02.953000');

-- ----------------------------
-- Table structure for t_sa_role_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role_function`;
CREATE TABLE `t_sa_role_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(32) NOT NULL COMMENT '所属角色',
  `function_id` varchar(32) NOT NULL COMMENT '所属菜单',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of t_sa_role_function
-- ----------------------------
INSERT INTO `t_sa_role_function` VALUES ('1', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '39c83f899de34bf59b21c6886582632a', '0', '10001002', '2021-04-19 15:01:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('25', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-19 22:06:44.008000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('26', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-19 22:06:44.245000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('27', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-19 22:06:44.445000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('28', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-19 22:33:59.525000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('29', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-19 22:33:59.883000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('30', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-19 22:33:59.994000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('31', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-19 22:34:00.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('32', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-19 22:47:34.804000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('33', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-19 22:47:35.267000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('34', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-19 22:47:35.676000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('35', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-19 22:47:35.846000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('36', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-19 22:47:36.311000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('37', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-19 22:47:44.341000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('38', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-19 22:47:44.617000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('39', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-19 22:47:44.926000', 'bb155acbf5ef43dcac9aa892274fadd5');
