/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : toucan_shopping_user_2

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-03-30 22:23:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user_0
-- ----------------------------
DROP TABLE IF EXISTS `t_user_0`;
CREATE TABLE `t_user_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户主表';

-- ----------------------------
-- Records of t_user_0
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_1
-- ----------------------------
DROP TABLE IF EXISTS `t_user_1`;
CREATE TABLE `t_user_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户主表';

-- ----------------------------
-- Records of t_user_1
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_app
-- ----------------------------
DROP TABLE IF EXISTS `t_user_app`;
CREATE TABLE `t_user_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of t_user_app
-- ----------------------------
INSERT INTO `t_user_app` VALUES ('1001', 'b0ec72880b894b09b5c803dd8fa78518', '10001001', '2021-02-04 05:40:09', '0');
INSERT INTO `t_user_app` VALUES ('1002', 'b0ec72880b894b09b5c803dd8fa78518', '10001002', '2021-02-04 05:40:09', '0');

-- ----------------------------
-- Table structure for t_user_detail_0
-- ----------------------------
DROP TABLE IF EXISTS `t_user_detail_0`;
CREATE TABLE `t_user_detail_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `nick_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `true_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `head_sculpture` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `id_card` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证',
  `user_main_id` bigint NOT NULL COMMENT '所属用户',
  `sex` tinyint NOT NULL DEFAULT '0' COMMENT '性别 0:女 1:男',
  `type` tinyint DEFAULT NULL COMMENT '用户类型 0:买家 1:卖家 2:既是买家又是卖家',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户详细信息';

-- ----------------------------
-- Records of t_user_detail_0
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_detail_1
-- ----------------------------
DROP TABLE IF EXISTS `t_user_detail_1`;
CREATE TABLE `t_user_detail_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `nick_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `true_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `head_sculpture` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `id_card` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证',
  `user_main_id` bigint NOT NULL COMMENT '所属用户',
  `sex` tinyint NOT NULL DEFAULT '0' COMMENT '性别 0:女 1:男',
  `type` tinyint DEFAULT NULL COMMENT '用户类型 0:买家 1:卖家 2:既是买家又是卖家',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户详细信息';

-- ----------------------------
-- Records of t_user_detail_1
-- ----------------------------
INSERT INTO `t_user_detail_1` VALUES ('823914828740427844', '用户18701601892', null, null, null, '823914828371329051', '1', null, '2021-03-23 05:43:23', '0');
INSERT INTO `t_user_detail_1` VALUES ('826581824544178250', '用户18701601892', null, null, null, '826581822723850309', '1', null, '2021-03-30 22:21:04', '0');
INSERT INTO `t_user_detail_1` VALUES ('826581970128470084', '用户18701601898', null, null, null, '826581968996007997', '1', null, '2021-03-30 22:21:39', '0');

-- ----------------------------
-- Table structure for t_user_email_0
-- ----------------------------
DROP TABLE IF EXISTS `t_user_email_0`;
CREATE TABLE `t_user_email_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱(可用于登录)',
  `user_main_id` bigint NOT NULL COMMENT '用户主表ID',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户邮箱关联子表';

-- ----------------------------
-- Records of t_user_email_0
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_email_1
-- ----------------------------
DROP TABLE IF EXISTS `t_user_email_1`;
CREATE TABLE `t_user_email_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱(可用于登录)',
  `user_main_id` bigint NOT NULL COMMENT '用户主表ID',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户邮箱关联子表';

-- ----------------------------
-- Records of t_user_email_1
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_login_record
-- ----------------------------
DROP TABLE IF EXISTS `t_user_login_record`;
CREATE TABLE `t_user_login_record` (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录';

-- ----------------------------
-- Records of t_user_login_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_mobile_phone_0
-- ----------------------------
DROP TABLE IF EXISTS `t_user_mobile_phone_0`;
CREATE TABLE `t_user_mobile_phone_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `mobile_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号(注册时必填)',
  `user_main_id` bigint NOT NULL COMMENT '用户主表ID',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户手机号关联子表';

-- ----------------------------
-- Records of t_user_mobile_phone_0
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_mobile_phone_1
-- ----------------------------
DROP TABLE IF EXISTS `t_user_mobile_phone_1`;
CREATE TABLE `t_user_mobile_phone_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `mobile_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号(注册时必填)',
  `user_main_id` bigint NOT NULL COMMENT '用户主表ID',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户手机号关联子表';

-- ----------------------------
-- Records of t_user_mobile_phone_1
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_username_0
-- ----------------------------
DROP TABLE IF EXISTS `t_user_username_0`;
CREATE TABLE `t_user_username_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `user_main_id` bigint NOT NULL COMMENT '用户主表ID',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户与用户名关联子表';

-- ----------------------------
-- Records of t_user_username_0
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_username_1
-- ----------------------------
DROP TABLE IF EXISTS `t_user_username_1`;
CREATE TABLE `t_user_username_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `username` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `user_main_id` bigint NOT NULL COMMENT '用户主表ID',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `delete_status` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户与用户名关联子表';

-- ----------------------------
-- Records of t_user_username_1
-- ----------------------------
