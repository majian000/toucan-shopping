/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : toucan_shopping_category

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-03-29 21:41:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint NOT NULL COMMENT '父类别ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类别名称',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '-1',
  `type` smallint DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `category_sort` bigint unsigned DEFAULT '0' COMMENT '排序',
  `show_status` tinyint DEFAULT '1' COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '点击类别跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=330 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品类别表';

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES ('324', '-1', '家用电器', '/static/images/nav9.png', '1', '0', '1', 'www.baidu.com', '类别备注', '2021-03-29 16:46:12', null, null, null, '0');
INSERT INTO `t_category` VALUES ('325', '324', '电视', '-1', '1', '0', '1', 'www.163com', '类别备注', '2021-03-29 16:46:51', null, null, null, '0');
INSERT INTO `t_category` VALUES ('326', '325', '超薄电视', '-1', '1', '0', '1', 'www.taobao.com', '类别备注', '2021-03-29 16:47:12', null, null, null, '0');
INSERT INTO `t_category` VALUES ('327', '-1', '手机;运营商;数码', '/static/images/nav9.png', '1', '0', '1', 'www.taobao.com;www.jd.com;www.163.com', null, '2021-03-29 17:01:52', null, null, null, '0');
INSERT INTO `t_category` VALUES ('328', '327', '手机通讯', '-1', '1', '0', '1', 'www.taobao.com', null, '2021-03-29 17:03:24', null, null, null, '0');
INSERT INTO `t_category` VALUES ('329', '328', '手机', '-1', '1', '0', '1', 'www.taobao.com', null, '2021-03-29 17:05:04', null, null, null, '0');

-- ----------------------------
-- Table structure for t_category_area
-- ----------------------------
DROP TABLE IF EXISTS `t_category_area`;
CREATE TABLE `t_category_area` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '类别主键',
  `area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区编码',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='类别地区表';

-- ----------------------------
-- Records of t_category_area
-- ----------------------------
INSERT INTO `t_category_area` VALUES ('1', '324', '110000', '2021-03-29 17:15:22', null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('2', '325', '110000', '2021-03-29 17:16:24', null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('3', '326', '110000', '2021-03-29 21:40:06', null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('4', '327', '110000', '2021-03-29 21:40:20', null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('5', '328', '110000', '2021-03-29 21:40:28', null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('6', '329', '110000', '2021-03-29 21:40:37', null, null, null, '0');
