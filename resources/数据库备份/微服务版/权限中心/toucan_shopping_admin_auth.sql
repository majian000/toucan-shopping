/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80018
Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
Source Database       : toucan_shopping_admin_auth

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-30 22:32:44
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
INSERT INTO `t_app` VALUES ('11', '权限管理', '10001003', '1', '0', '', '2021-02-12 14:34:50.000000', 'bb155acbf5ef43dcac9aa892274fadd5', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-25 21:46:40.655000');

-- ----------------------------
-- Table structure for t_sa_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin`;
CREATE TABLE `t_sa_admin` (
  `id` bigint(20) NOT NULL COMMENT '主键',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统用户主表';

-- ----------------------------
-- Records of t_sa_admin
-- ----------------------------
INSERT INTO `t_sa_admin` VALUES ('10', 'bb155acbf5ef43dcac9aa892274fadd5', 'sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', null, null, '2021-02-04 16:33:16.000000', null, null);
INSERT INTO `t_sa_admin` VALUES ('11', '44e9e4282d73452e96e1f53a15a5d675', 'sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '1', null, null, '2021-02-04 16:37:11.000000', null, null);
INSERT INTO `t_sa_admin` VALUES ('12', '10d08ecadc0a4e74a9f61a4a979bcd6e', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 21:30:46.369000', null, null);
INSERT INTO `t_sa_admin` VALUES ('13', '71ee9022096d4e70b44df286af3cf251', 'sa002', '4297f44b13955235245b2497399d7a93', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 21:32:01.830000', '71ee9022096d4e70b44df286af3cf251', '2021-04-23 23:00:55.926000');
INSERT INTO `t_sa_admin` VALUES ('14', 'f8758a5423074a18a3039255c32f901a', 'sa03', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 22:02:00.518000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-22 22:20:47.585000');
INSERT INTO `t_sa_admin` VALUES ('77', '263681613aff46619ed1b4a175b0fc33', 'sa04', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '123', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-22 22:11:54.049000', null, null);
INSERT INTO `t_sa_admin` VALUES ('78', '2d44a5d572ca408593336a1cf09bfe14', 'sa02', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-22 23:09:49.238000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:25:37.786000');
INSERT INTO `t_sa_admin` VALUES ('79', '6a94e16ceeab4a99b5d74fc34a00ae03', 'sa003', 'c33367701511b4f6020ec61ded352059', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:41:27.141000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 22:26:06.587000');
INSERT INTO `t_sa_admin` VALUES ('80', '6eeaa44c7ba6402cb62348b496b15dcd', 'zhanghao001', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 23:09:49.684000', null, null);
INSERT INTO `t_sa_admin` VALUES ('81', 'f70a3f2d8ccc46cf8636259383b4849b', 'app_manager_01', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:25:51.700000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-25 21:46:16.532000');
INSERT INTO `t_sa_admin` VALUES ('82', '08d044fa382d4011b43026bd85696618', 'm_sa', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 23:17:56.061000', null, null);
INSERT INTO `t_sa_admin` VALUES ('83', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'm_sa02', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-30 20:45:21.385000', null, null);
INSERT INTO `t_sa_admin` VALUES ('837805783709122635', 'd62732b6a00643ecab2295a27be92b14', 'm_sa03', 'e10adc3949ba59abbe56e057f20f883e', '1', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-30 21:41:08.830000', null, null);

-- ----------------------------
-- Table structure for t_sa_admin_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_app`;
CREATE TABLE `t_sa_admin_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1071 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of t_sa_admin_app
-- ----------------------------
INSERT INTO `t_sa_admin_app` VALUES ('1003', 'bb155acbf5ef43dcac9aa892274fadd5', '10000001', '0', '2021-02-04 03:41:52.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1004', 'bb155acbf5ef43dcac9aa892274fadd5', '10000002', '0', '2021-02-04 03:41:52.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1005', 'd10590b318544049ba2104d1f5517a7d', '10000001', '0', '2021-02-04 03:47:09.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1006', 'd10590b318544049ba2104d1f5517a7d', '10000002', '0', '2021-02-04 03:47:09.000000', 'd10590b318544049ba2104d1f5517a7d');
INSERT INTO `t_sa_admin_app` VALUES ('1010', 'bb155acbf5ef43dcac9aa892274fadd5', '10001002', '0', '2021-02-12 14:34:25.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1011', 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', '1', '2021-02-12 14:34:50.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1012', 'bb155acbf5ef43dcac9aa892274fadd5', '11001001', '0', '2021-02-13 17:35:01.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1013', 'bb155acbf5ef43dcac9aa892274fadd5', '11001002', '0', '2021-02-13 17:36:08.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1014', 'bb155acbf5ef43dcac9aa892274fadd5', '11001003', '0', '2021-02-13 17:37:09.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1015', 'bb155acbf5ef43dcac9aa892274fadd5', '11001004', '0', '2021-02-13 17:38:44.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1016', 'bb155acbf5ef43dcac9aa892274fadd5', '11001005', '0', '2021-02-13 17:38:58.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1017', 'bb155acbf5ef43dcac9aa892274fadd5', '11001006', '0', '2021-02-13 17:39:09.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1018', 'bb155acbf5ef43dcac9aa892274fadd5', '11001007', '0', '2021-02-13 17:39:17.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1019', 'bb155acbf5ef43dcac9aa892274fadd5', '11001008', '0', '2021-02-13 17:39:26.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1020', 'bb155acbf5ef43dcac9aa892274fadd5', '11001009', '0', '2021-02-13 17:39:35.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1021', 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', '1', '2021-02-13 17:39:43.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1022', 'bb155acbf5ef43dcac9aa892274fadd5', '11001011', '1', '2021-02-13 17:40:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1023', 'bb155acbf5ef43dcac9aa892274fadd5', '11001010', '1', '2021-02-14 10:38:57.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1024', 'bb155acbf5ef43dcac9aa892274fadd5', '11001012', '1', '2021-02-14 10:39:06.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1025', 'bb155acbf5ef43dcac9aa892274fadd5', '11001013', '1', '2021-02-14 10:39:12.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1026', 'bb155acbf5ef43dcac9aa892274fadd5', '10001003', '0', '2021-04-04 15:14:08.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1027', '10d08ecadc0a4e74a9f61a4a979bcd6e', '10001002', '0', '2021-04-20 21:30:47.719000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1028', '10d08ecadc0a4e74a9f61a4a979bcd6e', '10001003', '0', '2021-04-20 21:30:47.780000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1029', '71ee9022096d4e70b44df286af3cf251', '10001002', '0', '2021-04-20 21:32:01.909000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1030', '71ee9022096d4e70b44df286af3cf251', '10001003', '0', '2021-04-20 21:32:01.987000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1031', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-20 22:02:00.877000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1032', '263681613aff46619ed1b4a175b0fc33', '10001002', '0', '2021-04-22 22:11:54.167000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1033', '263681613aff46619ed1b4a175b0fc33', '10001003', '0', '2021-04-22 22:11:54.217000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1034', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:14:46.140000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1035', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:14:46.176000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1036', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:15:35.328000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1037', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:15:51.307000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1038', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:15:55.775000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1039', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:15:58.690000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1040', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:15:55.693000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1041', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:16:34.010000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1042', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:16:34.493000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1043', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:16:34.182000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1044', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:17:06.341000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1045', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:17:07.658000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1046', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:17:22.978000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1047', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:17:27.254000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1048', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:17:27.301000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1049', 'f8758a5423074a18a3039255c32f901a', '10001003', '1', '2021-04-22 22:20:29.622000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1050', 'f8758a5423074a18a3039255c32f901a', '10001002', '1', '2021-04-22 22:20:33.376000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1051', 'f8758a5423074a18a3039255c32f901a', '10001002', '0', '2021-04-22 22:20:47.745000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1052', 'f8758a5423074a18a3039255c32f901a', '10001003', '0', '2021-04-22 22:20:47.790000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1053', '2d44a5d572ca408593336a1cf09bfe14', '10001002', '1', '2021-04-22 23:09:49.349000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1054', '2d44a5d572ca408593336a1cf09bfe14', '10001002', '0', '2021-04-23 21:25:37.937000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1055', '2d44a5d572ca408593336a1cf09bfe14', '10001003', '0', '2021-04-23 21:25:38.011000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1056', '6a94e16ceeab4a99b5d74fc34a00ae03', '10001002', '1', '2021-04-23 21:41:27.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1057', '6a94e16ceeab4a99b5d74fc34a00ae03', '10001003', '1', '2021-04-23 21:41:27.240000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1058', '6a94e16ceeab4a99b5d74fc34a00ae03', '10001002', '1', '2021-04-23 21:41:39.917000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1059', '6a94e16ceeab4a99b5d74fc34a00ae03', '10001003', '0', '2021-04-23 21:58:58.593000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1060', '6a94e16ceeab4a99b5d74fc34a00ae03', '10001002', '1', '2021-04-23 21:59:18.146000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1061', '6a94e16ceeab4a99b5d74fc34a00ae03', '10001002', '0', '2021-04-23 22:01:05.181000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1062', '6eeaa44c7ba6402cb62348b496b15dcd', '10001003', '0', '2021-04-23 23:09:49.729000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1063', 'f70a3f2d8ccc46cf8636259383b4849b', '10001003', '0', '2021-04-24 01:25:51.940000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1064', 'f70a3f2d8ccc46cf8636259383b4849b', '10001002', '1', '2021-04-25 21:46:11.994000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1065', '08d044fa382d4011b43026bd85696618', '10001002', '0', '2021-04-27 23:17:56.094000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1066', '08d044fa382d4011b43026bd85696618', '10001003', '0', '2021-04-27 23:17:56.100000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1067', '7bbdca6e27e642478b6fb5b1e1c8eab8', '10001002', '0', '2021-04-30 20:45:21.842000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1068', '7bbdca6e27e642478b6fb5b1e1c8eab8', '10001003', '0', '2021-04-30 20:45:22.268000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1069', 'd62732b6a00643ecab2295a27be92b14', '10001002', '0', '2021-04-30 21:41:08.889000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_app` VALUES ('1070', 'd62732b6a00643ecab2295a27be92b14', '10001003', '0', '2021-04-30 21:41:08.935000', 'bb155acbf5ef43dcac9aa892274fadd5');

-- ----------------------------
-- Table structure for t_sa_admin_orgnazition
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_admin_orgnazition`;
CREATE TABLE `t_sa_admin_orgnazition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `orgnazition_id` varchar(32) NOT NULL COMMENT '所属部门',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员部门关联表';

-- ----------------------------
-- Records of t_sa_admin_orgnazition
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
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员角色关联表';

-- ----------------------------
-- Records of t_sa_admin_role
-- ----------------------------
INSERT INTO `t_sa_admin_role` VALUES ('18', '10d08ecadc0a4e74a9f61a4a979bcd6e', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '0', '10001002', '2021-04-22 21:09:17.852000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('19', '10d08ecadc0a4e74a9f61a4a979bcd6e', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0', '10001003', '2021-04-22 21:09:17.893000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('20', '10d08ecadc0a4e74a9f61a4a979bcd6e', 'f0349fe34ce34a9780033354cc27d335', '0', '10001003', '2021-04-22 21:09:17.931000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('24', '71ee9022096d4e70b44df286af3cf251', '7943ff9283ba4572bdad44838cc29052', '0', '10001003', '2021-04-22 22:35:18.082000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('25', 'bb155acbf5ef43dcac9aa892274fadd5', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '0', '10001002', '2021-04-22 23:00:47.231000', '10d08ecadc0a4e74a9f61a4a979bcd6e');
INSERT INTO `t_sa_admin_role` VALUES ('26', 'bb155acbf5ef43dcac9aa892274fadd5', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0', '10001003', '2021-04-22 23:00:47.276000', '10d08ecadc0a4e74a9f61a4a979bcd6e');
INSERT INTO `t_sa_admin_role` VALUES ('27', 'bb155acbf5ef43dcac9aa892274fadd5', 'f0349fe34ce34a9780033354cc27d335', '0', '10001003', '2021-04-22 23:00:47.336000', '10d08ecadc0a4e74a9f61a4a979bcd6e');
INSERT INTO `t_sa_admin_role` VALUES ('28', 'bb155acbf5ef43dcac9aa892274fadd5', '7943ff9283ba4572bdad44838cc29052', '0', '10001003', '2021-04-22 23:00:47.382000', '10d08ecadc0a4e74a9f61a4a979bcd6e');
INSERT INTO `t_sa_admin_role` VALUES ('31', '6eeaa44c7ba6402cb62348b496b15dcd', '7943ff9283ba4572bdad44838cc29052', '0', '10001003', '2021-04-23 23:10:00.764000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('108', 'f70a3f2d8ccc46cf8636259383b4849b', 'f033f6356c2449bdb34d657fa4720ffa', '0', '10001003', '2021-04-26 21:58:52.962000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('109', '263681613aff46619ed1b4a175b0fc33', '142a30e4210c48a0966e84bf1e22e815', '0', '10001003', '2021-04-26 22:33:27.996000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_admin_role` VALUES ('110', '08d044fa382d4011b43026bd85696618', '89d3bf8fbf4543188a64fb7738a6bc62', '0', '10001003', '2021-04-27 23:18:21.403000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('111', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('112', '7bbdca6e27e642478b6fb5b1e1c8eab8', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('113', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('114', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('115', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('116', '7bbdca6e27e642478b6fb5b1e1c8eab8', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('117', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('118', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('119', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '2021-04-30 20:59:06.371000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('120', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('121', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('122', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('123', '7bbdca6e27e642478b6fb5b1e1c8eab8', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('124', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('125', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('126', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('127', '7bbdca6e27e642478b6fb5b1e1c8eab8', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('128', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('129', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('130', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '2021-04-30 20:59:11.601000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('131', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 20:59:23.248000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('132', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '2021-04-30 20:59:23.248000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('133', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('134', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('135', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('136', '7bbdca6e27e642478b6fb5b1e1c8eab8', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('137', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('138', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('139', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('140', '7bbdca6e27e642478b6fb5b1e1c8eab8', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('141', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('142', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('143', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '2021-04-30 20:59:26.772000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('144', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 20:59:31.441000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('145', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 20:59:31.441000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('146', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('147', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('148', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('149', '7bbdca6e27e642478b6fb5b1e1c8eab8', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('150', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('151', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('152', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('153', '7bbdca6e27e642478b6fb5b1e1c8eab8', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('154', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('155', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('156', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '2021-04-30 20:59:35.829000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('157', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 20:59:46.086000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('158', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 20:59:46.086000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('159', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:00:11.200000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('160', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:00:11.200000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('161', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 21:00:11.200000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('162', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 21:00:11.200000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('163', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:00:15.561000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('164', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:00:15.561000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('165', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 21:00:15.561000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('166', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 21:00:15.561000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('167', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 21:00:15.561000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('168', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 21:00:15.561000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('169', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '1', '10001002', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('170', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('171', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('172', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('173', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('174', '7bbdca6e27e642478b6fb5b1e1c8eab8', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('175', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('176', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('177', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('178', '7bbdca6e27e642478b6fb5b1e1c8eab8', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('179', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('180', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('181', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '2021-04-30 21:00:20.709000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('182', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:00:26.212000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('183', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:00:26.212000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('184', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '1', '10001002', '2021-04-30 21:01:26.303000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('185', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:01:26.303000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('186', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:01:26.303000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('187', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '1', '10001002', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('188', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('189', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('190', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('191', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('192', '7bbdca6e27e642478b6fb5b1e1c8eab8', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('193', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('194', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('195', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('196', '7bbdca6e27e642478b6fb5b1e1c8eab8', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('197', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('198', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('199', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '2021-04-30 21:01:30.874000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('200', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:01:36.750000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('201', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:01:36.750000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('202', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 21:01:36.750000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('203', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '2021-04-30 21:01:41.031000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('204', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1', '10001003', '2021-04-30 21:01:41.031000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('205', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '2021-04-30 21:01:41.031000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('206', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '2021-04-30 21:01:41.031000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('207', '7bbdca6e27e642478b6fb5b1e1c8eab8', '520fccd50efa484e9ab2f87cddd22283', '0', '10001002', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('208', '7bbdca6e27e642478b6fb5b1e1c8eab8', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0', '10001003', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('209', '7bbdca6e27e642478b6fb5b1e1c8eab8', '7943ff9283ba4572bdad44838cc29052', '0', '10001003', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('210', '7bbdca6e27e642478b6fb5b1e1c8eab8', 'f033f6356c2449bdb34d657fa4720ffa', '0', '10001003', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('211', '7bbdca6e27e642478b6fb5b1e1c8eab8', '142a30e4210c48a0966e84bf1e22e815', '0', '10001003', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('212', '7bbdca6e27e642478b6fb5b1e1c8eab8', '89d3bf8fbf4543188a64fb7738a6bc62', '0', '10001003', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_admin_role` VALUES ('213', '7bbdca6e27e642478b6fb5b1e1c8eab8', '572bc044d1e24be19faa6a2235043720', '0', '10001003', '2021-04-30 21:02:00.589000', 'bb155acbf5ef43dcac9aa892274fadd5');

-- ----------------------------
-- Table structure for t_sa_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_function`;
CREATE TABLE `t_sa_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `function_id` varbinary(32) DEFAULT NULL COMMENT '功能项ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接',
  `function_text` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '功能内容',
  `permission` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '功能项类型 0目录 1菜单 2按钮 3工具条按钮 4:API 5页面控件',
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
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='功能项';

-- ----------------------------
-- Records of t_sa_function
-- ----------------------------
INSERT INTO `t_sa_function` VALUES ('5', 0x3339633833663839396465333462663539623231633638383635383236333261, '系统管理', '', '', 'permissioncenter:system', '0', '21', 'fa fa-address-book', '11', '1', '0', '', '10001003', '2021-04-17 22:15:04.321000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-22 23:08:33.823000', '10d08ecadc0a4e74a9f61a4a979bcd6e');
INSERT INTO `t_sa_function` VALUES ('9', 0x6636616433363764623565343431393161643234613936303830336365613637, '应用列表', '/app/listPage', null, 'permissioncenter:system:app', '0', '5', null, '11', '1', '0', '', '10001003', '2021-04-17 22:20:42.623000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('10', 0x6430333433653335623239643430373738376538346130613238663066313165, '账号列表', '/admin/listPage', null, 'permissioncenter:system:admin', '0', '5', null, '12', '1', '0', '', '10001003', '2021-04-17 22:21:19.116000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('11', 0x3834633934623539613165353464353338323166386431663237643633633536, '角色列表', '/role/listPage', null, 'permissioncenter:system:role', '1', '5', null, '11', '1', '0', '', '10001003', '2021-04-17 22:21:44.153000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('12', 0x3661333730323661323863363435363238633163633262366434346163363161, '功能项列表', '/function/listPage', null, 'permissioncenter:system:function', '1', '5', null, '11', '1', '0', '', '10001003', '2021-04-17 22:22:18.880000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('21', 0x3563363463363065303831333434343238323161626339616437653362346338, '权限管理', '', '', 'permissioncenter', '0', '-1', 'fa fa-address-book', '11', '1', '0', '', '10001003', '2021-04-19 21:09:33.600000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:43:42.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('22', 0x3336336130643838366661353465383238323163303865306230343633646535, '查看列表API', '/function/tree/table', null, 'permissioncenter:system:function:tree', '4', '12', null, '11', '1', '0', null, '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('23', 0x6133393231383864376661633437326262663162643633656563383664343838, '保存API', '/function/save', '', 'permissioncenter:system:function:save', '4', '30', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:42:59.116000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('24', 0x6562366634636264333430303461633438353363306662386133343335363532, '删除API', '/function/delete', '', 'permissioncenter:system:function:delete', '4', '33', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:44:36.806000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('25', 0x6638346438363466656538373436363938616634373837653735343232393064, '批量删除API', '/function/delete/ids', '', 'permissioncenter:system:function:deletes', '4', '31', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:45:51.154000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('26', 0x6630353735343265626466643432366362626162383461636634313936333761, '查询角色功能树API', '/function/query/role/function/tree', '', 'permissioncenter:system:function:role:function:tree', '4', '11', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:52:59.087000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('27', 0x6466613233613431376161393439326539366235613465356437326165363162, '修改API', '/function/update', '', 'permissioncenter:system:function:update', '4', '32', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:46:23.993000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('28', 0x6237336437663132343339393437616138303439626564386333373030333135, '跳转保存界面API', '/function/addPage', '', 'permissioncenter:system:function:addPage', '4', '30', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:44:04.693000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('29', 0x3334613536663838363861383430313061393864316131656535316633366239, '跳转编辑界面API', '/function/editPage', '', 'permissioncenter:system:function:editPage', '4', '32', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:46:38.022000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('30', 0x6238323233373731396362663434303962653838363130633733373434386432, '添加', '/', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'permissioncenter:system:function:addToolbarBtn', '3', '12', null, '11', '1', '0', '工具条》添加按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('31', 0x6161326635616633303365333432373339383065633163336338393731353766, '删除', '/', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'permissioncenter:system:function:deleteToolbarBtn', '3', '12', null, '11', '1', '0', '工具条》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('32', 0x3731306439376263653536383464376139356264393237313530613332336135, '编辑', '/', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"edit\">修改</a> &nbsp;&nbsp;', 'permissioncenter:system:function:updateRowBtn', '2', '12', '', '11', '1', '0', '操作列》修改按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:07:07.666000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('33', 0x3331663336386265306662303433653762383838353064646666366232376533, '删除', '/', '<a class=\"layui-btn layui-btn-danger layui-btn-xs\" lay-event=\"delete\">删除</a>', 'permissioncenter:system:function:deleteRowBtn', '2', '12', '', '11', '1', '0', '操作列》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:42:34.405000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('34', 0x6263363866613637653237623434613338656637343664353236333833326535, '查看列表API', '/role/list', '', 'permissioncenter:system:role:list', '4', '11', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('35', 0x3430626434373636653163333461306662663565346539326261363739303061, '保存API', '/role/save', '', 'permissioncenter:system:role:save', '4', '41', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('36', 0x6334356262633239346261633461363062613834643736633436373731636634, '删除API', '/role/delete', '', 'permissioncenter:system:role:delete', '4', '44', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('37', 0x3637616366323963323134383462393762336236653238326564646137366361, '批量删除API', '/role/delete/ids', '', 'permissioncenter:system:role:deletes', '4', '42', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 11:16:36.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('38', 0x6639313161336133656434353430646162383163666136626165366334643534, '编辑API', '/role/update', '', 'permissioncenter:system:role:update', '4', '43', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:46:23.993000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('39', 0x3234306133376265353632383430636239386533386439356265633133333166, '跳转保存界面API', '/role/addPage', '', 'permissioncenter:system:role:addPage', '4', '41', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:44:04.693000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('40', 0x6664356336633461393838313465363438623634643832666237613035626136, '跳转编辑界面API', '/role/editPage', '', 'permissioncenter:system:role:editPage', '4', '43', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:46:38.022000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('41', 0x6464663530626230646161303437613138336136343561363033326230363136, '添加', '/', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'permissioncenter:system:role:addToolbarBtn', '3', '11', null, '11', '1', '0', '工具条》添加按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('42', 0x6261303730306437336233383435353838383032386265386533653337366630, '删除', '/', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'permissioncenter:system:role:deleteToolbarBtn', '3', '11', null, '11', '1', '0', '工具条》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('43', 0x6130373765373239636538393432653338643666326336303661626134386562, '编辑', '/', '<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"edit\">编辑</a> &nbsp;&nbsp;', 'permissioncenter:system:role:updateRowBtn', '2', '11', '', '11', '1', '0', '操作列》编辑按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:10:03.773000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('44', 0x6334393161316363316362653463323638636130333237313531633135363763, '删除', '/', '<a class=\"layui-btn layui-btn-xs layui-btn-danger data-count-delete\" lay-event=\"delete\">删除</a> &nbsp;&nbsp;', 'permissioncenter:system:role:deleteRowBtn', '2', '11', '', '11', '1', '0', '操作列》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:10:20.360000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('45', 0x6632633136343463633332653438666461323561386131633537383464323939, '权限', '/', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"permission\">权限</a>', 'permissioncenter:system:role:permissionRowBtn', '2', '11', '', '11', '1', '0', '操作列》权限按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:42:09.223000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('46', 0x6162663463356531663261323462326138373461663366653534613234303065, '查看权限树API', '/function/query/role/function/tree', '', 'permissioncenter:system:role:function:tree', '4', '45', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('47', 0x6564313662373531323765373464653162653137363235313137356232613665, '保存权限树API', '/role/connect/functions', '', 'permissioncenter:system:role:connect:functions', '4', '45', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('48', 0x6561303930323433316533343431373162383638383335646238343038336536, '添加', '/', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'permissioncenter:system:app:addToolbarBtn', '3', '9', null, '11', '1', '0', '工具条》添加按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('49', 0x6431353662646661306430633462346261356634363061396430353530373962, '删除', '/', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'permissioncenter:system:app:deleteToolbarBtn', '3', '9', null, '11', '1', '0', '工具条》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('50', 0x3535393231656464393736383464366238363362343564366132663062633365, '编辑', '/', '<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"edit\">编辑</a> &nbsp;&nbsp;', 'permissioncenter:system:app:updateRowBtn', '2', '9', '', '11', '1', '0', '操作列》修改按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:12:10.125000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('51', 0x3833653932623833386330373430356462636437666634633034363237383535, '删除', '/', '<a class=\"layui-btn layui-btn-xs layui-btn-danger data-count-delete\" lay-event=\"delete\">删除</a> &nbsp;&nbsp;', 'permissioncenter:system:app:deleteRowBtn', '2', '9', '', '11', '1', '0', '操作列》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:12:19.452000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('52', 0x3633313730333633333336623431346661636132633334306164626361626334, '跳转保存界面API', '/app/addPage', '', 'permissioncenter:system:app:addPage', '4', '48', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:44:04.693000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('53', 0x3865663866346664626463303436383161366337316264313831386161353933, '跳转编辑界面API', '/app/editPage', '', 'permissioncenter:system:app:editPage', '4', '50', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:46:38.022000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('54', 0x6139643661653563383065333432616361663465656139393335356639316262, '删除API', '/app/delete', '', 'permissioncenter:system:app:delete', '4', '49', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('55', 0x3831626261303761346564313461643861626534363232366264393134303135, '批量删除API', '/app/delete/ids', '', 'permissioncenter:system:app:deletes', '4', '51', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 11:16:36.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('56', 0x3731393637343733353938383435643661303830346133666262646261313333, '保存API', '/app/save', '', 'permissioncenter:system:app:save', '4', '48', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, '');
INSERT INTO `t_sa_function` VALUES ('57', 0x3436346566326432643261303466646539613033336336363633626661363662, '编辑API', '/app/update', '', 'permissioncenter:system:app:update', '4', '50', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-20 20:46:23.993000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('58', 0x6231323361663064353166663430316462326137343237303263303131653062, '查看列表API', '/app/list', '', 'permissioncenter:system:app:list', '4', '9', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 21:30:04.871000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('59', 0x6239623262303136313166633466393038356566646433303661396661613832, '用户管理', '', '', 'usercenter', '0', '-1', '', '1', '1', '0', '', '10001002', '2021-04-21 22:02:59.137000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:11:07.042000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('60', 0x3861393136316132376631313465333338663038653137623336313437633835, '用户统计', '/', '', 'usercenter:statistics', '0', '-1', '', '2', '1', '0', '', '10001002', '2021-04-21 22:05:22.203000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:22:16.710000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('61', 0x3230386539643365616535353466373162646138363330623631623234613664, '活跃用户统计', '/', '', 'usercenter:statistics:alive', '0', '60', '', '1', '1', '1', '', '10001002', '2021-04-21 22:20:40.955000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('62', 0x6631623237386263326530383432336138633062636662383835623038313265, '用户管理', '/', '', 'usercenter:user', '1', '59', '', '1', '1', '0', '', '10001002', '2021-04-21 22:24:38.975000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 22:25:12.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('63', 0x3363373232663863343837653462633361616161656530333836386666303839, '用户列表', '/user/listPage', '', 'usercenter:user:listpage', '1', '62', '', '1', '1', '0', '', '10001002', '2021-04-21 22:25:32.910000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('64', 0x3830313733323039313463643432396562366337376530636331333333643031, '查询列表API', '/user/list', '', 'usercenter:user:list:api', '4', '63', '', '1', '1', '1', '', '10001002', '2021-04-21 22:35:08.589000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('68', 0x6435373462346137366331393464343939643164353435346161663061383039, '添加', '/', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'permissioncenter:system:admin:addToolbarBtn', '3', '10', null, '11', '1', '0', '工具条》添加按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('69', 0x3666626465636135663033353437363661366564633138353265656261316666, '删除', '/', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'permissioncenter:system:admin:deleteToolbarBtn', '3', '10', null, '11', '1', '0', '工具条》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('70', 0x6664326230323466333637313432383261623737383838373965646366376231, '编辑', '/', '<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"edit\">编辑</a> &nbsp;&nbsp;', 'permissioncenter:system:admin:updateRowBtn', '2', '10', '', '11', '1', '0', '操作列》修改按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:23:33.419000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('71', 0x3161353135333530393764643436326138393865343233333839306531333033, '删除', '/', '<a class=\"layui-btn layui-btn-xs layui-btn-danger data-count-delete\" lay-event=\"delete\">删除</a> &nbsp;&nbsp;', 'permissioncenter:system:admin:deleteRowBtn', '2', '10', '', '11', '1', '0', '操作列》删除按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:23:56.817000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('72', 0x6636376561663634396539663432646362363063373139303833386231316530, '角色', '/', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"role\">角色</a> &nbsp;&nbsp;', 'permissioncenter:system:admin:roleRowBtn', '2', '10', '', '11', '1', '0', '操作列》角色按钮', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:24:06.815000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('73', 0x6536626163333266393530613436643661303630633134663065626636346237, '跳转保存界面API', '/admin/addPage', '', 'permissioncenter:system:admin:addPage', '4', '68', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:18:06.761000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('74', 0x3062333437626264623666343465393239306464303637366136336235346439, '跳转编辑界面API', '/admin/editPage', '', 'permissioncenter:system:admin:editPage', '4', '70', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:28:59.361000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('75', 0x6262313239303065613366323438366339393933316635346564326136386466, '删除API', '/admin/delete', '', 'permissioncenter:system:admin:delete', '4', '71', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:31:27.143000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('76', 0x3630366335383664623139363431303239653037366566663337313433626133, '批量删除API', '/admin/delete/ids', '', 'permissioncenter:system:admin:deletes', '4', '69', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:30:49.082000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('77', 0x3832366266356364363766643431363739626262343661313366623137613334, '保存API', '/admin/save', '', 'permissioncenter:system:admin:save', '4', '68', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:18:33.722000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('78', 0x3238316130316634643966303439383261363934303830333930386633613362, '编辑API', '/admin/update', '', 'permissioncenter:system:admin:update', '4', '70', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:29:26.523000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('79', 0x3936653963663530383633633436396139356530663530303834613130383362, '查看列表API', '/admin/list', '', 'permissioncenter:system:admin:list', '4', '10', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 21:30:04.871000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('80', 0x6337386134353335623764313439376338656264393462363365393039393136, '查看角色树', '/role/query/admin/role/tree', '', 'permissioncenter:system:admin:tree:list', '4', '72', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-25 21:37:13.947000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('81', 0x6565656161333731383435633431326161306462616661356234386237396461, '保存角色关联', '/admin/connect/roles', '', 'permissioncenter:system:admin:connect:roles', '4', '72', '', '11', '1', '0', '', '10001003', '2021-04-20 14:44:07.000000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:30:19.426000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('82', 0x6437316630616232326462623439633039613938373232326138643533653031, '修改密码', '/', '<a class=\"layui-btn layui-btn-normal layui-btn-xs \" lay-event=\"password\">修改密码</a>&nbsp;&nbsp;', 'permissioncenter:system:admin:passwordRowBtn', '2', '10', '', '11', '1', '0', '', '10001003', '2021-04-24 01:16:01.659000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-30 20:58:07.820000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('83', 0x3639656165313962353937373436386239353239353237373330373861643737, '跳转修改密码界面API', '/admin/passwordPage', '', 'permissioncenter:system:admin:passwordPage', '4', '82', '', '11', '1', '0', '', '10001003', '2021-04-24 01:17:08.437000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('84', 0x3731656330646331663831613464343839333832343135316238326137376261, '修改密码API', '/admin/update/password', '', 'permissioncenter:system:admin:update:password', '4', '82', '', '11', '1', '0', '', '10001003', '2021-04-24 01:17:49.774000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('85', 0x3934393064343038313062663437616461313265393733343735366439623638, '跳转我的密码界面API', '/admin/myPasswordPage', '', 'permissioncenter:system:mypassword', '4', '86', '', '11', '1', '0', '', '10001003', '2021-04-24 01:19:59.204000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:21:00.636000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('86', 0x6434386135613764363132633430393961366266396634623961343639633138, '我的', '/', '', 'permissioncenter:system:my', '4', '-1', '', '11', '1', '0', '', '10001003', '2021-04-24 01:20:32.120000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:20:45.077000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('87', 0x6538386434643134303664633464306238383233643032353033343964646163, '修改我的密码API', '/admin/update/mypassword', '', 'permissioncenter:system:my:update:password', '4', '86', '', '12', '1', '0', '', '10001003', '2021-04-24 01:21:57.549000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('88', 0x3030386166386239663133323438323761313831643033373130626161393263, '缓存管理', '/', '', 'usercenter:cache', '1', '59', '', '1', '1', '0', '', '10001002', '2021-04-21 22:24:38.975000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 22:25:12.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('89', 0x3835623738313833646461643432396338633864336164643065373735613335, '用户列表', '/user/es/listPage', '', 'usercenter:cache:user:listpage', '1', '88', '', '1', '1', '0', '', '10001002', '2021-04-21 22:25:32.910000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('90', 0x3563333338636132356538333430663038663065343430303461366336656539, '查询列表API', '/user/es/list', '', 'usercenter:cache:user:list', '4', '89', '', '1', '1', '0', '', '10001002', '2021-04-21 22:25:32.910000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('91', 0x6366336539633231623830623431383162376135633533393933626534653166, '主界面', '/index/page', '', 'permissioncenter:index:page', '4', '-1', '', '1', '1', '0', '', '10001003', '2021-04-25 21:30:53.871000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:16:33.279000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('92', 0x3831373234336534386633613465333761343837333662396533623165613234, '菜单列表', '/index/menus', '', 'permissioncenter:index:menus', '4', '-1', '', '2', '1', '0', '', '10001003', '2021-04-25 21:31:35.941000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('93', 0x6133653662326637326534363465336162616665376439343966346135636363, '欢迎页', '/index/welcome', '', 'permissioncenter:index:welcome', '4', '-1', '', '3', '1', '0', '', '10001003', '2021-04-25 21:32:11.714000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('94', 0x3736363639393233383763313464653039636332313939643963623166633165, '查看上级功能', '/function/query/app/function/tree', '', 'permissioncenter:system:function:query:app:function:tree', '4', '12', '', '1', '1', '0', '', '10001003', '2021-04-25 21:43:45.293000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('95', 0x6235396638323736613333323466313638623730666235666635376465613433, '1', '1', '', '1', '4', '-1', '1', '1', '1', '1', '1', '10001002', '2021-04-25 21:45:08.516000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('96', 0x3138666161663635306136323431613839353432333333333631306431373131, '登录界面', '/login/page', '', 'permissioncenter:login:page', '4', '-1', '', '3', '1', '0', '', '10001003', '2021-04-27 21:17:05.762000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('97', 0x6430316539636533313439343437626261363131666239613463653839633532, '主界面', '/index/page', '', 'usercenter:index:page', '4', '-1', '', '1', '1', '0', '', '10001002', '2021-04-27 21:18:21.576000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('98', 0x3565306161313661306637363433353638333462626564363462613331643233, '菜单列表', '/index/menus', '', 'usercenter:index:menus', '4', '-1', '', '3', '1', '0', '', '10001002', '2021-04-27 21:18:54.634000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('99', 0x3034343663396163316266313433366661613839373064643137633965396435, '欢迎页', '/index/welcome', '', 'usercenter:index:welcome', '4', '-1', '', '4', '1', '0', '', '10001002', '2021-04-27 21:19:13.039000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('100', 0x3838313063653637313032313464656161363533363536336130646630343832, '登录界面', '/login/page', '', 'usercenter:login:page', '4', '-1', '', '4', '1', '0', '', '10001002', '2021-04-27 21:19:34.802000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('101', 0x3662316332333137313533613430656338323562343461373063316661643237, '我的', '', '', 'usercenter:system:my', '4', '-1', '', '2', '1', '0', '', '10001002', '2021-04-27 21:20:13.050000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('102', 0x3138633964306362366264643466376339303661386634333638323066656263, '跳转我的密码界面API', '/admin/myPasswordPage', '', 'usercenter:system:mypassword', '4', '101', '', '3', '1', '0', '', '10001002', '2021-04-27 21:20:31.313000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('103', 0x3038613561626439383365323461383862373636643166393232653563386164, '修改我的密码API', '/admin/update/mypassword', '', 'usercenter:system:my:update:password', '4', '101', '', '3', '1', '0', '', '10001002', '2021-04-27 21:20:54.030000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('104', 0x6136356564343331646334353465316162316630633031643863623261653239, '权限管理', '', '', 'usercenter:permission', '0', '-1', '', '3', '1', '0', '', '10001002', '2021-04-27 21:21:53.088000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:43:31.736000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('105', 0x3761656434393433663430623463633762656133643230623732633636326130, '系统管理', '', '', 'usercenter:permission:system', '1', '104', '', '3', '1', '0', '', '10001002', '2021-04-27 21:23:26.612000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('106', 0x6334386432383538396636393436306238656632353033623362343235623330, '账号列表', '/admin/listPage', '', 'usercenter:permission:system:admin', '0', '105', '', '1', '1', '0', '', '10001002', '2021-04-27 21:24:56.026000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('107', 0x3730653433636639313733303433306439646365303838393232323333616137, '编辑', '', '<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"edit\">编辑</a> &nbsp;&nbsp;', 'usercenter:permission:system:admin:updateRowBtn', '2', '106', '', '1', '1', '0', '', '10001002', '2021-04-27 21:25:22.916000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('108', 0x3839663533393435653632303437383839613031333432313162623931623532, '跳转编辑界面API', '/admin/editPage', '', 'usercenter:permission:system:admin:editPage', '4', '107', '', '1', '1', '0', '', '10001002', '2021-04-27 21:25:46.751000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('109', 0x6332653831356232633337623433383661663761353933623831663438653063, '编辑API', '/admin/update', '', 'usercenter:permission:system:admin:update', '4', '107', '', '1', '1', '0', '', '10001002', '2021-04-27 21:26:06.537000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('110', 0x3930383131333563313963653432313162623264383134643464333066663339, '删除', '', '<a class=\"layui-btn layui-btn-xs layui-btn-danger data-count-delete\" lay-event=\"delete\">删除</a> &nbsp;&nbsp;', 'usercenter:permission:system:admin:deleteRowBtn', '2', '106', '', '1', '1', '0', '', '10001002', '2021-04-27 21:26:28.647000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('111', 0x3564663863346163313434323438303039333263633033386636363438633662, '删除API', '/admin/delete', '', 'usercenter:permission:system:admin:delete', '4', '110', '', '1', '1', '0', '', '10001002', '2021-04-27 21:26:50.327000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('112', 0x6332626339393164336531343436336238303036313961333230666236663432, '角色', '', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"role\">角色</a> &nbsp;&nbsp;', 'usercenter:permission:system:admin:roleRowBtn', '2', '106', '', '1', '1', '0', '', '10001002', '2021-04-27 21:27:10.562000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('113', 0x3736323566326235346262383430656538313866343431346638636361353033, '查看角色树', '/role/query/admin/role/tree', '', 'usercenter:permission:system:admin:tree:list', '4', '112', '', '1', '1', '0', '', '10001002', '2021-04-27 21:31:16.380000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('114', 0x3361626461323432366538353439633562363664653831363939353363633230, '保存角色关联', '/admin/connect/roles', '', 'usercenter:permission:system:admin:connect:roles', '4', '112', '', '1', '1', '0', '', '10001002', '2021-04-27 21:31:38.095000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('115', 0x6638373131643630336461643433323762643964363664633938636431663461, '查看列表API', '/admin/list', '', 'usercenter:permission:system:admin:list', '4', '106', '', '3', '1', '0', '', '10001002', '2021-04-27 21:32:00.438000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('116', 0x6430643561343763663861613432656561393164353335346537616461633164, '添加', '', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'usercenter:permission:system:admin:addToolbarBtn', '3', '106', '', '1', '1', '0', '工具条按钮', '10001002', '2021-04-27 21:32:47.301000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('117', 0x6263333539383130643764383462376638393166313031393332396333643663, '跳转保存界面API', '/admin/addPage', '', 'usercenter:permission:system:admin:addPage', '4', '116', '', '1', '1', '0', '', '10001002', '2021-04-27 21:33:48.847000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('118', 0x3332383565643333333862623431633862366232383434633962646136333865, '保存API', '/admin/save', '', 'usercenter:permission:system:admin:save', '4', '116', '', '1', '1', '0', '', '10001002', '2021-04-27 21:34:07.859000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('119', 0x3739313232363831336633663435363262383764333161313030303034353130, '删除', '', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'usercenter:permission:system:admin:deleteToolbarBtn', '3', '106', '', '3', '1', '0', '', '10001002', '2021-04-27 21:34:45.728000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('120', 0x3537386237343238336633303431623538303733373065353563383466303762, '批量删除API', '/admin/delete/ids', '', 'usercenter:permission:system:admin:deletes', '4', '119', '', '3', '1', '0', '', '10001002', '2021-04-27 21:35:05.702000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('121', 0x3631323635663235383433643436393962386232313934313661336366306237, '修改密码', '', '<a class=\"layui-btn layui-btn-normal layui-btn-xs \" lay-event=\"password\">修改密码</a>', 'usercenter:permission:system:admin:passwordRowBtn', '2', '106', '', '3', '1', '0', '', '10001002', '2021-04-27 21:35:37.139000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('122', 0x6133633639363535616561613463646139366638336663336364383835343033, '跳转修改密码界面API', '/admin/passwordPage', '', 'usercenter:permission:system:admin:passwordPage', '4', '121', '', '3', '1', '0', '', '10001002', '2021-04-27 21:36:01.961000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('123', 0x3136633435663539636463393434383338623734626230616339636361626134, '修改密码API', '/admin/update/password', '', 'usercenter:permission:system:admin:update:password', '4', '121', '', '3', '1', '0', '', '10001002', '2021-04-27 21:36:23.499000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('124', 0x6162376131383639623137613435376539663363373063373534366266653934, '角色列表', '/role/listPage', '', 'usercenter:permission:system:role', '1', '105', '', '3', '1', '0', '', '10001002', '2021-04-27 21:37:30.346000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('125', 0x3934383233616430656662303431366539366531306261336663316134396133, '编辑', '', '<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"edit\">编辑</a> &nbsp;&nbsp;', 'usercenter:permission:system:role:updateRowBtn', '2', '124', '', '3', '1', '0', '', '10001002', '2021-04-27 21:37:54.753000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('126', 0x3565666334613464633831333465393562383831333832363132306535373265, '编辑API', '/role/update', '', 'usercenter:permission:system:role:update', '4', '125', '', '4', '1', '0', '', '10001002', '2021-04-27 21:38:14.968000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('127', 0x3432636438643634326536363436653962636139643039363866316366336431, '跳转编辑界面API', '/role/editPage', '', 'usercenter:permission:system:role:editPage', '4', '125', '', '4', '1', '0', '', '10001002', '2021-04-27 21:38:35.928000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('128', 0x3232353638373131643738643431356162616136393163646636663835316638, '删除', '', '<a class=\"layui-btn layui-btn-xs layui-btn-danger data-count-delete\" lay-event=\"delete\">删除</a> &nbsp;&nbsp;', 'usercenter:permission:system:role:deleteRowBtn', '2', '124', '', '3', '1', '0', '', '10001002', '2021-04-27 21:38:57.654000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('129', 0x3837616135653239393262653464383262393231656666623539343462386464, '删除API', '/role/delete', '', 'usercenter:permission:system:role:delete', '4', '128', '', '3', '1', '0', '', '10001002', '2021-04-27 21:39:18.490000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('130', 0x6634636665643532363936643437616362623864373131366565383430643234, '权限', '', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"permission\">权限</a>', 'usercenter:permission:system:role:permissionRowBtn', '2', '124', '', '5', '1', '0', '', '10001002', '2021-04-27 21:39:37.980000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('131', 0x6333323732343265663633643461356162653264323563666464653133373264, '查看权限树API', '/function/query/role/function/tree', '', 'usercenter:permission:system:role:function:tree', '4', '130', '', '3', '1', '0', '', '10001002', '2021-04-27 21:40:00.895000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('132', 0x3834396634363537366639623431306261306463633737346262656561393338, '保存权限树API', '/role/connect/functions', '', 'usercenter:permission:system:role:connect:functions', '4', '130', '', '4', '1', '0', '', '10001002', '2021-04-27 21:40:25.848000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('133', 0x3964336639306361353132343439343061376466313736353661376631396263, '查看列表API', '/role/list', '', 'usercenter:permission:system:role:list', '4', '124', '', '4', '1', '0', '', '10001002', '2021-04-27 21:40:47.119000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('134', 0x3234353632303264643837633431353961386464353035643634303134633732, '添加', '', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'usercenter:permission:system:role:addToolbarBtn', '3', '124', '', '5', '1', '0', '', '10001002', '2021-04-27 21:41:04.952000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('135', 0x6562333431373333336239393434323861383965303232616361663539313763, '跳转保存界面API', '/role/addPage', '', 'usercenter:permission:system:role:addPage', '4', '134', '', '5', '1', '0', '', '10001002', '2021-04-27 21:41:23.409000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('136', 0x6232396534346636396463353435646162623835326230623930656462323737, '保存API', '/role/save', '', 'usercenter:permission:system:role:save', '4', '134', '', '6', '1', '0', '', '10001002', '2021-04-27 21:41:45.572000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('137', 0x3136333736333030626363633462626561383835613633643864613564366437, '删除', '', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'usercenter:permission:system:role:deleteToolbarBtn', '3', '124', '', '7', '1', '0', '', '10001002', '2021-04-27 21:42:04.772000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('138', 0x3366666336343564333565363463623439393131333831653038326537303562, '批量删除API', '/role/delete/ids', '', 'usercenter:permission:system:role:deletes', '4', '137', '', '6', '1', '0', '', '10001002', '2021-04-27 21:42:23.744000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('139', 0x3763653362363838646362363463666561366461396565353932656531356638, '功能项列表', '/function/listPage', '', 'usercenter:permission:system:function', '1', '105', '', '6', '1', '0', '', '10001002', '2021-04-27 21:43:19.408000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:44:18.951000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('140', 0x3262383432313761396365613438373261336661653539613163656637626230, '编辑', '', '<a class=\"layui-btn layui-btn-normal layui-btn-xs data-count-edit\" lay-event=\"edit\">编辑</a> &nbsp;&nbsp;', 'usercenter:permission:system:function:updateRowBtn', '2', '139', '', '1', '1', '0', '', '10001002', '2021-04-27 21:44:42.266000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('141', 0x6232366139383161333139613463616539346363663365383961333030353265, '修改API', '/function/update', '', 'usercenter:permission:system:function:update', '4', '140', '', '1', '1', '0', '', '10001002', '2021-04-27 21:45:00.861000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('142', 0x3462616337336334646137643438656262626234356331376630303433393664, '跳转编辑界面API', '/function/editPage', '', 'usercenter:permission:system:function:editPage', '4', '140', '', '5', '1', '0', '', '10001002', '2021-04-27 21:47:30.839000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('143', 0x6335346362303530346132393438313662663236333533306463613231633565, '删除', '', '<a class=\"layui-btn layui-btn-xs layui-btn-danger data-count-delete\" lay-event=\"delete\">删除</a> &nbsp;&nbsp;', 'usercenter:permission:system:function:deleteRowBtn', '2', '139', '', '3', '1', '0', '', '10001002', '2021-04-27 21:47:47.953000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('144', 0x6462383830343464373935363465306562663961373166373334383436333231, '删除API', '/function/delete', '', 'usercenter:permission:system:function:delete', '4', '143', '', '3', '1', '0', '', '10001002', '2021-04-27 21:48:18.166000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('145', 0x3835303336633634383062343464626162653834366266653564633363393737, '查看列表API', '/function/tree/table', '', 'usercenter:permission:system:function:tree', '4', '139', '', '4', '1', '0', '', '10001002', '2021-04-27 21:48:38.017000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('146', 0x6365316464393631613339333433306561396366376438303566656361653661, '查询角色功能树API', '/function/query/role/function/tree', '', 'usercenter:permission:system:function:role:function:tree', '4', '124', '', '3', '1', '0', '', '10001002', '2021-04-27 21:48:59.240000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 21:52:36.769000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('147', 0x3662626237363065376364613466356438393462393138653363303837613866, '添加', '', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'usercenter:permission:system:function:addToolbarBtn', '3', '139', '', '3', '1', '0', '', '10001002', '2021-04-27 21:49:17.598000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('148', 0x6637633362373636323363343463386361633332316635353138613761386231, '保存API', '/function/save', '', 'usercenter:permission:system:function:save', '4', '147', '', '4', '1', '0', '', '10001002', '2021-04-27 21:49:36.468000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('149', 0x6136333735313361313164383463366438366539626364653732633231656337, '跳转保存界面API', '/function/addPage', '', 'usercenter:permission:system:function:addPage', '4', '147', '', '3', '1', '0', '', '10001002', '2021-04-27 21:50:00.045000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('150', 0x3830653837373933306362343434346438616365626437326534313831363931, '删除', '', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'usercenter:permission:system:function:deleteToolbarBtn', '3', '139', '', '5', '1', '0', '', '10001002', '2021-04-27 21:50:23.407000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('151', 0x3638353366306231623937653434313762346463316166383562653461653839, '批量删除API', '/function/delete/ids', '', 'usercenter:permission:system:function:deletes', '4', '150', '', '5', '1', '0', '', '10001002', '2021-04-27 21:50:44.696000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('152', 0x3333306264616131343535333462653561646465613439393136636538353137, '查看上级功能', '/function/query/app/function/tree', '', 'usercenter:permission:system:function:query:app:function:tree', '4', '139', '', '5', '1', '0', '', '10001002', '2021-04-27 21:51:27.455000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('153', 0x3630346538323862396663343464643638656561613864333836663432643339, '1', '', '', '1', '4', '141', '', '1', '1', '0', '1', '10001002', '2021-04-27 22:00:28.752000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('154', 0x6363393931393138383831633435333338663733666233363534376139323062, '11', '1', '', '1', '0', '153', '', '1', '1', '0', '', '10001002', '2021-04-27 22:02:35.041000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('155', 0x6164336133323637323332383434613062373038303030613761316130323064, '111', '11', '1', '1', '4', '154', '', '1', '1', '0', '1', '10001002', '2021-04-27 22:04:36.282000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('156', 0x3431633130333830656666393464376562383139323236313839336339336339, '123', '123', '1', '1', '4', '155', '1', '1', '1', '0', '', '10001002', '2021-04-27 22:05:26.882000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('157', 0x3837363339633031333039313465623139343162663562363933633930626536, '1', '1', '1', '1', '0', '156', '', '11', '1', '0', '', '10001002', '2021-04-27 22:05:43.165000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('158', 0x3031303335656530616464373461363661333937323130613165376461343161, '1', '1', '', '1', '4', '157', '', '123', '1', '0', '123', '10001002', '2021-04-27 22:06:14.172000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('159', 0x3532303238623439636634353432343962396265346639373561386231323334, '123', '123', '123', '123', '4', '158', '', '1', '1', '0', '', '10001002', '2021-04-27 22:06:46.996000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('160', 0x6234326336313165663335383466313638646238366161613365363236626335, '1', '1', '1', '1', '4', '159', '', '1', '1', '0', '', '10001002', '2021-04-27 22:07:15.889000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('161', 0x3265636639643566356163333430303339393765626366306465353163376463, '1', '1', '1', '1', '4', '160', '', '11', '1', '0', '', '10001002', '2021-04-27 22:08:11.323000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('162', 0x3364616430633563306332323465666539373062303765623462346566393235, '1', '1', '1', '1', '4', '161', '', '1', '1', '0', '', '10001002', '2021-04-27 22:08:39.778000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('163', 0x6435383164303330386330613462376538306661363632303466303430303265, '1', '1', '1', '1', '4', '162', '', '1', '1', '0', '', '10001002', '2021-04-27 22:09:07.844000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('164', 0x6561633663353664623565343462613238636639666261333938346532363261, '1', '1', '1', '1', '4', '163', '', '1', '1', '0', '', '10001002', '2021-04-27 22:10:06.335000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('165', 0x3163663536333766386339633436303538353862646134613131613064633233, '11', '1', '1', '1', '4', '164', '', '1', '1', '0', '', '10001002', '2021-04-27 22:11:14.042000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('166', 0x6163363730643432373264663439386662343732656465313365376533643032, '应用列表', '', '\n                                            <a href=\"javascript:;\" layuimini-content-href=\"/app/listPage\" data-title=\"应用列表\" data-icon=\"fa fa-window-maximize\">\n                                                <i class=\"fa fa-window-maximize\"></i>\n                                                <cite>应用列表</cite>\n                                            </a>\n', 'permissioncenter:welcome:applist', '5', '93', '', '1', '1', '0', '', '10001003', '2021-04-28 21:02:58.821000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-28 21:14:19.959000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('167', 0x3033363936653161646535393464653839373363336231306238636433343638, '账号列表', '', '\n                                            <a href=\"javascript:;\" layuimini-content-href=\"/admin/listPage\" data-title=\"账号列表\" data-icon=\"fa fa-gears\">\n                                                <i class=\"fa fa-gears\"></i>\n                                                <cite>账号列表</cite>\n                                            </a>\n', 'permissioncenter:welcome:adminlist', '5', '93', '', '2', '1', '0', '', '10001003', '2021-04-28 21:03:47.196000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-28 21:14:34.347000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('168', 0x3131373039393433363536353462633861333833303461633634656333336132, '角色列表', '', '  <a href=\"javascript:;\" layuimini-content-href=\"/role/listPage\" data-title=\"角色列表\" data-icon=\"fa fa-file-text\">\n                                                <i class=\"fa fa-file-text\"></i>\n                                                <cite>角色列表</cite>\n                                            </a>\n                         ', 'permissioncenter:welcome:rolelist', '5', '93', '', '3', '1', '0', '', '10001003', '2021-04-28 21:04:20.173000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-28 21:13:55.236000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('169', 0x3533383235353436363030323430303239303131303930343866616663316533, '功能项列表', '', '                                            <a href=\"javascript:;\" layuimini-content-href=\"/function/listPage\" data-title=\"功能项列表\" data-icon=\"fa fa-dot-circle-o\">\n                                                <i class=\"fa fa-dot-circle-o\"></i>\n                                                <cite>功能项列表</cite>\n                                            </a>', 'permissioncenter:welcome:functionlist', '5', '93', '', '4', '1', '0', '', '10001003', '2021-04-28 21:04:55.172000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-28 21:14:09.394000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('170', 0x3736613238626636663136373436303738663533353662646533626637383461, '组织机构列表', '/orgnazition/listPage', '', 'permissioncenter:system:orgnazition:listPage', '1', '5', '', '6', '1', '0', '', '10001003', '2021-04-29 20:33:22.761000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:31:33.276000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('171', 0x6635323462306630323932313438303661393665363331643034383136323635, '查看列表API', '/orgnazition/tree/table', '', 'permissioncenter:system:orgnazition:list', '4', '170', '', '7', '1', '0', '', '10001003', '2021-04-29 20:34:31.058000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:35:02.194000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('172', 0x6565353062396134656430633437613139623730333935333962386632363737, '添加', '', '<button class=\"layui-btn layui-btn-normal layui-btn-sm data-add-btn\" lay-event=\"add\"> 添加 </button>', 'permissioncenter:system:orgnazition:addbtn', '3', '170', '', '1', '1', '0', '', '10001003', '2021-04-29 20:35:21.659000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:33:43.290000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('173', 0x3830393431636361653732623463323238373264336439396532363135366532, '跳转添加界面API', '/orgnazition/addPage', '', 'permissioncenter:system:orgnazition:addpage', '4', '172', '', '1', '1', '0', '', '10001003', '2021-04-29 20:36:06.659000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:34:04.775000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('174', 0x3834313232353364323736633462306261303736613766343332383934646238, '保存API', '/orgnazition/save', '', 'permissioncenter:system:orgnazition:save', '4', '172', '', '1', '1', '0', '', '10001003', '2021-04-29 20:36:53.065000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:34:32.686000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('175', 0x3339386536313930613361653465383661633037386139366239376430363465, '查看组织机构树', '/orgnazition/query/tree', '', 'permissioncenter:system:orgnazition:tree', '4', '170', '', '1', '1', '0', '', '10001003', '2021-04-29 20:50:23.594000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:35:23.090000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('176', 0x6663343834363031346465393432333362373864333065306162366330343965, '删除', '', '<button class=\"layui-btn layui-btn-sm layui-btn-danger data-delete-btn\" lay-event=\"delete\">删除</button>', 'permissioncenter:system:orgnazition:delbtn', '3', '170', '', '3', '1', '0', '', '10001003', '2021-04-29 21:20:57.360000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:35:44.938000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('177', 0x6339623131333230656333383437343662313262313938303339636233656130, '删除API', '/orgnazition/delete', '', 'permissioncenter:system:orgnazition:delete', '4', '176', '', '1', '1', '0', '', '10001003', '2021-04-29 21:22:47.157000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('178', 0x6530626366343239376261313436376362346234343666653461393934633166, '编辑', '', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"edit\">修改</a> &nbsp;&nbsp;', 'permissioncenter:system:orgnazition:updateRowBtn', '2', '170', '', '6', '1', '0', '', '10001003', '2021-04-29 21:23:39.816000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:24:04.449000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('179', 0x3335386437336531366665613433636162333034326366366636383864353033, '修改API', '/orgnazition/update', '', 'permissioncenter:system:orgnazition:update', '4', '178', '', '7', '1', '0', '', '10001003', '2021-04-29 21:26:48.717000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('180', 0x3331363066396264326538343430353538613862306239613366623665313431, '跳转编辑界面API', '/orgnazition/editPage', '', 'permissioncenter:system:orgnazition:editPage', '4', '178', '', '8', '1', '0', '', '10001003', '2021-04-29 21:27:24.609000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('181', 0x3332386536333639356337343465623262383231336335643030303466656631, '删除', '', '<a class=\"layui-btn layui-btn-danger layui-btn-xs\" lay-event=\"delete\">删除</a>', 'permissioncenter:system:orgnazition:deleteRowBtn', '2', '170', '', '2', '1', '0', '', '10001003', '2021-04-29 21:28:45.539000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:29:50.254000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_function` VALUES ('182', 0x3731343831663239343863313433656438396231643661356161653239633137, '删除API', '/orgnazition/delete', '', 'permissioncenter:system:orgnazition:delete', '4', '181', '', '8', '1', '0', '', '10001003', '2021-04-29 21:29:23.147000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('183', 0x6133363666373663313133343439656661303030336236303966313061343535, '组织机构', '', '<a class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-event=\"orgnazition\">组织机构</a> &nbsp;&nbsp;', 'permissioncenter:system:admin:orgRowBtn', '2', '10', '', '8', '1', '0', '', '10001003', '2021-04-30 20:57:26.450000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('184', 0x3831313538666566643237353431623339303737333639653031313835366662, '跳转选择组织机构界面API', '/admin/selectOrgnazitionPage', '', 'permissioncenter:system:admin:selectOrgnazitionPage', '4', '183', '', '10', '1', '0', '', '10001003', '2021-04-30 21:06:46.529000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_function` VALUES ('185', 0x6666626338353865613763623464396538633937383864363765323666353162, '查询组织机构树API', '/orgnazition/query/admin/orgnazition/tree', '', 'permissioncenter:system:admin:orgnazition:tree', '4', '183', '', '10', '1', '0', '', '10001003', '2021-04-30 22:04:02.952000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);

-- ----------------------------
-- Table structure for t_sa_orgnazition
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_orgnazition`;
CREATE TABLE `t_sa_orgnazition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orgnazition_id` varchar(32) DEFAULT NULL COMMENT '组织机构ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单 -1表示当前是顶级',
  `orgnazition_sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='组织机构';

-- ----------------------------
-- Records of t_sa_orgnazition
-- ----------------------------
INSERT INTO `t_sa_orgnazition` VALUES ('170', '1323755008df4b4f88c62827d3eaddf2', '犀鸟数科', '-1', '1', '0', '', '2021-04-29 21:13:38.557000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-30 22:31:15.740000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition` VALUES ('171', '79be0fc5c2604e21bd007b1a98422d97', '研发中心1', '170', '1', '0', '', '2021-04-29 21:19:44.415000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('172', 'a96b0a1deafa4df4b6838d985a54ad95', '研发中心2', '170', '1', '0', '', '2021-04-29 21:19:58.747000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:42:52.950000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition` VALUES ('173', '7608f0a6a31042e4a4db47c760feecc0', '研发中心1——研发1部', '171', '1', '0', '', '2021-04-29 21:43:07.962000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:44:01.195000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition` VALUES ('174', 'c004b32491374f5da881f575a5188cff', '研发中心1——研发2部', '171', '3', '1', '', '2021-04-29 21:43:26.822000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-29 21:44:08.660000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition` VALUES ('175', 'b09bffcf41f4496c8a94d58acc8edc98', '研发中心2——研发1部', '172', '5', '0', '', '2021-04-29 21:44:27.077000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('176', '1a9d472b6df94f0e8d37caa64ea8842e', '研发中心1——测试1部', '171', '1', '0', '1', '2021-04-30 20:46:53.581000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-30 20:53:51.432000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition` VALUES ('177', 'f8866e3be0354c77b965bde9315fa061', '研发中心1——测试1部——测试1组', '173', '9', '0', '', '2021-04-30 20:54:50.040000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('178', '5be3798ed19e4b19b1419d03490d4326', '研发中心1——研发3部', '171', '3', '0', '', '2021-04-30 22:28:36.957000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('179', 'e6118636d973448ca7eee4e73a2abad3', '研发中心1——研发3部', '171', '3', '0', '', '2021-04-30 22:28:54.158000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('180', '2bf5a4ca77ea429b8265ce61929d66ee', '研发中心1——研发5部', '171', '33', '0', '', '2021-04-30 22:29:04.377000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('181', '9a17e1a7164f44bbbae112c288c0962a', '研发中心1——研发6部', '171', '3', '0', '1', '2021-04-30 22:29:19.104000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);
INSERT INTO `t_sa_orgnazition` VALUES ('182', '19b10098c4e7422da00dd19e10678d17', '研发中心1——研发8部', '171', '3', '0', '', '2021-04-30 22:29:28.017000', 'bb155acbf5ef43dcac9aa892274fadd5', null, null);

-- ----------------------------
-- Table structure for t_sa_orgnazition_app
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_orgnazition_app`;
CREATE TABLE `t_sa_orgnazition_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orgnazition_id` varchar(32) NOT NULL COMMENT '所属组织机构',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门应用关联表';

-- ----------------------------
-- Records of t_sa_orgnazition_app
-- ----------------------------
INSERT INTO `t_sa_orgnazition_app` VALUES ('1', 'a96b0a1deafa4df4b6838d985a54ad95', '10001003', '0', '2021-04-30 13:19:31.000000', '-1');
INSERT INTO `t_sa_orgnazition_app` VALUES ('2', 'a96b0a1deafa4df4b6838d985a54ad95', '10001002', '0', '2021-04-30 13:20:20.000000', '-1');
INSERT INTO `t_sa_orgnazition_app` VALUES ('3', '7608f0a6a31042e4a4db47c760feecc0', '10001003', '0', '2021-04-30 13:20:36.000000', '-1');
INSERT INTO `t_sa_orgnazition_app` VALUES ('4', '1a9d472b6df94f0e8d37caa64ea8842e', '10001002', '1', '2021-04-30 20:46:53.652000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('5', '1a9d472b6df94f0e8d37caa64ea8842e', '10001003', '1', '2021-04-30 20:46:53.695000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('6', '1a9d472b6df94f0e8d37caa64ea8842e', '10001002', '1', '2021-04-30 20:53:43.983000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('7', '1a9d472b6df94f0e8d37caa64ea8842e', '10001002', '0', '2021-04-30 20:53:51.524000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('8', '1a9d472b6df94f0e8d37caa64ea8842e', '10001003', '0', '2021-04-30 20:53:51.572000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('9', 'f8866e3be0354c77b965bde9315fa061', '10001002', '0', '2021-04-30 20:54:50.090000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('10', 'f8866e3be0354c77b965bde9315fa061', '10001003', '0', '2021-04-30 20:54:50.140000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('11', '5be3798ed19e4b19b1419d03490d4326', '10001002', '0', '2021-04-30 22:28:37.025000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('12', '5be3798ed19e4b19b1419d03490d4326', '10001003', '0', '2021-04-30 22:28:37.079000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('13', 'e6118636d973448ca7eee4e73a2abad3', '10001002', '0', '2021-04-30 22:28:54.197000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('14', 'e6118636d973448ca7eee4e73a2abad3', '10001003', '0', '2021-04-30 22:28:54.238000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('15', '2bf5a4ca77ea429b8265ce61929d66ee', '10001002', '0', '2021-04-30 22:29:04.420000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('16', '2bf5a4ca77ea429b8265ce61929d66ee', '10001003', '0', '2021-04-30 22:29:04.475000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('17', '9a17e1a7164f44bbbae112c288c0962a', '10001002', '0', '2021-04-30 22:29:19.150000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('18', '9a17e1a7164f44bbbae112c288c0962a', '10001003', '0', '2021-04-30 22:29:19.216000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('19', '19b10098c4e7422da00dd19e10678d17', '10001002', '0', '2021-04-30 22:29:28.057000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('20', '19b10098c4e7422da00dd19e10678d17', '10001003', '0', '2021-04-30 22:29:28.099000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('21', '1323755008df4b4f88c62827d3eaddf2', '10001002', '0', '2021-04-30 22:31:15.835000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_orgnazition_app` VALUES ('22', '1323755008df4b4f88c62827d3eaddf2', '10001003', '0', '2021-04-30 22:31:15.869000', 'bb155acbf5ef43dcac9aa892274fadd5');

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

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
INSERT INTO `t_sa_role` VALUES ('11', '部门负责人', 'f0349fe34ce34a9780033354cc27d335', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 23:37:54.419000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-21 23:40:23.350000');
INSERT INTO `t_sa_role` VALUES ('12', '账号管理员', '7943ff9283ba4572bdad44838cc29052', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-22 22:34:27.579000', null, null);
INSERT INTO `t_sa_role` VALUES ('13', '应用管理员', '922739c7dc844aeb90d0e79118ffa642', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 21:48:34.017000', null, null);
INSERT INTO `t_sa_role` VALUES ('14', '账号管理员', 'c0c19231b9c44aeba849545297986744', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-23 23:09:08.882000', null, null);
INSERT INTO `t_sa_role` VALUES ('15', '应用管理员(只允许添加)', 'f033f6356c2449bdb34d657fa4720ffa', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-24 01:25:02.739000', null, null);
INSERT INTO `t_sa_role` VALUES ('16', '管理员02', 'd50a8420dcba444a818b2804416aeca9', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-26 21:20:54.898000', null, null);
INSERT INTO `t_sa_role` VALUES ('17', '管理员03', '0d726135121f4fe49569b533f2dae1f3', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-26 21:37:57.276000', null, null);
INSERT INTO `t_sa_role` VALUES ('18', '管理员04', 'ab952bea768e4d4d9c8e2b30220e4348', '1', '10001003', '1', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-26 22:00:59.991000', null, null);
INSERT INTO `t_sa_role` VALUES ('19', '管理员04', '520fccd50efa484e9ab2f87cddd22283', '1', '10001002', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-26 22:10:09.532000', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-26 22:29:27.525000');
INSERT INTO `t_sa_role` VALUES ('20', '管理员04', '142a30e4210c48a0966e84bf1e22e815', '1', '10001003', '0', '', 'f70a3f2d8ccc46cf8636259383b4849b', '2021-04-26 22:33:05.046000', null, null);
INSERT INTO `t_sa_role` VALUES ('21', '超级管理员', '89d3bf8fbf4543188a64fb7738a6bc62', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-27 23:18:05.506000', null, null);
INSERT INTO `t_sa_role` VALUES ('22', '侯涛', '572bc044d1e24be19faa6a2235043720', '1', '10001003', '0', '', 'bb155acbf5ef43dcac9aa892274fadd5', '2021-04-28 11:59:15.485000', null, null);

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
) ENGINE=InnoDB AUTO_INCREMENT=5430 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of t_sa_role_function
-- ----------------------------
INSERT INTO `t_sa_role_function` VALUES ('315', 'f0349fe34ce34a9780033354cc27d335', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-22 21:08:51.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('316', 'f0349fe34ce34a9780033354cc27d335', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-22 21:08:51.151000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('317', 'f0349fe34ce34a9780033354cc27d335', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-22 21:08:51.190000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('318', 'f0349fe34ce34a9780033354cc27d335', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-22 21:08:51.226000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('319', 'f0349fe34ce34a9780033354cc27d335', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-22 21:08:51.271000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('320', 'f0349fe34ce34a9780033354cc27d335', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-22 21:08:51.848000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('321', 'f0349fe34ce34a9780033354cc27d335', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-22 21:08:51.886000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('322', 'f0349fe34ce34a9780033354cc27d335', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-22 21:08:51.940000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('323', 'f0349fe34ce34a9780033354cc27d335', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-22 21:08:51.981000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('324', 'f0349fe34ce34a9780033354cc27d335', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-22 21:08:52.022000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('325', 'f0349fe34ce34a9780033354cc27d335', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-22 21:08:52.061000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('326', 'f0349fe34ce34a9780033354cc27d335', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-22 21:08:52.114000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('327', 'f0349fe34ce34a9780033354cc27d335', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-22 21:08:52.158000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('328', 'f0349fe34ce34a9780033354cc27d335', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-22 21:08:52.200000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('329', 'f0349fe34ce34a9780033354cc27d335', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-22 21:08:52.245000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('330', 'f0349fe34ce34a9780033354cc27d335', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-22 21:08:52.285000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('331', 'f0349fe34ce34a9780033354cc27d335', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-22 21:08:52.327000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('332', 'f0349fe34ce34a9780033354cc27d335', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-22 21:08:52.364000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('333', 'f0349fe34ce34a9780033354cc27d335', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-22 21:08:52.406000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('334', 'f0349fe34ce34a9780033354cc27d335', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-22 21:08:52.444000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('335', 'f0349fe34ce34a9780033354cc27d335', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-22 21:08:52.485000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('336', 'f0349fe34ce34a9780033354cc27d335', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-22 21:08:52.536000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('337', 'f0349fe34ce34a9780033354cc27d335', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-22 21:08:52.578000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('338', 'f0349fe34ce34a9780033354cc27d335', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-22 21:08:52.618000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('339', 'f0349fe34ce34a9780033354cc27d335', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-22 21:08:52.666000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('340', 'f0349fe34ce34a9780033354cc27d335', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-22 21:08:52.704000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('341', 'f0349fe34ce34a9780033354cc27d335', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-22 21:08:52.751000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('342', 'f0349fe34ce34a9780033354cc27d335', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-22 21:08:52.808000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('343', 'f0349fe34ce34a9780033354cc27d335', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-22 21:08:52.873000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('344', 'f0349fe34ce34a9780033354cc27d335', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-22 21:08:52.911000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('345', 'f0349fe34ce34a9780033354cc27d335', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-22 21:08:52.954000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('346', 'f0349fe34ce34a9780033354cc27d335', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-22 21:08:52.989000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('347', 'f0349fe34ce34a9780033354cc27d335', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-22 21:08:53.039000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('348', 'f0349fe34ce34a9780033354cc27d335', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-22 21:08:53.283000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('349', 'f0349fe34ce34a9780033354cc27d335', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-22 21:08:53.346000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('350', 'f0349fe34ce34a9780033354cc27d335', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-22 21:08:53.383000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('351', 'f0349fe34ce34a9780033354cc27d335', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-22 21:08:53.418000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('352', 'f0349fe34ce34a9780033354cc27d335', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-22 21:08:53.468000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('353', 'f0349fe34ce34a9780033354cc27d335', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-22 21:08:53.509000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('354', 'f0349fe34ce34a9780033354cc27d335', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-22 21:08:53.554000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('355', 'f0349fe34ce34a9780033354cc27d335', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-22 21:08:53.596000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('356', 'f0349fe34ce34a9780033354cc27d335', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-22 21:08:53.645000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('357', 'f0349fe34ce34a9780033354cc27d335', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-22 21:08:53.687000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('358', '7943ff9283ba4572bdad44838cc29052', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-22 22:35:03.851000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('359', '7943ff9283ba4572bdad44838cc29052', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-22 22:35:03.909000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('360', '7943ff9283ba4572bdad44838cc29052', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-22 22:35:03.958000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('364', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', 'b9b2b01611fc4f9085efdd306a9faa82', '1', '10001002', '2021-04-23 21:25:23.332000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('365', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', 'f1b278bc2e08423a8c0bcfb885b0812e', '1', '10001002', '2021-04-23 21:25:23.527000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('366', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '3c722f8c487e4bc3aaaaee03868ff089', '1', '10001002', '2021-04-23 21:25:23.571000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('367', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '8a9161a27f114e338f08e17b36147c85', '1', '10001002', '2021-04-23 21:25:23.613000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('368', '922739c7dc844aeb90d0e79118ffa642', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-23 21:49:04.853000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('369', '922739c7dc844aeb90d0e79118ffa642', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-23 21:49:04.893000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('370', '922739c7dc844aeb90d0e79118ffa642', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-23 21:49:04.934000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('371', '922739c7dc844aeb90d0e79118ffa642', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-23 21:49:04.972000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('372', '922739c7dc844aeb90d0e79118ffa642', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-23 21:49:05.011000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('373', '922739c7dc844aeb90d0e79118ffa642', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-23 21:49:05.044000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('374', '922739c7dc844aeb90d0e79118ffa642', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-23 21:49:05.081000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('375', '922739c7dc844aeb90d0e79118ffa642', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-23 21:49:05.140000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('376', '922739c7dc844aeb90d0e79118ffa642', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-23 21:49:05.193000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('377', '922739c7dc844aeb90d0e79118ffa642', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-23 21:49:05.243000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('378', '922739c7dc844aeb90d0e79118ffa642', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-23 21:49:05.283000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('379', '922739c7dc844aeb90d0e79118ffa642', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-23 21:49:05.324000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('380', '922739c7dc844aeb90d0e79118ffa642', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-23 21:49:05.364000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('381', '922739c7dc844aeb90d0e79118ffa642', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-23 21:49:05.398000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('382', 'c0c19231b9c44aeba849545297986744', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-23 23:09:24.981000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('383', 'c0c19231b9c44aeba849545297986744', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-23 23:09:25.027000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('384', 'c0c19231b9c44aeba849545297986744', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-23 23:09:25.073000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('385', 'c0c19231b9c44aeba849545297986744', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-23 23:09:25.123000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('386', 'c0c19231b9c44aeba849545297986744', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-23 23:09:25.169000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('387', 'c0c19231b9c44aeba849545297986744', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-23 23:09:25.216000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('388', 'c0c19231b9c44aeba849545297986744', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-23 23:09:25.260000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('389', 'c0c19231b9c44aeba849545297986744', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-23 23:09:25.308000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('390', 'c0c19231b9c44aeba849545297986744', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-23 23:09:25.352000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('391', 'c0c19231b9c44aeba849545297986744', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-23 23:09:25.409000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('392', 'c0c19231b9c44aeba849545297986744', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-23 23:09:25.457000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('393', 'c0c19231b9c44aeba849545297986744', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-23 23:09:25.502000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('394', 'c0c19231b9c44aeba849545297986744', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-23 23:09:25.549000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('395', 'c0c19231b9c44aeba849545297986744', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-23 23:09:25.599000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('396', 'c0c19231b9c44aeba849545297986744', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-23 23:09:25.652000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('397', 'c0c19231b9c44aeba849545297986744', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-23 23:09:25.694000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('398', 'c0c19231b9c44aeba849545297986744', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-23 23:09:25.747000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('599', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-25 21:45:41.543000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('600', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-25 21:45:41.587000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('601', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-25 21:45:41.624000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('602', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-25 21:45:41.666000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('603', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-25 21:45:41.707000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('604', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-25 21:45:41.746000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('605', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-25 21:45:41.789000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('606', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-25 21:45:41.826000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('607', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-25 21:45:41.875000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('608', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-25 21:45:41.918000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('609', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-25 21:45:41.954000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('610', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-25 21:45:41.993000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('611', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-25 21:45:42.035000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('612', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-25 21:45:42.075000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('613', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-25 21:45:42.114000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('614', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-25 21:45:42.161000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('615', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-25 21:45:42.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('616', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-25 21:45:42.251000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('617', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-25 21:45:42.289000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('618', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-25 21:45:42.334000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('619', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-25 21:45:42.376000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('620', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-25 21:45:42.414000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('621', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-25 21:45:42.454000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('622', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-25 21:45:42.498000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('623', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-25 21:45:42.538000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('624', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-25 21:45:42.581000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('625', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-25 21:45:42.619000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('626', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-25 21:45:42.657000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('627', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-25 21:45:42.701000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('628', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-25 21:45:42.750000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('629', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-25 21:45:42.792000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('630', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-25 21:45:42.833000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('631', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-25 21:45:42.879000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('632', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-25 21:45:42.920000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('633', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-25 21:45:42.960000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('634', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-25 21:45:43.002000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('635', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-25 21:45:43.040000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('636', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-25 21:45:43.080000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('637', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-25 21:45:43.122000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('638', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-25 21:45:43.169000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('639', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-25 21:45:43.209000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('640', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-25 21:45:43.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('641', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-25 21:45:43.291000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('642', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-25 21:45:43.331000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('643', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-25 21:45:43.370000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('644', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-25 21:45:43.413000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('645', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-25 21:45:43.453000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('646', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-25 21:45:43.509000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('647', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-25 21:45:43.552000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('648', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-25 21:45:43.595000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('649', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-25 21:45:43.640000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('650', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-25 21:45:43.679000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('651', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-25 21:45:43.727000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('652', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-25 21:45:43.776000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('653', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-25 21:45:43.824000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('654', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-25 21:45:43.867000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('655', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-25 21:45:43.918000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('656', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-25 21:45:43.959000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('657', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-25 21:45:44.000000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('658', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-25 21:45:44.039000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('659', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-25 21:45:44.083000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('660', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-25 21:45:44.124000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('661', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-25 21:45:44.173000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('662', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-25 21:45:44.217000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('663', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-25 21:45:44.263000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('664', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-25 21:45:44.302000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('665', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-25 21:45:44.344000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2322', 'd50a8420dcba444a818b2804416aeca9', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2323', 'd50a8420dcba444a818b2804416aeca9', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2324', 'd50a8420dcba444a818b2804416aeca9', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2325', 'd50a8420dcba444a818b2804416aeca9', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2326', 'd50a8420dcba444a818b2804416aeca9', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2327', 'd50a8420dcba444a818b2804416aeca9', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2328', 'd50a8420dcba444a818b2804416aeca9', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2329', 'd50a8420dcba444a818b2804416aeca9', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2330', 'd50a8420dcba444a818b2804416aeca9', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2331', 'd50a8420dcba444a818b2804416aeca9', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2332', 'd50a8420dcba444a818b2804416aeca9', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2333', 'd50a8420dcba444a818b2804416aeca9', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2334', 'd50a8420dcba444a818b2804416aeca9', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2335', 'd50a8420dcba444a818b2804416aeca9', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2336', 'd50a8420dcba444a818b2804416aeca9', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2337', 'd50a8420dcba444a818b2804416aeca9', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2338', 'd50a8420dcba444a818b2804416aeca9', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2339', 'd50a8420dcba444a818b2804416aeca9', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2340', 'd50a8420dcba444a818b2804416aeca9', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2341', 'd50a8420dcba444a818b2804416aeca9', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2342', 'd50a8420dcba444a818b2804416aeca9', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2343', 'd50a8420dcba444a818b2804416aeca9', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2344', 'd50a8420dcba444a818b2804416aeca9', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2345', 'd50a8420dcba444a818b2804416aeca9', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2346', 'd50a8420dcba444a818b2804416aeca9', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2347', 'd50a8420dcba444a818b2804416aeca9', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2348', 'd50a8420dcba444a818b2804416aeca9', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2349', 'd50a8420dcba444a818b2804416aeca9', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2350', 'd50a8420dcba444a818b2804416aeca9', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2351', 'd50a8420dcba444a818b2804416aeca9', 'd71f0ab22dbb49c09a987222a8d53e01', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2352', 'd50a8420dcba444a818b2804416aeca9', '69eae19b5977468b952952773078ad77', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2353', 'd50a8420dcba444a818b2804416aeca9', '71ec0dc1f81a4d4893824151b82a77ba', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2354', 'd50a8420dcba444a818b2804416aeca9', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2355', 'd50a8420dcba444a818b2804416aeca9', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2356', 'd50a8420dcba444a818b2804416aeca9', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2357', 'd50a8420dcba444a818b2804416aeca9', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2358', 'd50a8420dcba444a818b2804416aeca9', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2359', 'd50a8420dcba444a818b2804416aeca9', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2360', 'd50a8420dcba444a818b2804416aeca9', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2361', 'd50a8420dcba444a818b2804416aeca9', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2362', 'd50a8420dcba444a818b2804416aeca9', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2363', 'd50a8420dcba444a818b2804416aeca9', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2364', 'd50a8420dcba444a818b2804416aeca9', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2365', 'd50a8420dcba444a818b2804416aeca9', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2366', 'd50a8420dcba444a818b2804416aeca9', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2367', 'd50a8420dcba444a818b2804416aeca9', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2368', 'd50a8420dcba444a818b2804416aeca9', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2369', 'd50a8420dcba444a818b2804416aeca9', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2370', 'd50a8420dcba444a818b2804416aeca9', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2371', 'd50a8420dcba444a818b2804416aeca9', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2372', 'd50a8420dcba444a818b2804416aeca9', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2373', 'd50a8420dcba444a818b2804416aeca9', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2374', 'd50a8420dcba444a818b2804416aeca9', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2375', 'd50a8420dcba444a818b2804416aeca9', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2376', 'd50a8420dcba444a818b2804416aeca9', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2377', 'd50a8420dcba444a818b2804416aeca9', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2378', 'd50a8420dcba444a818b2804416aeca9', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2379', 'd50a8420dcba444a818b2804416aeca9', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2380', 'd50a8420dcba444a818b2804416aeca9', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2381', 'd50a8420dcba444a818b2804416aeca9', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2382', 'd50a8420dcba444a818b2804416aeca9', '7666992387c14de09cc2199d9cb1fc1e', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2383', 'd50a8420dcba444a818b2804416aeca9', 'd48a5a7d612c4099a6bf9f4b9a469c18', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2384', 'd50a8420dcba444a818b2804416aeca9', '9490d40810bf47ada12e9734756d9b68', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2385', 'd50a8420dcba444a818b2804416aeca9', 'e88d4d1406dc4d0b8823d0250349ddac', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2386', 'd50a8420dcba444a818b2804416aeca9', 'cf3e9c21b80b4181b7a5c53993be4e1f', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2387', 'd50a8420dcba444a818b2804416aeca9', '817243e48f3a4e37a48736b9e3b1ea24', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2388', 'd50a8420dcba444a818b2804416aeca9', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '0', '10001003', '2021-04-26 21:37:46.257000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2828', 'f033f6356c2449bdb34d657fa4720ffa', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2829', 'f033f6356c2449bdb34d657fa4720ffa', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2830', 'f033f6356c2449bdb34d657fa4720ffa', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2831', 'f033f6356c2449bdb34d657fa4720ffa', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2832', 'f033f6356c2449bdb34d657fa4720ffa', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2833', 'f033f6356c2449bdb34d657fa4720ffa', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2834', 'f033f6356c2449bdb34d657fa4720ffa', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2835', 'f033f6356c2449bdb34d657fa4720ffa', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2836', 'f033f6356c2449bdb34d657fa4720ffa', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2837', 'f033f6356c2449bdb34d657fa4720ffa', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2838', 'f033f6356c2449bdb34d657fa4720ffa', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2839', 'f033f6356c2449bdb34d657fa4720ffa', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2840', 'f033f6356c2449bdb34d657fa4720ffa', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2841', 'f033f6356c2449bdb34d657fa4720ffa', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2842', 'f033f6356c2449bdb34d657fa4720ffa', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2843', 'f033f6356c2449bdb34d657fa4720ffa', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2844', 'f033f6356c2449bdb34d657fa4720ffa', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2845', 'f033f6356c2449bdb34d657fa4720ffa', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2846', 'f033f6356c2449bdb34d657fa4720ffa', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2847', 'f033f6356c2449bdb34d657fa4720ffa', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2848', 'f033f6356c2449bdb34d657fa4720ffa', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2849', 'f033f6356c2449bdb34d657fa4720ffa', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2850', 'f033f6356c2449bdb34d657fa4720ffa', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2851', 'f033f6356c2449bdb34d657fa4720ffa', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2852', 'f033f6356c2449bdb34d657fa4720ffa', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2853', 'f033f6356c2449bdb34d657fa4720ffa', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2854', 'f033f6356c2449bdb34d657fa4720ffa', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2855', 'f033f6356c2449bdb34d657fa4720ffa', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2856', 'f033f6356c2449bdb34d657fa4720ffa', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2857', 'f033f6356c2449bdb34d657fa4720ffa', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2858', 'f033f6356c2449bdb34d657fa4720ffa', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2859', 'f033f6356c2449bdb34d657fa4720ffa', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2860', 'f033f6356c2449bdb34d657fa4720ffa', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2861', 'f033f6356c2449bdb34d657fa4720ffa', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2862', 'f033f6356c2449bdb34d657fa4720ffa', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2863', 'f033f6356c2449bdb34d657fa4720ffa', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2864', 'f033f6356c2449bdb34d657fa4720ffa', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2865', 'f033f6356c2449bdb34d657fa4720ffa', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2866', 'f033f6356c2449bdb34d657fa4720ffa', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2867', 'f033f6356c2449bdb34d657fa4720ffa', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2868', 'f033f6356c2449bdb34d657fa4720ffa', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2869', 'f033f6356c2449bdb34d657fa4720ffa', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2870', 'f033f6356c2449bdb34d657fa4720ffa', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2871', 'f033f6356c2449bdb34d657fa4720ffa', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2872', 'f033f6356c2449bdb34d657fa4720ffa', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2873', 'f033f6356c2449bdb34d657fa4720ffa', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2874', 'f033f6356c2449bdb34d657fa4720ffa', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2875', 'f033f6356c2449bdb34d657fa4720ffa', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2876', 'f033f6356c2449bdb34d657fa4720ffa', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2877', 'f033f6356c2449bdb34d657fa4720ffa', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2878', 'f033f6356c2449bdb34d657fa4720ffa', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2879', 'f033f6356c2449bdb34d657fa4720ffa', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2880', 'f033f6356c2449bdb34d657fa4720ffa', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2881', 'f033f6356c2449bdb34d657fa4720ffa', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2882', 'f033f6356c2449bdb34d657fa4720ffa', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2883', 'f033f6356c2449bdb34d657fa4720ffa', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2884', 'f033f6356c2449bdb34d657fa4720ffa', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2885', 'f033f6356c2449bdb34d657fa4720ffa', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2886', 'f033f6356c2449bdb34d657fa4720ffa', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2887', 'f033f6356c2449bdb34d657fa4720ffa', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2888', 'f033f6356c2449bdb34d657fa4720ffa', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2889', 'f033f6356c2449bdb34d657fa4720ffa', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2890', 'f033f6356c2449bdb34d657fa4720ffa', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2891', 'f033f6356c2449bdb34d657fa4720ffa', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2892', 'f033f6356c2449bdb34d657fa4720ffa', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2893', 'f033f6356c2449bdb34d657fa4720ffa', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('2894', 'f033f6356c2449bdb34d657fa4720ffa', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-26 21:58:21.296000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3040', '0d726135121f4fe49569b533f2dae1f3', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3041', '0d726135121f4fe49569b533f2dae1f3', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3042', '0d726135121f4fe49569b533f2dae1f3', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3043', '0d726135121f4fe49569b533f2dae1f3', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3044', '0d726135121f4fe49569b533f2dae1f3', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3045', '0d726135121f4fe49569b533f2dae1f3', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3046', '0d726135121f4fe49569b533f2dae1f3', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3047', '0d726135121f4fe49569b533f2dae1f3', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3048', '0d726135121f4fe49569b533f2dae1f3', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3049', '0d726135121f4fe49569b533f2dae1f3', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3050', '0d726135121f4fe49569b533f2dae1f3', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3051', '0d726135121f4fe49569b533f2dae1f3', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3052', '0d726135121f4fe49569b533f2dae1f3', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3053', '0d726135121f4fe49569b533f2dae1f3', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3054', '0d726135121f4fe49569b533f2dae1f3', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3055', '0d726135121f4fe49569b533f2dae1f3', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3056', '0d726135121f4fe49569b533f2dae1f3', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3057', '0d726135121f4fe49569b533f2dae1f3', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3058', '0d726135121f4fe49569b533f2dae1f3', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3059', '0d726135121f4fe49569b533f2dae1f3', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3060', '0d726135121f4fe49569b533f2dae1f3', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3061', '0d726135121f4fe49569b533f2dae1f3', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3062', '0d726135121f4fe49569b533f2dae1f3', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3063', '0d726135121f4fe49569b533f2dae1f3', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3064', '0d726135121f4fe49569b533f2dae1f3', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3065', '0d726135121f4fe49569b533f2dae1f3', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3066', '0d726135121f4fe49569b533f2dae1f3', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3067', '0d726135121f4fe49569b533f2dae1f3', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3068', '0d726135121f4fe49569b533f2dae1f3', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3069', '0d726135121f4fe49569b533f2dae1f3', 'd71f0ab22dbb49c09a987222a8d53e01', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3070', '0d726135121f4fe49569b533f2dae1f3', '69eae19b5977468b952952773078ad77', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3071', '0d726135121f4fe49569b533f2dae1f3', '71ec0dc1f81a4d4893824151b82a77ba', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3072', '0d726135121f4fe49569b533f2dae1f3', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3073', '0d726135121f4fe49569b533f2dae1f3', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3074', '0d726135121f4fe49569b533f2dae1f3', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3075', '0d726135121f4fe49569b533f2dae1f3', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3076', '0d726135121f4fe49569b533f2dae1f3', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3077', '0d726135121f4fe49569b533f2dae1f3', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3078', '0d726135121f4fe49569b533f2dae1f3', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3079', '0d726135121f4fe49569b533f2dae1f3', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3080', '0d726135121f4fe49569b533f2dae1f3', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3081', '0d726135121f4fe49569b533f2dae1f3', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3082', '0d726135121f4fe49569b533f2dae1f3', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3083', '0d726135121f4fe49569b533f2dae1f3', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3084', '0d726135121f4fe49569b533f2dae1f3', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3085', '0d726135121f4fe49569b533f2dae1f3', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3086', '0d726135121f4fe49569b533f2dae1f3', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3087', '0d726135121f4fe49569b533f2dae1f3', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3088', '0d726135121f4fe49569b533f2dae1f3', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3089', '0d726135121f4fe49569b533f2dae1f3', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3090', '0d726135121f4fe49569b533f2dae1f3', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3091', '0d726135121f4fe49569b533f2dae1f3', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3092', '0d726135121f4fe49569b533f2dae1f3', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3093', '0d726135121f4fe49569b533f2dae1f3', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3094', '0d726135121f4fe49569b533f2dae1f3', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3095', '0d726135121f4fe49569b533f2dae1f3', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3096', '0d726135121f4fe49569b533f2dae1f3', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3097', '0d726135121f4fe49569b533f2dae1f3', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3098', '0d726135121f4fe49569b533f2dae1f3', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3099', '0d726135121f4fe49569b533f2dae1f3', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3100', '0d726135121f4fe49569b533f2dae1f3', '7666992387c14de09cc2199d9cb1fc1e', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3101', '0d726135121f4fe49569b533f2dae1f3', 'd48a5a7d612c4099a6bf9f4b9a469c18', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3102', '0d726135121f4fe49569b533f2dae1f3', '9490d40810bf47ada12e9734756d9b68', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3103', '0d726135121f4fe49569b533f2dae1f3', 'e88d4d1406dc4d0b8823d0250349ddac', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3104', '0d726135121f4fe49569b533f2dae1f3', 'cf3e9c21b80b4181b7a5c53993be4e1f', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3105', '0d726135121f4fe49569b533f2dae1f3', '817243e48f3a4e37a48736b9e3b1ea24', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('3106', '0d726135121f4fe49569b533f2dae1f3', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '0', '10001003', '2021-04-26 22:00:03.882000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4394', '520fccd50efa484e9ab2f87cddd22283', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-26 22:28:41.425000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4395', '520fccd50efa484e9ab2f87cddd22283', 'b9b2b01611fc4f9085efdd306a9faa82', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4396', '520fccd50efa484e9ab2f87cddd22283', 'f1b278bc2e08423a8c0bcfb885b0812e', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4397', '520fccd50efa484e9ab2f87cddd22283', '3c722f8c487e4bc3aaaaee03868ff089', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4398', '520fccd50efa484e9ab2f87cddd22283', '008af8b9f1324827a181d03710baa92c', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4399', '520fccd50efa484e9ab2f87cddd22283', '85b78183ddad429c8c8d3add0e775a35', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4400', '520fccd50efa484e9ab2f87cddd22283', '5c338ca25e8340f08f0e44004a6c6ee9', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4401', '520fccd50efa484e9ab2f87cddd22283', '8a9161a27f114e338f08e17b36147c85', '1', '10001002', '2021-04-26 22:29:02.973000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4402', '520fccd50efa484e9ab2f87cddd22283', '8a9161a27f114e338f08e17b36147c85', '1', '10001002', '2021-04-26 22:29:06.338000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4403', '520fccd50efa484e9ab2f87cddd22283', 'b9b2b01611fc4f9085efdd306a9faa82', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4404', '520fccd50efa484e9ab2f87cddd22283', 'f1b278bc2e08423a8c0bcfb885b0812e', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4405', '520fccd50efa484e9ab2f87cddd22283', '3c722f8c487e4bc3aaaaee03868ff089', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4406', '520fccd50efa484e9ab2f87cddd22283', '008af8b9f1324827a181d03710baa92c', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4407', '520fccd50efa484e9ab2f87cddd22283', '85b78183ddad429c8c8d3add0e775a35', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4408', '520fccd50efa484e9ab2f87cddd22283', '5c338ca25e8340f08f0e44004a6c6ee9', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4409', '520fccd50efa484e9ab2f87cddd22283', '8a9161a27f114e338f08e17b36147c85', '1', '10001002', '2021-04-26 22:29:09.095000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4410', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', 'b9b2b01611fc4f9085efdd306a9faa82', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4411', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', 'f1b278bc2e08423a8c0bcfb885b0812e', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4412', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '3c722f8c487e4bc3aaaaee03868ff089', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4413', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '008af8b9f1324827a181d03710baa92c', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4414', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '85b78183ddad429c8c8d3add0e775a35', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4415', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '5c338ca25e8340f08f0e44004a6c6ee9', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4416', 'e7dd9c824ff94d4c8b93cc1996b4ff0c', '8a9161a27f114e338f08e17b36147c85', '0', '10001002', '2021-04-26 22:31:32.534000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4417', 'f033f6356c2449bdb34d657fa4720ffa', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4418', 'f033f6356c2449bdb34d657fa4720ffa', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4419', 'f033f6356c2449bdb34d657fa4720ffa', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4420', 'f033f6356c2449bdb34d657fa4720ffa', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4421', 'f033f6356c2449bdb34d657fa4720ffa', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4422', 'f033f6356c2449bdb34d657fa4720ffa', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4423', 'f033f6356c2449bdb34d657fa4720ffa', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4424', 'f033f6356c2449bdb34d657fa4720ffa', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4425', 'f033f6356c2449bdb34d657fa4720ffa', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4426', 'f033f6356c2449bdb34d657fa4720ffa', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4427', 'f033f6356c2449bdb34d657fa4720ffa', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4428', 'f033f6356c2449bdb34d657fa4720ffa', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4429', 'f033f6356c2449bdb34d657fa4720ffa', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4430', 'f033f6356c2449bdb34d657fa4720ffa', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4431', 'f033f6356c2449bdb34d657fa4720ffa', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4432', 'f033f6356c2449bdb34d657fa4720ffa', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4433', 'f033f6356c2449bdb34d657fa4720ffa', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4434', 'f033f6356c2449bdb34d657fa4720ffa', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4435', 'f033f6356c2449bdb34d657fa4720ffa', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4436', 'f033f6356c2449bdb34d657fa4720ffa', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4437', 'f033f6356c2449bdb34d657fa4720ffa', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4438', 'f033f6356c2449bdb34d657fa4720ffa', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4439', 'f033f6356c2449bdb34d657fa4720ffa', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4440', 'f033f6356c2449bdb34d657fa4720ffa', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4441', 'f033f6356c2449bdb34d657fa4720ffa', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4442', 'f033f6356c2449bdb34d657fa4720ffa', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4443', 'f033f6356c2449bdb34d657fa4720ffa', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4444', 'f033f6356c2449bdb34d657fa4720ffa', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4445', 'f033f6356c2449bdb34d657fa4720ffa', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4446', 'f033f6356c2449bdb34d657fa4720ffa', 'd71f0ab22dbb49c09a987222a8d53e01', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4447', 'f033f6356c2449bdb34d657fa4720ffa', '69eae19b5977468b952952773078ad77', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4448', 'f033f6356c2449bdb34d657fa4720ffa', '71ec0dc1f81a4d4893824151b82a77ba', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4449', 'f033f6356c2449bdb34d657fa4720ffa', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4450', 'f033f6356c2449bdb34d657fa4720ffa', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4451', 'f033f6356c2449bdb34d657fa4720ffa', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4452', 'f033f6356c2449bdb34d657fa4720ffa', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4453', 'f033f6356c2449bdb34d657fa4720ffa', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4454', 'f033f6356c2449bdb34d657fa4720ffa', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4455', 'f033f6356c2449bdb34d657fa4720ffa', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4456', 'f033f6356c2449bdb34d657fa4720ffa', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4457', 'f033f6356c2449bdb34d657fa4720ffa', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4458', 'f033f6356c2449bdb34d657fa4720ffa', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4459', 'f033f6356c2449bdb34d657fa4720ffa', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4460', 'f033f6356c2449bdb34d657fa4720ffa', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4461', 'f033f6356c2449bdb34d657fa4720ffa', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4462', 'f033f6356c2449bdb34d657fa4720ffa', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4463', 'f033f6356c2449bdb34d657fa4720ffa', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4464', 'f033f6356c2449bdb34d657fa4720ffa', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4465', 'f033f6356c2449bdb34d657fa4720ffa', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4466', 'f033f6356c2449bdb34d657fa4720ffa', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4467', 'f033f6356c2449bdb34d657fa4720ffa', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4468', 'f033f6356c2449bdb34d657fa4720ffa', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4469', 'f033f6356c2449bdb34d657fa4720ffa', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4470', 'f033f6356c2449bdb34d657fa4720ffa', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4471', 'f033f6356c2449bdb34d657fa4720ffa', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4472', 'f033f6356c2449bdb34d657fa4720ffa', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4473', 'f033f6356c2449bdb34d657fa4720ffa', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4474', 'f033f6356c2449bdb34d657fa4720ffa', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4475', 'f033f6356c2449bdb34d657fa4720ffa', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4476', 'f033f6356c2449bdb34d657fa4720ffa', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4477', 'f033f6356c2449bdb34d657fa4720ffa', '7666992387c14de09cc2199d9cb1fc1e', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4478', 'f033f6356c2449bdb34d657fa4720ffa', 'd48a5a7d612c4099a6bf9f4b9a469c18', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4479', 'f033f6356c2449bdb34d657fa4720ffa', '9490d40810bf47ada12e9734756d9b68', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4480', 'f033f6356c2449bdb34d657fa4720ffa', 'e88d4d1406dc4d0b8823d0250349ddac', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4481', 'f033f6356c2449bdb34d657fa4720ffa', 'cf3e9c21b80b4181b7a5c53993be4e1f', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4482', 'f033f6356c2449bdb34d657fa4720ffa', '817243e48f3a4e37a48736b9e3b1ea24', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4483', 'f033f6356c2449bdb34d657fa4720ffa', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '0', '10001003', '2021-04-26 22:32:12.030000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4484', '142a30e4210c48a0966e84bf1e22e815', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4485', '142a30e4210c48a0966e84bf1e22e815', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4486', '142a30e4210c48a0966e84bf1e22e815', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4487', '142a30e4210c48a0966e84bf1e22e815', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4488', '142a30e4210c48a0966e84bf1e22e815', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4489', '142a30e4210c48a0966e84bf1e22e815', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4490', '142a30e4210c48a0966e84bf1e22e815', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4491', '142a30e4210c48a0966e84bf1e22e815', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4492', '142a30e4210c48a0966e84bf1e22e815', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4493', '142a30e4210c48a0966e84bf1e22e815', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4494', '142a30e4210c48a0966e84bf1e22e815', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4495', '142a30e4210c48a0966e84bf1e22e815', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4496', '142a30e4210c48a0966e84bf1e22e815', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4497', '142a30e4210c48a0966e84bf1e22e815', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4498', '142a30e4210c48a0966e84bf1e22e815', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4499', '142a30e4210c48a0966e84bf1e22e815', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4500', '142a30e4210c48a0966e84bf1e22e815', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4501', '142a30e4210c48a0966e84bf1e22e815', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4502', '142a30e4210c48a0966e84bf1e22e815', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4503', '142a30e4210c48a0966e84bf1e22e815', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-26 22:33:21.164000', 'f70a3f2d8ccc46cf8636259383b4849b');
INSERT INTO `t_sa_role_function` VALUES ('4504', '142a30e4210c48a0966e84bf1e22e815', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4505', '142a30e4210c48a0966e84bf1e22e815', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4506', '142a30e4210c48a0966e84bf1e22e815', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4507', '142a30e4210c48a0966e84bf1e22e815', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4508', '142a30e4210c48a0966e84bf1e22e815', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4509', '142a30e4210c48a0966e84bf1e22e815', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4510', '142a30e4210c48a0966e84bf1e22e815', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4511', '142a30e4210c48a0966e84bf1e22e815', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4512', '142a30e4210c48a0966e84bf1e22e815', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4513', '142a30e4210c48a0966e84bf1e22e815', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4514', '142a30e4210c48a0966e84bf1e22e815', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4515', '142a30e4210c48a0966e84bf1e22e815', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4516', '142a30e4210c48a0966e84bf1e22e815', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4517', '142a30e4210c48a0966e84bf1e22e815', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4518', '142a30e4210c48a0966e84bf1e22e815', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4519', '142a30e4210c48a0966e84bf1e22e815', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4520', '142a30e4210c48a0966e84bf1e22e815', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4521', '142a30e4210c48a0966e84bf1e22e815', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4522', '142a30e4210c48a0966e84bf1e22e815', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4523', '142a30e4210c48a0966e84bf1e22e815', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4524', '142a30e4210c48a0966e84bf1e22e815', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4525', '142a30e4210c48a0966e84bf1e22e815', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4526', '142a30e4210c48a0966e84bf1e22e815', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4527', '142a30e4210c48a0966e84bf1e22e815', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4528', '142a30e4210c48a0966e84bf1e22e815', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4529', '142a30e4210c48a0966e84bf1e22e815', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4530', '142a30e4210c48a0966e84bf1e22e815', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4531', '142a30e4210c48a0966e84bf1e22e815', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4532', '142a30e4210c48a0966e84bf1e22e815', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4533', '142a30e4210c48a0966e84bf1e22e815', 'd71f0ab22dbb49c09a987222a8d53e01', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4534', '142a30e4210c48a0966e84bf1e22e815', '69eae19b5977468b952952773078ad77', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4535', '142a30e4210c48a0966e84bf1e22e815', '71ec0dc1f81a4d4893824151b82a77ba', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4536', '142a30e4210c48a0966e84bf1e22e815', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4537', '142a30e4210c48a0966e84bf1e22e815', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4538', '142a30e4210c48a0966e84bf1e22e815', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4539', '142a30e4210c48a0966e84bf1e22e815', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4540', '142a30e4210c48a0966e84bf1e22e815', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4541', '142a30e4210c48a0966e84bf1e22e815', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4542', '142a30e4210c48a0966e84bf1e22e815', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4543', '142a30e4210c48a0966e84bf1e22e815', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4544', '142a30e4210c48a0966e84bf1e22e815', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4545', '142a30e4210c48a0966e84bf1e22e815', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4546', '142a30e4210c48a0966e84bf1e22e815', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4547', '142a30e4210c48a0966e84bf1e22e815', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4548', '142a30e4210c48a0966e84bf1e22e815', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4549', '142a30e4210c48a0966e84bf1e22e815', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4550', '142a30e4210c48a0966e84bf1e22e815', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4551', '142a30e4210c48a0966e84bf1e22e815', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4552', '142a30e4210c48a0966e84bf1e22e815', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4553', '142a30e4210c48a0966e84bf1e22e815', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4554', '142a30e4210c48a0966e84bf1e22e815', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4555', '142a30e4210c48a0966e84bf1e22e815', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4556', '142a30e4210c48a0966e84bf1e22e815', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4557', '142a30e4210c48a0966e84bf1e22e815', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4558', '142a30e4210c48a0966e84bf1e22e815', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4559', '142a30e4210c48a0966e84bf1e22e815', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4560', '142a30e4210c48a0966e84bf1e22e815', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4561', '142a30e4210c48a0966e84bf1e22e815', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4562', '142a30e4210c48a0966e84bf1e22e815', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4563', '142a30e4210c48a0966e84bf1e22e815', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4564', '142a30e4210c48a0966e84bf1e22e815', '7666992387c14de09cc2199d9cb1fc1e', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4565', '142a30e4210c48a0966e84bf1e22e815', 'd48a5a7d612c4099a6bf9f4b9a469c18', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4566', '142a30e4210c48a0966e84bf1e22e815', '9490d40810bf47ada12e9734756d9b68', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4567', '142a30e4210c48a0966e84bf1e22e815', 'e88d4d1406dc4d0b8823d0250349ddac', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4568', '142a30e4210c48a0966e84bf1e22e815', 'cf3e9c21b80b4181b7a5c53993be4e1f', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4569', '142a30e4210c48a0966e84bf1e22e815', '817243e48f3a4e37a48736b9e3b1ea24', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4570', '142a30e4210c48a0966e84bf1e22e815', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '0', '10001003', '2021-04-27 20:51:29.712000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4571', '520fccd50efa484e9ab2f87cddd22283', 'b9b2b01611fc4f9085efdd306a9faa82', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4572', '520fccd50efa484e9ab2f87cddd22283', 'f1b278bc2e08423a8c0bcfb885b0812e', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4573', '520fccd50efa484e9ab2f87cddd22283', '3c722f8c487e4bc3aaaaee03868ff089', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4574', '520fccd50efa484e9ab2f87cddd22283', '008af8b9f1324827a181d03710baa92c', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4575', '520fccd50efa484e9ab2f87cddd22283', '85b78183ddad429c8c8d3add0e775a35', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4576', '520fccd50efa484e9ab2f87cddd22283', '5c338ca25e8340f08f0e44004a6c6ee9', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4577', '520fccd50efa484e9ab2f87cddd22283', '8a9161a27f114e338f08e17b36147c85', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4578', '520fccd50efa484e9ab2f87cddd22283', 'd01e9ce3149447bba611fb9a4ce89c52', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4579', '520fccd50efa484e9ab2f87cddd22283', '5e0aa16a0f764356834bbed64ba31d23', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4580', '520fccd50efa484e9ab2f87cddd22283', '0446c9ac1bf1436faa8970dd17c9e9d5', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4581', '520fccd50efa484e9ab2f87cddd22283', '8810ce6710214deaa6536563a0df0482', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4582', '520fccd50efa484e9ab2f87cddd22283', '6b1c2317153a40ec825b44a70c1fad27', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4583', '520fccd50efa484e9ab2f87cddd22283', '18c9d0cb6bdd4f7c906a8f436820febc', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4584', '520fccd50efa484e9ab2f87cddd22283', '08a5abd983e24a88b766d1f922e5c8ad', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4585', '520fccd50efa484e9ab2f87cddd22283', 'a65ed431dc454e1ab1f0c01d8cb2ae29', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4586', '520fccd50efa484e9ab2f87cddd22283', '7aed4943f40b4cc7bea3d20b72c662a0', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4587', '520fccd50efa484e9ab2f87cddd22283', 'c48d28589f69460b8ef2503b3b425b30', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4588', '520fccd50efa484e9ab2f87cddd22283', '70e43cf91730430d9dce088922233aa7', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4589', '520fccd50efa484e9ab2f87cddd22283', '89f53945e62047889a0134211bb91b52', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4590', '520fccd50efa484e9ab2f87cddd22283', 'c2e815b2c37b4386af7a593b81f48e0c', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4591', '520fccd50efa484e9ab2f87cddd22283', '9081135c19ce4211bb2d814d4d30ff39', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4592', '520fccd50efa484e9ab2f87cddd22283', '5df8c4ac14424800932cc038f6648c6b', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4593', '520fccd50efa484e9ab2f87cddd22283', 'c2bc991d3e14463b800619a320fb6f42', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4594', '520fccd50efa484e9ab2f87cddd22283', '7625f2b54bb840ee818f4414f8cca503', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4595', '520fccd50efa484e9ab2f87cddd22283', '3abda2426e8549c5b66de8169953cc20', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4596', '520fccd50efa484e9ab2f87cddd22283', 'f8711d603dad4327bd9d66dc98cd1f4a', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4597', '520fccd50efa484e9ab2f87cddd22283', 'd0d5a47cf8aa42eea91d5354e7adac1d', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4598', '520fccd50efa484e9ab2f87cddd22283', 'bc359810d7d84b7f891f1019329c3d6c', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4599', '520fccd50efa484e9ab2f87cddd22283', '3285ed3338bb41c8b6b2844c9bda638e', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4600', '520fccd50efa484e9ab2f87cddd22283', '791226813f3f4562b87d31a100004510', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4601', '520fccd50efa484e9ab2f87cddd22283', '578b74283f3041b5807370e55c84f07b', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4602', '520fccd50efa484e9ab2f87cddd22283', '61265f25843d4699b8b219416a3cf0b7', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4603', '520fccd50efa484e9ab2f87cddd22283', 'a3c69655aeaa4cda96f83fc3cd885403', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4604', '520fccd50efa484e9ab2f87cddd22283', '16c45f59cdc944838b74bb0ac9ccaba4', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4605', '520fccd50efa484e9ab2f87cddd22283', 'ab7a1869b17a457e9f3c70c7546bfe94', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4606', '520fccd50efa484e9ab2f87cddd22283', '94823ad0efb0416e96e10ba3fc1a49a3', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4607', '520fccd50efa484e9ab2f87cddd22283', '5efc4a4dc8134e95b8813826120e572e', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4608', '520fccd50efa484e9ab2f87cddd22283', '42cd8d642e6646e9bca9d0968f1cf3d1', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4609', '520fccd50efa484e9ab2f87cddd22283', '22568711d78d415abaa691cdf6f851f8', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4610', '520fccd50efa484e9ab2f87cddd22283', '87aa5e2992be4d82b921effb5944b8dd', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4611', '520fccd50efa484e9ab2f87cddd22283', 'f4cfed52696d47acbb8d7116ee840d24', '0', '10001002', '2021-04-27 22:16:10.187000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4612', '520fccd50efa484e9ab2f87cddd22283', 'c327242ef63d4a5abe2d25cfdde1372d', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4613', '520fccd50efa484e9ab2f87cddd22283', '849f46576f9b410ba0dcc774bbeea938', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4614', '520fccd50efa484e9ab2f87cddd22283', '9d3f90ca51244940a7df17656a7f19bc', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4615', '520fccd50efa484e9ab2f87cddd22283', '2456202dd87c4159a8dd505d64014c72', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4616', '520fccd50efa484e9ab2f87cddd22283', 'eb3417333b994428a89e022acaf5917c', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4617', '520fccd50efa484e9ab2f87cddd22283', 'b29e44f69dc545dabb852b0b90edb277', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4618', '520fccd50efa484e9ab2f87cddd22283', '16376300bccc4bbea885a63d8da5d6d7', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4619', '520fccd50efa484e9ab2f87cddd22283', '3ffc645d35e64cb49911381e082e705b', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4620', '520fccd50efa484e9ab2f87cddd22283', 'ce1dd961a393430ea9cf7d805fecae6a', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4621', '520fccd50efa484e9ab2f87cddd22283', '7ce3b688dcb64cfea6da9ee592ee15f8', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4622', '520fccd50efa484e9ab2f87cddd22283', '2b84217a9cea4872a3fae59a1cef7bb0', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4623', '520fccd50efa484e9ab2f87cddd22283', 'b26a981a319a4cae94ccf3e89a30052e', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4624', '520fccd50efa484e9ab2f87cddd22283', '604e828b9fc44dd68eeaa8d386f42d39', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4625', '520fccd50efa484e9ab2f87cddd22283', 'cc991918881c45338f73fb36547a920b', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4626', '520fccd50efa484e9ab2f87cddd22283', 'ad3a3267232844a0b708000a7a1a020d', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4627', '520fccd50efa484e9ab2f87cddd22283', '41c10380eff94d7eb8192261893c93c9', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4628', '520fccd50efa484e9ab2f87cddd22283', '87639c0130914eb1941bf5b693c90be6', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4629', '520fccd50efa484e9ab2f87cddd22283', '01035ee0add74a66a397210a1e7da41a', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4630', '520fccd50efa484e9ab2f87cddd22283', '52028b49cf454249b9be4f975a8b1234', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4631', '520fccd50efa484e9ab2f87cddd22283', 'b42c611ef3584f168db86aaa3e626bc5', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4632', '520fccd50efa484e9ab2f87cddd22283', '2ecf9d5f5ac34003997ebcf0de51c7dc', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4633', '520fccd50efa484e9ab2f87cddd22283', '3dad0c5c0c224efe970b07eb4b4ef925', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4634', '520fccd50efa484e9ab2f87cddd22283', 'd581d0308c0a4b7e80fa66204f04002e', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4635', '520fccd50efa484e9ab2f87cddd22283', 'eac6c56db5e44ba28cf9fba3984e262a', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4636', '520fccd50efa484e9ab2f87cddd22283', '1cf5637f8c9c4605858bda4a11a0dc23', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4637', '520fccd50efa484e9ab2f87cddd22283', '4bac73c4da7d48ebbbb45c17f004396d', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4638', '520fccd50efa484e9ab2f87cddd22283', 'c54cb0504a294816bf263530dca21c5e', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4639', '520fccd50efa484e9ab2f87cddd22283', 'db88044d79564e0ebf9a71f734846321', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4640', '520fccd50efa484e9ab2f87cddd22283', '85036c6480b44dbabe846bfe5dc3c977', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4641', '520fccd50efa484e9ab2f87cddd22283', '6bbb760e7cda4f5d894b918e3c087a8f', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4642', '520fccd50efa484e9ab2f87cddd22283', 'f7c3b76623c44c8cac321f5518a7a8b1', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4643', '520fccd50efa484e9ab2f87cddd22283', 'a637513a11d84c6d86e9bcde72c21ec7', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4644', '520fccd50efa484e9ab2f87cddd22283', '80e877930cb4444d8acebd72e4181691', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4645', '520fccd50efa484e9ab2f87cddd22283', '6853f0b1b97e4417b4dc1af85be4ae89', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4646', '520fccd50efa484e9ab2f87cddd22283', '330bdaa145534be5addea49916ce8517', '0', '10001002', '2021-04-27 22:16:10.188000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4647', '89d3bf8fbf4543188a64fb7738a6bc62', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4648', '89d3bf8fbf4543188a64fb7738a6bc62', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4649', '89d3bf8fbf4543188a64fb7738a6bc62', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4650', '89d3bf8fbf4543188a64fb7738a6bc62', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4651', '89d3bf8fbf4543188a64fb7738a6bc62', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4652', '89d3bf8fbf4543188a64fb7738a6bc62', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4653', '89d3bf8fbf4543188a64fb7738a6bc62', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4654', '89d3bf8fbf4543188a64fb7738a6bc62', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4655', '89d3bf8fbf4543188a64fb7738a6bc62', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4656', '89d3bf8fbf4543188a64fb7738a6bc62', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4657', '89d3bf8fbf4543188a64fb7738a6bc62', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4658', '89d3bf8fbf4543188a64fb7738a6bc62', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4659', '89d3bf8fbf4543188a64fb7738a6bc62', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4660', '89d3bf8fbf4543188a64fb7738a6bc62', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4661', '89d3bf8fbf4543188a64fb7738a6bc62', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4662', '89d3bf8fbf4543188a64fb7738a6bc62', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4663', '89d3bf8fbf4543188a64fb7738a6bc62', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4664', '89d3bf8fbf4543188a64fb7738a6bc62', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4665', '89d3bf8fbf4543188a64fb7738a6bc62', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4666', '89d3bf8fbf4543188a64fb7738a6bc62', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4667', '89d3bf8fbf4543188a64fb7738a6bc62', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4668', '89d3bf8fbf4543188a64fb7738a6bc62', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4669', '89d3bf8fbf4543188a64fb7738a6bc62', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4670', '89d3bf8fbf4543188a64fb7738a6bc62', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4671', '89d3bf8fbf4543188a64fb7738a6bc62', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4672', '89d3bf8fbf4543188a64fb7738a6bc62', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4673', '89d3bf8fbf4543188a64fb7738a6bc62', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4674', '89d3bf8fbf4543188a64fb7738a6bc62', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4675', '89d3bf8fbf4543188a64fb7738a6bc62', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4676', '89d3bf8fbf4543188a64fb7738a6bc62', 'd71f0ab22dbb49c09a987222a8d53e01', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4677', '89d3bf8fbf4543188a64fb7738a6bc62', '69eae19b5977468b952952773078ad77', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4678', '89d3bf8fbf4543188a64fb7738a6bc62', '71ec0dc1f81a4d4893824151b82a77ba', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4679', '89d3bf8fbf4543188a64fb7738a6bc62', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4680', '89d3bf8fbf4543188a64fb7738a6bc62', 'f057542ebdfd426cbbab84acf419637a', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4681', '89d3bf8fbf4543188a64fb7738a6bc62', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4682', '89d3bf8fbf4543188a64fb7738a6bc62', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4683', '89d3bf8fbf4543188a64fb7738a6bc62', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4684', '89d3bf8fbf4543188a64fb7738a6bc62', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4685', '89d3bf8fbf4543188a64fb7738a6bc62', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4686', '89d3bf8fbf4543188a64fb7738a6bc62', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4687', '89d3bf8fbf4543188a64fb7738a6bc62', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4688', '89d3bf8fbf4543188a64fb7738a6bc62', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4689', '89d3bf8fbf4543188a64fb7738a6bc62', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4690', '89d3bf8fbf4543188a64fb7738a6bc62', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4691', '89d3bf8fbf4543188a64fb7738a6bc62', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4692', '89d3bf8fbf4543188a64fb7738a6bc62', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4693', '89d3bf8fbf4543188a64fb7738a6bc62', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4694', '89d3bf8fbf4543188a64fb7738a6bc62', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4695', '89d3bf8fbf4543188a64fb7738a6bc62', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4696', '89d3bf8fbf4543188a64fb7738a6bc62', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4697', '89d3bf8fbf4543188a64fb7738a6bc62', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4698', '89d3bf8fbf4543188a64fb7738a6bc62', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4699', '89d3bf8fbf4543188a64fb7738a6bc62', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4700', '89d3bf8fbf4543188a64fb7738a6bc62', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4701', '89d3bf8fbf4543188a64fb7738a6bc62', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4702', '89d3bf8fbf4543188a64fb7738a6bc62', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4703', '89d3bf8fbf4543188a64fb7738a6bc62', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4704', '89d3bf8fbf4543188a64fb7738a6bc62', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4705', '89d3bf8fbf4543188a64fb7738a6bc62', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4706', '89d3bf8fbf4543188a64fb7738a6bc62', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4707', '89d3bf8fbf4543188a64fb7738a6bc62', '7666992387c14de09cc2199d9cb1fc1e', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4708', '89d3bf8fbf4543188a64fb7738a6bc62', 'd48a5a7d612c4099a6bf9f4b9a469c18', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4709', '89d3bf8fbf4543188a64fb7738a6bc62', '9490d40810bf47ada12e9734756d9b68', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4710', '89d3bf8fbf4543188a64fb7738a6bc62', 'e88d4d1406dc4d0b8823d0250349ddac', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4711', '89d3bf8fbf4543188a64fb7738a6bc62', 'cf3e9c21b80b4181b7a5c53993be4e1f', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4712', '89d3bf8fbf4543188a64fb7738a6bc62', '817243e48f3a4e37a48736b9e3b1ea24', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4713', '89d3bf8fbf4543188a64fb7738a6bc62', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4714', '89d3bf8fbf4543188a64fb7738a6bc62', '18faaf650a6241a895423333610d1711', '0', '10001003', '2021-04-27 23:18:15.038000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4715', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4716', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4717', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4718', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4719', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4720', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4721', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4722', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4723', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4724', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4725', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4726', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4727', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4728', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4729', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4730', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4731', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4732', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4733', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4734', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4735', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4736', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4737', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4738', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4739', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4740', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4741', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4742', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4743', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4744', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4745', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4746', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4747', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4748', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4749', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4750', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4751', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4752', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4753', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4754', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4755', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4756', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4757', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4758', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4759', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4760', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4761', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4762', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4763', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4764', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4765', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4766', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4767', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4768', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4769', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4770', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4771', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4772', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4773', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4774', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4775', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4776', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4777', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4778', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4779', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4780', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4781', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4782', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4783', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4784', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4785', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4786', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-28 21:05:25.483000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4787', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4788', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4789', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4790', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4791', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4792', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4793', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4794', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4795', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4796', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4797', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4798', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4799', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4800', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4801', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4802', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4803', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4804', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4805', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4806', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4807', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4808', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4809', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4810', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4811', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4812', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4813', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4814', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4815', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4816', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4817', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4818', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4819', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4820', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4821', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4822', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4823', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4824', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4825', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4826', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4827', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4828', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4829', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4830', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4831', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4832', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4833', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4834', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4835', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4836', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4837', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4838', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4839', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4840', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4841', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4842', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4843', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4844', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4845', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4846', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4847', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4848', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4849', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4850', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4851', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4852', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4853', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4854', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4855', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4856', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-28 21:15:15.610000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4857', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4858', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4859', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4860', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4861', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4862', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4863', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4864', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4865', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4866', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4867', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4868', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4869', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4870', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4871', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4872', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4873', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4874', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4875', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4876', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4877', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4878', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4879', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4880', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4881', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4882', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4883', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4884', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4885', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4886', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4887', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4888', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4889', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4890', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4891', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4892', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4893', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4894', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4895', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4896', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4897', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4898', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4899', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4900', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4901', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4902', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4903', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4904', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4905', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4906', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4907', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4908', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4909', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4910', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4911', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4912', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4913', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4914', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4915', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4916', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4917', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4918', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4919', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4920', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4921', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4922', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4923', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4924', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4925', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4926', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4927', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4928', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-28 21:15:33.253000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4929', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4930', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4931', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4932', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4933', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4934', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4935', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4936', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4937', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4938', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4939', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4940', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4941', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4942', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4943', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4944', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4945', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4946', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4947', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4948', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4949', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4950', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4951', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4952', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4953', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4954', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4955', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4956', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4957', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4958', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4959', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4960', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4961', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4962', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4963', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4964', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4965', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4966', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4967', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4968', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4969', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4970', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4971', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4972', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4973', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4974', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4975', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4976', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4977', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4978', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4979', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4980', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4981', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4982', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4983', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4984', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4985', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4986', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4987', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4988', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4989', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4990', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '76a28bf6f16746078f5356bde3bf784a', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4991', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f524b0f029214806a96e631d04816265', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4992', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ee50b9a4ed0c47a19b7039539b8f2677', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4993', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '80941ccae72b4c22872d3d99e26156e2', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4994', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8412253d276c4b0ba076a7f432894db8', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4995', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4996', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4997', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4998', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('4999', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5000', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5001', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5002', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5003', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5004', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5005', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-29 20:37:03.205000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5006', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5007', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5008', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5009', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5010', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5011', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5012', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5013', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5014', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5015', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5016', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5017', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5018', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5019', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5020', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5021', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5022', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5023', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5024', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5025', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5026', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5027', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5028', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5029', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5030', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5031', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5032', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5033', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5034', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5035', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5036', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5037', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5038', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5039', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5040', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5041', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5042', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5043', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5044', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5045', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5046', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5047', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5048', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5049', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5050', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5051', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5052', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5053', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5054', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5055', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5056', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5057', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5058', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5059', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5060', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5061', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5062', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5063', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5064', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5065', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5066', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5067', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '76a28bf6f16746078f5356bde3bf784a', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5068', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f524b0f029214806a96e631d04816265', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5069', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ee50b9a4ed0c47a19b7039539b8f2677', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5070', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '80941ccae72b4c22872d3d99e26156e2', '1', '10001003', '2021-04-29 20:50:33.202000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5071', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8412253d276c4b0ba076a7f432894db8', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5072', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '398e6190a3ae4e86ac078a96b97d064e', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5073', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5074', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5075', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5076', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5077', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5078', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5079', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5080', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5081', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5082', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5083', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-29 20:50:33.203000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5084', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5085', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5086', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5087', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5088', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5089', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5090', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5091', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5092', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5093', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5094', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5095', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5096', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5097', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5098', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5099', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5100', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5101', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5102', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5103', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5104', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5105', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5106', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5107', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5108', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5109', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5110', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5111', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5112', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5113', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5114', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5115', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5116', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5117', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5118', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5119', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5120', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5121', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5122', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5123', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5124', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5125', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5126', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5127', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5128', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5129', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5130', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5131', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5132', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5133', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5134', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5135', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5136', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5137', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5138', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5139', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5140', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5141', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5142', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5143', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5144', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5145', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '76a28bf6f16746078f5356bde3bf784a', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5146', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f524b0f029214806a96e631d04816265', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5147', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ee50b9a4ed0c47a19b7039539b8f2677', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5148', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '80941ccae72b4c22872d3d99e26156e2', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5149', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8412253d276c4b0ba076a7f432894db8', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5150', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '398e6190a3ae4e86ac078a96b97d064e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5151', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fc4846014de94233b78d30e0ab6c049e', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5152', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c9b11320ec384746b12b198039cb3ea0', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5153', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e0bcf4297ba1467cb4b446fe4a994c1f', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5154', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '358d73e16fea43cab3042cf6f688d503', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5155', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '3160f9bd2e8440558a8b0b9a3fb6e141', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5156', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '328e63695c744eb2b8213c5d0004fef1', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5157', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71481f2948c143ed89b1d6a5aae29c17', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5158', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5159', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5160', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5161', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5162', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5163', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5164', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5165', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5166', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5167', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5168', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-29 21:30:01.230000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5169', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5170', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5171', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5172', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5173', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5174', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5175', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5176', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5177', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5178', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5179', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5180', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5181', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5182', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5183', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5184', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5185', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5186', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5187', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5188', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5189', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5190', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5191', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5192', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5193', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5194', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5195', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5196', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5197', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5198', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5199', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5200', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5201', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a366f76c113449efa0003b609f10a455', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5202', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5203', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5204', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5205', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5206', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5207', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5208', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5209', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5210', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5211', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5212', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5213', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5214', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5215', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5216', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5217', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5218', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5219', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5220', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5221', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5222', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5223', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5224', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5225', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5226', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5227', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5228', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5229', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5230', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5231', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '76a28bf6f16746078f5356bde3bf784a', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5232', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f524b0f029214806a96e631d04816265', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5233', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ee50b9a4ed0c47a19b7039539b8f2677', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5234', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '80941ccae72b4c22872d3d99e26156e2', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5235', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8412253d276c4b0ba076a7f432894db8', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5236', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '398e6190a3ae4e86ac078a96b97d064e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5237', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fc4846014de94233b78d30e0ab6c049e', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5238', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c9b11320ec384746b12b198039cb3ea0', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5239', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e0bcf4297ba1467cb4b446fe4a994c1f', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5240', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '358d73e16fea43cab3042cf6f688d503', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5241', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '3160f9bd2e8440558a8b0b9a3fb6e141', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5242', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '328e63695c744eb2b8213c5d0004fef1', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5243', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71481f2948c143ed89b1d6a5aae29c17', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5244', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5245', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5246', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5247', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5248', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5249', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5250', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5251', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5252', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5253', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5254', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-30 20:57:35.952000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5255', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5256', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5257', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5258', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5259', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5260', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5261', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5262', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5263', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5264', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5265', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5266', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5267', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5268', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5269', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5270', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5271', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5272', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5273', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5274', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5275', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5276', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5277', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5278', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5279', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5280', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5281', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5282', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5283', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5284', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5285', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5286', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5287', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a366f76c113449efa0003b609f10a455', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5288', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81158fefd27541b39077369e011856fb', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5289', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5290', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5291', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5292', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5293', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5294', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5295', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5296', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5297', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5298', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5299', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5300', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5301', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5302', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5303', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5304', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5305', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5306', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5307', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5308', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5309', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5310', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5311', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5312', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5313', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5314', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5315', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5316', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5317', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5318', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '76a28bf6f16746078f5356bde3bf784a', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5319', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f524b0f029214806a96e631d04816265', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5320', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ee50b9a4ed0c47a19b7039539b8f2677', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5321', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '80941ccae72b4c22872d3d99e26156e2', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5322', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8412253d276c4b0ba076a7f432894db8', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5323', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '398e6190a3ae4e86ac078a96b97d064e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5324', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fc4846014de94233b78d30e0ab6c049e', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5325', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c9b11320ec384746b12b198039cb3ea0', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5326', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e0bcf4297ba1467cb4b446fe4a994c1f', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5327', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '358d73e16fea43cab3042cf6f688d503', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5328', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '3160f9bd2e8440558a8b0b9a3fb6e141', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5329', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '328e63695c744eb2b8213c5d0004fef1', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5330', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71481f2948c143ed89b1d6a5aae29c17', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5331', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5332', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5333', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5334', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5335', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5336', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5337', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5338', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5339', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5340', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5341', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '1', '10001003', '2021-04-30 21:08:39.553000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5342', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5c64c60e08134442821abc9ad7e3b4c8', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5343', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '39c83f899de34bf59b21c6886582632a', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5344', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f6ad367db5e44191ad24a960803cea67', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5345', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ea0902431e344171b868835db84083e6', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5346', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '63170363336b414faca2c340adbcabc4', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5347', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71967473598845d6a0804a3fbbdba133', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5348', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd156bdfa0d0c4b4ba5f460a9d055079b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5349', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a9d6ae5c80e342acaf4eea99355f91bb', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5350', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '55921edd97684d6b863b45d6a2f0bc3e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5351', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8ef8f4fdbdc04681a6c71bd1818aa593', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5352', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '464ef2d2d2a04fde9a033c6663bfa66b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5353', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '83e92b838c07405dbcd7ff4c04627855', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5354', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81bba07a4ed14ad8abe46226bd914015', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5355', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b123af0d51ff401db2a742702c011e0b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5356', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd0343e35b29d407787e84a0a28f0f11e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5357', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd574b4a76c194d499d1d5454aaf0a809', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5358', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e6bac32f950a46d6a060c14f0ebf64b7', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5359', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '826bf5cd67fd41679bbb46a13fb17a34', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5360', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6fbdeca5f0354766a6edc1852eeba1ff', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5361', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '606c586db19641029e076eff37143ba3', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5362', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd2b024f36714282ab7788879edcf7b1', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5363', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '0b347bbdb6f44e9290dd0676a63b54d9', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5364', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '281a01f4d9f04982a6940803908f3a3b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5365', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1a51535097dd462a898e4233890e1303', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5366', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bb12900ea3f2486c99931f54ed2a68df', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5367', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f67eaf649e9f42dcb60c7190838b11e0', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5368', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c78a4535b7d1497c8ebd94b63e909916', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5369', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eeeaa371845c412aa0dbafa5b48b79da', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5370', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '96e9cf50863c469a95e0f50084a1083b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5371', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd71f0ab22dbb49c09a987222a8d53e01', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5372', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '69eae19b5977468b952952773078ad77', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5373', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71ec0dc1f81a4d4893824151b82a77ba', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5374', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a366f76c113449efa0003b609f10a455', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5375', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '81158fefd27541b39077369e011856fb', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5376', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ffbc858ea7cb4d9e8c9788d67e26f51b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5377', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '84c94b59a1e54d53821f8d1f27d63c56', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5378', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f057542ebdfd426cbbab84acf419637a', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5379', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'bc68fa67e27b44a38ef746d5263832e5', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5380', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ddf50bb0daa047a183a645a6032b0616', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5381', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '40bd4766e1c34a0fbf5e4e92ba67900a', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5382', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '240a37be562840cb98e38d95bec1331f', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5383', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ba0700d73b38455888028be8e3e376f0', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5384', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '67acf29c21484b97b3b6e282edda76ca', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5385', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a077e729ce8942e38d6f2c606aba48eb', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5386', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f911a3a3ed4540dab81cfa6bae6c4d54', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5387', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fd5c6c4a98814e648b64d82fb7a05ba6', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5388', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c491a1cc1cbe4c268ca0327151c1567c', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5389', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c45bbc294bac4a60ba84d76c46771cf4', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5390', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f2c1644cc32e48fda25a8a1c5784d299', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5391', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'abf4c5e1f2a24b2a874af3fe54a2400e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5392', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ed16b75127e74de1be176251175b2a6e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5393', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '6a37026a28c645628c1cc2b6d44ac61a', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5394', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '363a0d886fa54e82821c08e0b0463de5', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5395', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b82237719cbf4409be88610c737448d2', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5396', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a392188d7fac472bbf1bd63eec86d488', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5397', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'b73d7f12439947aa8049bed8c3700315', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5398', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'aa2f5af303e34273980ec1c3c897157f', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5399', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f84d864fee8746698af4787e7542290d', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5400', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '710d97bce5684d7a95bd927150a323a5', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5401', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'dfa23a417aa9492e96b5a4e5d72ae61b', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5402', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '34a56f8868a84010a98d1a1ee51f36b9', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5403', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '31f368be0fb043e7b88850ddff6b27e3', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5404', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'eb6f4cbd34004ac4853c0fb8a3435652', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5405', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '7666992387c14de09cc2199d9cb1fc1e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5406', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '76a28bf6f16746078f5356bde3bf784a', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5407', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'f524b0f029214806a96e631d04816265', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5408', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ee50b9a4ed0c47a19b7039539b8f2677', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5409', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '80941ccae72b4c22872d3d99e26156e2', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5410', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '8412253d276c4b0ba076a7f432894db8', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5411', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '398e6190a3ae4e86ac078a96b97d064e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5412', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'fc4846014de94233b78d30e0ab6c049e', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5413', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'c9b11320ec384746b12b198039cb3ea0', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5414', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e0bcf4297ba1467cb4b446fe4a994c1f', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5415', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '358d73e16fea43cab3042cf6f688d503', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5416', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '3160f9bd2e8440558a8b0b9a3fb6e141', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5417', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '328e63695c744eb2b8213c5d0004fef1', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5418', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '71481f2948c143ed89b1d6a5aae29c17', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5419', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'd48a5a7d612c4099a6bf9f4b9a469c18', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5420', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '9490d40810bf47ada12e9734756d9b68', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5421', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'e88d4d1406dc4d0b8823d0250349ddac', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5422', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'cf3e9c21b80b4181b7a5c53993be4e1f', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5423', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '817243e48f3a4e37a48736b9e3b1ea24', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5424', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'a3e6b2f72e464e3abafe7d949f4a5ccc', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5425', '6ba7eb5c30ea49a5b20a4f9de5636bb0', 'ac670d4272df498fb472ede13e7e3d02', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5426', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '03696e1ade594de8973c3b10b8cd3468', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5427', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '1170994365654bc8a38304ac64ec33a2', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5428', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '5382554660024002901109048fafc1e3', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
INSERT INTO `t_sa_role_function` VALUES ('5429', '6ba7eb5c30ea49a5b20a4f9de5636bb0', '18faaf650a6241a895423333610d1711', '0', '10001003', '2021-04-30 22:04:11.644000', 'bb155acbf5ef43dcac9aa892274fadd5');
