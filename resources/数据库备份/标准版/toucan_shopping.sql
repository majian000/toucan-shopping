/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80018
Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
Source Database       : toucan_shopping

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-15 21:37:09
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

-- ----------------------------
-- Table structure for t_area
-- ----------------------------
DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编码',
  `parent_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上级编码',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '市',
  `area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区县',
  `area_sort` bigint(20) unsigned DEFAULT '0' COMMENT '排序',
  `is_municipality` smallint(6) DEFAULT NULL COMMENT '是否直辖市 0:否 1:是',
  `type` smallint(5) unsigned DEFAULT '0' COMMENT '1省 2市 3区县',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7258 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='地区编码表';

-- ----------------------------
-- Records of t_area
-- ----------------------------
INSERT INTO `t_area` VALUES ('3630', '110000', '-1', '', '北京市', '', '3628', '1', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3631', '110101', '110000', '', '北京市', '东城区', '3627', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3632', '110102', '110000', '', '北京市', '西城区', '3626', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3633', '110105', '110000', '', '北京市', '朝阳区', '3625', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3634', '110106', '110000', '', '北京市', '丰台区', '3624', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3635', '110107', '110000', '', '北京市', '石景山区', '3623', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3636', '110108', '110000', '', '北京市', '海淀区', '3622', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3637', '110109', '110000', '', '北京市', '门头沟区', '3621', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3638', '110111', '110000', '', '北京市', '房山区', '3620', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3639', '110112', '110000', '', '北京市', '通州区', '3619', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3640', '110113', '110000', '', '北京市', '顺义区', '3618', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3641', '110114', '110000', '', '北京市', '昌平区', '3617', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3642', '110115', '110000', '', '北京市', '大兴区', '3616', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3643', '110116', '110000', '', '北京市', '怀柔区', '3615', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3644', '110117', '110000', '', '北京市', '平谷区', '3614', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3645', '110118', '110000', '', '北京市', '密云区', '3613', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3646', '110119', '110000', '', '北京市', '延庆区', '3612', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3647', '120000', '-1', '', '天津市', '', '3611', '1', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3648', '120101', '120000', '', '天津市', '和平区', '3610', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3649', '120102', '120000', '', '天津市', '河东区', '3609', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3650', '120103', '120000', '', '天津市', '河西区', '3608', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3651', '120104', '120000', '', '天津市', '南开区', '3607', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3652', '120105', '120000', '', '天津市', '河北区', '3606', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3653', '120106', '120000', '', '天津市', '红桥区', '3605', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3654', '120110', '120000', '', '天津市', '东丽区', '3604', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3655', '120111', '120000', '', '天津市', '西青区', '3603', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3656', '120112', '120000', '', '天津市', '津南区', '3602', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3657', '120113', '120000', '', '天津市', '北辰区', '3601', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3658', '120114', '120000', '', '天津市', '武清区', '3600', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3659', '120115', '120000', '', '天津市', '宝坻区', '3599', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3660', '120116', '120000', '', '天津市', '滨海新区', '3598', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3661', '120117', '120000', '', '天津市', '宁河区', '3597', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3662', '120118', '120000', '', '天津市', '静海区', '3596', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3663', '120119', '120000', '', '天津市', '蓟州区', '3595', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3664', '130000', '-1', '河北省', '', '', '3594', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3665', '130100', '130000', '河北省', '石家庄市', '', '3593', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3666', '130102', '130100', '河北省', '石家庄市', '长安区', '3592', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3667', '130104', '130100', '河北省', '石家庄市', '桥西区', '3591', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3668', '130105', '130100', '河北省', '石家庄市', '新华区', '3590', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3669', '130107', '130100', '河北省', '石家庄市', '井陉矿区', '3589', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3670', '130108', '130100', '河北省', '石家庄市', '裕华区', '3588', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3671', '130109', '130100', '河北省', '石家庄市', '藁城区', '3587', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3672', '130110', '130100', '河北省', '石家庄市', '鹿泉区', '3586', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3673', '130111', '130100', '河北省', '石家庄市', '栾城区', '3585', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3674', '130121', '130100', '河北省', '石家庄市', '井陉县', '3584', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3675', '130123', '130100', '河北省', '石家庄市', '正定县', '3583', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3676', '130125', '130100', '河北省', '石家庄市', '行唐县', '3582', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3677', '130126', '130100', '河北省', '石家庄市', '灵寿县', '3581', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3678', '130127', '130100', '河北省', '石家庄市', '高邑县', '3580', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3679', '130128', '130100', '河北省', '石家庄市', '深泽县', '3579', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3680', '130129', '130100', '河北省', '石家庄市', '赞皇县', '3578', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3681', '130130', '130100', '河北省', '石家庄市', '无极县', '3577', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3682', '130131', '130100', '河北省', '石家庄市', '平山县', '3576', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3683', '130132', '130100', '河北省', '石家庄市', '元氏县', '3575', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3684', '130133', '130100', '河北省', '石家庄市', '赵县', '3574', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3685', '130183', '130100', '河北省', '石家庄市', '晋州市', '3573', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3686', '130184', '130100', '河北省', '石家庄市', '新乐市', '3572', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3687', '130200', '130000', '河北省', '唐山市', '', '3571', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3688', '130202', '130200', '河北省', '唐山市', '路南区', '3570', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3689', '130203', '130200', '河北省', '唐山市', '路北区', '3569', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3690', '130204', '130200', '河北省', '唐山市', '古冶区', '3568', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3691', '130205', '130200', '河北省', '唐山市', '开平区', '3567', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3692', '130207', '130200', '河北省', '唐山市', '丰南区', '3566', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3693', '130208', '130200', '河北省', '唐山市', '丰润区', '3565', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3694', '130209', '130200', '河北省', '唐山市', '曹妃甸区', '3564', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3695', '130223', '130200', '河北省', '唐山市', '滦县', '3563', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3696', '130224', '130200', '河北省', '唐山市', '滦南县', '3562', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3697', '130225', '130200', '河北省', '唐山市', '乐亭县', '3561', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3698', '130227', '130200', '河北省', '唐山市', '迁西县', '3560', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3699', '130229', '130200', '河北省', '唐山市', '玉田县', '3559', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3700', '130281', '130200', '河北省', '唐山市', '遵化市', '3558', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3701', '130283', '130200', '河北省', '唐山市', '迁安市', '3557', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3702', '130300', '130000', '河北省', '秦皇岛市', '', '3556', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3703', '130302', '130300', '河北省', '秦皇岛市', '海港区', '3555', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3704', '130303', '130300', '河北省', '秦皇岛市', '山海关区', '3554', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3705', '130304', '130300', '河北省', '秦皇岛市', '北戴河区', '3553', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3706', '130306', '130300', '河北省', '秦皇岛市', '抚宁区', '3552', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3707', '130321', '130300', '河北省', '秦皇岛市', '青龙满族自治县', '3551', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3708', '130322', '130300', '河北省', '秦皇岛市', '昌黎县', '3550', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3709', '130324', '130300', '河北省', '秦皇岛市', '卢龙县', '3549', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3710', '130400', '130000', '河北省', '邯郸市', '', '3548', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3711', '130402', '130400', '河北省', '邯郸市', '邯山区', '3547', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3712', '130403', '130400', '河北省', '邯郸市', '丛台区', '3546', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3713', '130404', '130400', '河北省', '邯郸市', '复兴区', '3545', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3714', '130406', '130400', '河北省', '邯郸市', '峰峰矿区', '3544', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3715', '130407', '130400', '河北省', '邯郸市', '肥乡区', '3543', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3716', '130408', '130400', '河北省', '邯郸市', '永年区', '3542', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3717', '130423', '130400', '河北省', '邯郸市', '临漳县', '3541', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3718', '130424', '130400', '河北省', '邯郸市', '成安县', '3540', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3719', '130425', '130400', '河北省', '邯郸市', '大名县', '3539', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3720', '130426', '130400', '河北省', '邯郸市', '涉县', '3538', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3721', '130427', '130400', '河北省', '邯郸市', '磁县', '3537', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3722', '130430', '130400', '河北省', '邯郸市', '邱县', '3536', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3723', '130431', '130400', '河北省', '邯郸市', '鸡泽县', '3535', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3724', '130432', '130400', '河北省', '邯郸市', '广平县', '3534', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3725', '130433', '130400', '河北省', '邯郸市', '馆陶县', '3533', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3726', '130434', '130400', '河北省', '邯郸市', '魏县', '3532', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3727', '130435', '130400', '河北省', '邯郸市', '曲周县', '3531', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3728', '130481', '130400', '河北省', '邯郸市', '武安市', '3530', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3729', '130500', '130000', '河北省', '邢台市', '', '3529', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3730', '130502', '130500', '河北省', '邢台市', '桥东区', '3528', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3731', '130503', '130500', '河北省', '邢台市', '桥西区', '3527', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3732', '130521', '130500', '河北省', '邢台市', '邢台县', '3526', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3733', '130522', '130500', '河北省', '邢台市', '临城县', '3525', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3734', '130523', '130500', '河北省', '邢台市', '内丘县', '3524', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3735', '130524', '130500', '河北省', '邢台市', '柏乡县', '3523', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3736', '130525', '130500', '河北省', '邢台市', '隆尧县', '3522', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3737', '130526', '130500', '河北省', '邢台市', '任县', '3521', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3738', '130527', '130500', '河北省', '邢台市', '南和县', '3520', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3739', '130528', '130500', '河北省', '邢台市', '宁晋县', '3519', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3740', '130529', '130500', '河北省', '邢台市', '巨鹿县', '3518', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3741', '130530', '130500', '河北省', '邢台市', '新河县', '3517', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3742', '130531', '130500', '河北省', '邢台市', '广宗县', '3516', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3743', '130532', '130500', '河北省', '邢台市', '平乡县', '3515', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3744', '130533', '130500', '河北省', '邢台市', '威县', '3514', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3745', '130534', '130500', '河北省', '邢台市', '清河县', '3513', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3746', '130535', '130500', '河北省', '邢台市', '临西县', '3512', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3747', '130581', '130500', '河北省', '邢台市', '南宫市', '3511', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3748', '130582', '130500', '河北省', '邢台市', '沙河市', '3510', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3749', '130600', '130000', '河北省', '保定市', '', '3509', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3750', '130602', '130600', '河北省', '保定市', '竞秀区', '3508', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3751', '130606', '130600', '河北省', '保定市', '莲池区', '3507', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3752', '130607', '130600', '河北省', '保定市', '满城区', '3506', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3753', '130608', '130600', '河北省', '保定市', '清苑区', '3505', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3754', '130609', '130600', '河北省', '保定市', '徐水区', '3504', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3755', '130623', '130600', '河北省', '保定市', '涞水县', '3503', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3756', '130624', '130600', '河北省', '保定市', '阜平县', '3502', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3757', '130626', '130600', '河北省', '保定市', '定兴县', '3501', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3758', '130627', '130600', '河北省', '保定市', '唐县', '3500', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3759', '130628', '130600', '河北省', '保定市', '高阳县', '3499', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3760', '130629', '130600', '河北省', '保定市', '容城县', '3498', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3761', '130630', '130600', '河北省', '保定市', '涞源县', '3497', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3762', '130631', '130600', '河北省', '保定市', '望都县', '3496', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3763', '130632', '130600', '河北省', '保定市', '安新县', '3495', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3764', '130633', '130600', '河北省', '保定市', '易县', '3494', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3765', '130634', '130600', '河北省', '保定市', '曲阳县', '3493', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3766', '130635', '130600', '河北省', '保定市', '蠡县', '3492', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3767', '130636', '130600', '河北省', '保定市', '顺平县', '3491', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3768', '130637', '130600', '河北省', '保定市', '博野县', '3490', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3769', '130638', '130600', '河北省', '保定市', '雄县', '3489', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3770', '130681', '130600', '河北省', '保定市', '涿州市', '3488', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3771', '130683', '130600', '河北省', '保定市', '安国市', '3487', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3772', '130684', '130600', '河北省', '保定市', '高碑店市', '3486', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3773', '130700', '130000', '河北省', '张家口市', '', '3485', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3774', '130702', '130700', '河北省', '张家口市', '桥东区', '3484', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3775', '130703', '130700', '河北省', '张家口市', '桥西区', '3483', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3776', '130705', '130700', '河北省', '张家口市', '宣化区', '3482', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3777', '130706', '130700', '河北省', '张家口市', '下花园区', '3481', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3778', '130708', '130700', '河北省', '张家口市', '万全区', '3480', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3779', '130709', '130700', '河北省', '张家口市', '崇礼区', '3479', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3780', '130722', '130700', '河北省', '张家口市', '张北县', '3478', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3781', '130723', '130700', '河北省', '张家口市', '康保县', '3477', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3782', '130724', '130700', '河北省', '张家口市', '沽源县', '3476', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3783', '130725', '130700', '河北省', '张家口市', '尚义县', '3475', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3784', '130726', '130700', '河北省', '张家口市', '蔚县', '3474', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3785', '130727', '130700', '河北省', '张家口市', '阳原县', '3473', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3786', '130728', '130700', '河北省', '张家口市', '怀安县', '3472', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3787', '130730', '130700', '河北省', '张家口市', '怀来县', '3471', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3788', '130731', '130700', '河北省', '张家口市', '涿鹿县', '3470', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3789', '130732', '130700', '河北省', '张家口市', '赤城县', '3469', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3790', '130800', '130000', '河北省', '承德市', '', '3468', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3791', '130802', '130800', '河北省', '承德市', '双桥区', '3467', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3792', '130803', '130800', '河北省', '承德市', '双滦区', '3466', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3793', '130804', '130800', '河北省', '承德市', '鹰手营子矿区', '3465', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3794', '130821', '130800', '河北省', '承德市', '承德县', '3464', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3795', '130822', '130800', '河北省', '承德市', '兴隆县', '3463', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3796', '130824', '130800', '河北省', '承德市', '滦平县', '3462', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3797', '130825', '130800', '河北省', '承德市', '隆化县', '3461', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3798', '130826', '130800', '河北省', '承德市', '丰宁满族自治县', '3460', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3799', '130827', '130800', '河北省', '承德市', '宽城满族自治县', '3459', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3800', '130828', '130800', '河北省', '承德市', '围场满族蒙古族自治县', '3458', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3801', '130881', '130800', '河北省', '承德市', '平泉市', '3457', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3802', '130900', '130000', '河北省', '沧州市', '', '3456', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3803', '130902', '130900', '河北省', '沧州市', '新华区', '3455', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3804', '130903', '130900', '河北省', '沧州市', '运河区', '3454', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3805', '130921', '130900', '河北省', '沧州市', '沧县', '3453', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3806', '130922', '130900', '河北省', '沧州市', '青县', '3452', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3807', '130923', '130900', '河北省', '沧州市', '东光县', '3451', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3808', '130924', '130900', '河北省', '沧州市', '海兴县', '3450', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3809', '130925', '130900', '河北省', '沧州市', '盐山县', '3449', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3810', '130926', '130900', '河北省', '沧州市', '肃宁县', '3448', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3811', '130927', '130900', '河北省', '沧州市', '南皮县', '3447', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3812', '130928', '130900', '河北省', '沧州市', '吴桥县', '3446', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3813', '130929', '130900', '河北省', '沧州市', '献县', '3445', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3814', '130930', '130900', '河北省', '沧州市', '孟村回族自治县', '3444', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3815', '130981', '130900', '河北省', '沧州市', '泊头市', '3443', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3816', '130982', '130900', '河北省', '沧州市', '任丘市', '3442', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3817', '130983', '130900', '河北省', '沧州市', '黄骅市', '3441', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3818', '130984', '130900', '河北省', '沧州市', '河间市', '3440', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3819', '131000', '130000', '河北省', '廊坊市', '', '3439', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3820', '131002', '131000', '河北省', '廊坊市', '安次区', '3438', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3821', '131003', '131000', '河北省', '廊坊市', '广阳区', '3437', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3822', '131022', '131000', '河北省', '廊坊市', '固安县', '3436', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3823', '131023', '131000', '河北省', '廊坊市', '永清县', '3435', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3824', '131024', '131000', '河北省', '廊坊市', '香河县', '3434', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3825', '131025', '131000', '河北省', '廊坊市', '大城县', '3433', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3826', '131026', '131000', '河北省', '廊坊市', '文安县', '3432', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3827', '131028', '131000', '河北省', '廊坊市', '大厂回族自治县', '3431', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3828', '131081', '131000', '河北省', '廊坊市', '霸州市', '3430', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3829', '131082', '131000', '河北省', '廊坊市', '三河市', '3429', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3830', '131100', '130000', '河北省', '衡水市', '', '3428', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3831', '131102', '131100', '河北省', '衡水市', '桃城区', '3427', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3832', '131103', '131100', '河北省', '衡水市', '冀州区', '3426', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3833', '131121', '131100', '河北省', '衡水市', '枣强县', '3425', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3834', '131122', '131100', '河北省', '衡水市', '武邑县', '3424', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3835', '131123', '131100', '河北省', '衡水市', '武强县', '3423', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3836', '131124', '131100', '河北省', '衡水市', '饶阳县', '3422', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3837', '131125', '131100', '河北省', '衡水市', '安平县', '3421', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3838', '131126', '131100', '河北省', '衡水市', '故城县', '3420', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3839', '131127', '131100', '河北省', '衡水市', '景县', '3419', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3840', '131128', '131100', '河北省', '衡水市', '阜城县', '3418', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3841', '131182', '131100', '河北省', '衡水市', '深州市', '3417', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3842', '139001', 'null', '河北省', '定州市', '定州市', '3416', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3843', '139002', 'null', '河北省', '辛集市', '辛集市', '3415', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3844', '140000', '-1', '山西省', '', '', '3414', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3845', '140100', '140000', '山西省', '太原市', '', '3413', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3846', '140105', '140100', '山西省', '太原市', '小店区', '3412', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3847', '140106', '140100', '山西省', '太原市', '迎泽区', '3411', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3848', '140107', '140100', '山西省', '太原市', '杏花岭区', '3410', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3849', '140108', '140100', '山西省', '太原市', '尖草坪区', '3409', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3850', '140109', '140100', '山西省', '太原市', '万柏林区', '3408', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3851', '140110', '140100', '山西省', '太原市', '晋源区', '3407', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3852', '140121', '140100', '山西省', '太原市', '清徐县', '3406', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3853', '140122', '140100', '山西省', '太原市', '阳曲县', '3405', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3854', '140123', '140100', '山西省', '太原市', '娄烦县', '3404', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3855', '140181', '140100', '山西省', '太原市', '古交市', '3403', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3856', '140200', '140000', '山西省', '大同市', '', '3402', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3857', '140202', '140200', '山西省', '大同市', '城区', '3401', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3858', '140203', '140200', '山西省', '大同市', '矿区', '3400', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3859', '140211', '140200', '山西省', '大同市', '南郊区', '3399', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3860', '140212', '140200', '山西省', '大同市', '新荣区', '3398', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3861', '140221', '140200', '山西省', '大同市', '阳高县', '3397', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3862', '140222', '140200', '山西省', '大同市', '天镇县', '3396', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3863', '140223', '140200', '山西省', '大同市', '广灵县', '3395', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3864', '140224', '140200', '山西省', '大同市', '灵丘县', '3394', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3865', '140225', '140200', '山西省', '大同市', '浑源县', '3393', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3866', '140226', '140200', '山西省', '大同市', '左云县', '3392', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3867', '140227', '140200', '山西省', '大同市', '大同县', '3391', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3868', '140300', '140000', '山西省', '阳泉市', '', '3390', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3869', '140302', '140300', '山西省', '阳泉市', '城区', '3389', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3870', '140303', '140300', '山西省', '阳泉市', '矿区', '3388', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3871', '140311', '140300', '山西省', '阳泉市', '郊区', '3387', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3872', '140321', '140300', '山西省', '阳泉市', '平定县', '3386', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3873', '140322', '140300', '山西省', '阳泉市', '盂县', '3385', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3874', '140400', '140000', '山西省', '长治市', '', '3384', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3875', '140402', '140400', '山西省', '长治市', '城区', '3383', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3876', '140411', '140400', '山西省', '长治市', '郊区', '3382', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3877', '140421', '140400', '山西省', '长治市', '长治县', '3381', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3878', '140423', '140400', '山西省', '长治市', '襄垣县', '3380', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3879', '140424', '140400', '山西省', '长治市', '屯留县', '3379', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3880', '140425', '140400', '山西省', '长治市', '平顺县', '3378', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3881', '140426', '140400', '山西省', '长治市', '黎城县', '3377', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3882', '140427', '140400', '山西省', '长治市', '壶关县', '3376', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3883', '140428', '140400', '山西省', '长治市', '长子县', '3375', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3884', '140429', '140400', '山西省', '长治市', '武乡县', '3374', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3885', '140430', '140400', '山西省', '长治市', '沁县', '3373', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3886', '140431', '140400', '山西省', '长治市', '沁源县', '3372', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3887', '140481', '140400', '山西省', '长治市', '潞城市', '3371', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3888', '140500', '140000', '山西省', '晋城市', '', '3370', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3889', '140502', '140500', '山西省', '晋城市', '城区', '3369', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3890', '140521', '140500', '山西省', '晋城市', '沁水县', '3368', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3891', '140522', '140500', '山西省', '晋城市', '阳城县', '3367', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3892', '140524', '140500', '山西省', '晋城市', '陵川县', '3366', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3893', '140525', '140500', '山西省', '晋城市', '泽州县', '3365', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3894', '140581', '140500', '山西省', '晋城市', '高平市', '3364', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3895', '140600', '140000', '山西省', '朔州市', '', '3363', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3896', '140602', '140600', '山西省', '朔州市', '朔城区', '3362', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3897', '140603', '140600', '山西省', '朔州市', '平鲁区', '3361', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3898', '140621', '140600', '山西省', '朔州市', '山阴县', '3360', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3899', '140622', '140600', '山西省', '朔州市', '应县', '3359', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3900', '140623', '140600', '山西省', '朔州市', '右玉县', '3358', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3901', '140624', '140600', '山西省', '朔州市', '怀仁县', '3357', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3902', '140700', '140000', '山西省', '晋中市', '', '3356', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3903', '140702', '140700', '山西省', '晋中市', '榆次区', '3355', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3904', '140721', '140700', '山西省', '晋中市', '榆社县', '3354', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3905', '140722', '140700', '山西省', '晋中市', '左权县', '3353', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3906', '140723', '140700', '山西省', '晋中市', '和顺县', '3352', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3907', '140724', '140700', '山西省', '晋中市', '昔阳县', '3351', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3908', '140725', '140700', '山西省', '晋中市', '寿阳县', '3350', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3909', '140726', '140700', '山西省', '晋中市', '太谷县', '3349', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3910', '140727', '140700', '山西省', '晋中市', '祁县', '3348', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3911', '140728', '140700', '山西省', '晋中市', '平遥县', '3347', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3912', '140729', '140700', '山西省', '晋中市', '灵石县', '3346', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3913', '140781', '140700', '山西省', '晋中市', '介休市', '3345', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3914', '140800', '140000', '山西省', '运城市', '', '3344', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3915', '140802', '140800', '山西省', '运城市', '盐湖区', '3343', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3916', '140821', '140800', '山西省', '运城市', '临猗县', '3342', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3917', '140822', '140800', '山西省', '运城市', '万荣县', '3341', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3918', '140823', '140800', '山西省', '运城市', '闻喜县', '3340', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3919', '140824', '140800', '山西省', '运城市', '稷山县', '3339', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3920', '140825', '140800', '山西省', '运城市', '新绛县', '3338', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3921', '140826', '140800', '山西省', '运城市', '绛县', '3337', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3922', '140827', '140800', '山西省', '运城市', '垣曲县', '3336', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3923', '140828', '140800', '山西省', '运城市', '夏县', '3335', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3924', '140829', '140800', '山西省', '运城市', '平陆县', '3334', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3925', '140830', '140800', '山西省', '运城市', '芮城县', '3333', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3926', '140881', '140800', '山西省', '运城市', '永济市', '3332', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3927', '140882', '140800', '山西省', '运城市', '河津市', '3331', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3928', '140900', '140000', '山西省', '忻州市', '', '3330', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3929', '140902', '140900', '山西省', '忻州市', '忻府区', '3329', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3930', '140921', '140900', '山西省', '忻州市', '定襄县', '3328', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3931', '140922', '140900', '山西省', '忻州市', '五台县', '3327', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3932', '140923', '140900', '山西省', '忻州市', '代县', '3326', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3933', '140924', '140900', '山西省', '忻州市', '繁峙县', '3325', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3934', '140925', '140900', '山西省', '忻州市', '宁武县', '3324', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3935', '140926', '140900', '山西省', '忻州市', '静乐县', '3323', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3936', '140927', '140900', '山西省', '忻州市', '神池县', '3322', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3937', '140928', '140900', '山西省', '忻州市', '五寨县', '3321', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3938', '140929', '140900', '山西省', '忻州市', '岢岚县', '3320', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3939', '140930', '140900', '山西省', '忻州市', '河曲县', '3319', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3940', '140931', '140900', '山西省', '忻州市', '保德县', '3318', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3941', '140932', '140900', '山西省', '忻州市', '偏关县', '3317', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3942', '140981', '140900', '山西省', '忻州市', '原平市', '3316', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3943', '141000', '140000', '山西省', '临汾市', '', '3315', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3944', '141002', '141000', '山西省', '临汾市', '尧都区', '3314', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3945', '141021', '141000', '山西省', '临汾市', '曲沃县', '3313', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3946', '141022', '141000', '山西省', '临汾市', '翼城县', '3312', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3947', '141023', '141000', '山西省', '临汾市', '襄汾县', '3311', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3948', '141024', '141000', '山西省', '临汾市', '洪洞县', '3310', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3949', '141025', '141000', '山西省', '临汾市', '古县', '3309', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3950', '141026', '141000', '山西省', '临汾市', '安泽县', '3308', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3951', '141027', '141000', '山西省', '临汾市', '浮山县', '3307', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3952', '141028', '141000', '山西省', '临汾市', '吉县', '3306', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3953', '141029', '141000', '山西省', '临汾市', '乡宁县', '3305', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3954', '141030', '141000', '山西省', '临汾市', '大宁县', '3304', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3955', '141031', '141000', '山西省', '临汾市', '隰县', '3303', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3956', '141032', '141000', '山西省', '临汾市', '永和县', '3302', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3957', '141033', '141000', '山西省', '临汾市', '蒲县', '3301', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3958', '141034', '141000', '山西省', '临汾市', '汾西县', '3300', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3959', '141081', '141000', '山西省', '临汾市', '侯马市', '3299', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3960', '141082', '141000', '山西省', '临汾市', '霍州市', '3298', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3961', '141100', '140000', '山西省', '吕梁市', '', '3297', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3962', '141102', '141100', '山西省', '吕梁市', '离石区', '3296', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3963', '141121', '141100', '山西省', '吕梁市', '文水县', '3295', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3964', '141122', '141100', '山西省', '吕梁市', '交城县', '3294', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3965', '141123', '141100', '山西省', '吕梁市', '兴县', '3293', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3966', '141124', '141100', '山西省', '吕梁市', '临县', '3292', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3967', '141125', '141100', '山西省', '吕梁市', '柳林县', '3291', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3968', '141126', '141100', '山西省', '吕梁市', '石楼县', '3290', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3969', '141127', '141100', '山西省', '吕梁市', '岚县', '3289', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3970', '141128', '141100', '山西省', '吕梁市', '方山县', '3288', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3971', '141129', '141100', '山西省', '吕梁市', '中阳县', '3287', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3972', '141130', '141100', '山西省', '吕梁市', '交口县', '3286', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3973', '141181', '141100', '山西省', '吕梁市', '孝义市', '3285', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3974', '141182', '141100', '山西省', '吕梁市', '汾阳市', '3284', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3975', '150000', '-1', '内蒙古自治区', '', '', '3283', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3976', '150100', '150000', '内蒙古自治区', '呼和浩特市', '', '3282', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3977', '150102', '150100', '内蒙古自治区', '呼和浩特市', '新城区', '3281', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3978', '150103', '150100', '内蒙古自治区', '呼和浩特市', '回民区', '3280', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3979', '150104', '150100', '内蒙古自治区', '呼和浩特市', '玉泉区', '3279', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3980', '150105', '150100', '内蒙古自治区', '呼和浩特市', '赛罕区', '3278', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3981', '150121', '150100', '内蒙古自治区', '呼和浩特市', '土默特左旗', '3277', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3982', '150122', '150100', '内蒙古自治区', '呼和浩特市', '托克托县', '3276', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3983', '150123', '150100', '内蒙古自治区', '呼和浩特市', '和林格尔县', '3275', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3984', '150124', '150100', '内蒙古自治区', '呼和浩特市', '清水河县', '3274', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3985', '150125', '150100', '内蒙古自治区', '呼和浩特市', '武川县', '3273', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3986', '150200', '150000', '内蒙古自治区', '包头市', '', '3272', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3987', '150202', '150200', '内蒙古自治区', '包头市', '东河区', '3271', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3988', '150203', '150200', '内蒙古自治区', '包头市', '昆都仑区', '3270', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3989', '150204', '150200', '内蒙古自治区', '包头市', '青山区', '3269', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3990', '150205', '150200', '内蒙古自治区', '包头市', '石拐区', '3268', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3991', '150206', '150200', '内蒙古自治区', '包头市', '白云鄂博矿区', '3267', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3992', '150207', '150200', '内蒙古自治区', '包头市', '九原区', '3266', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3993', '150221', '150200', '内蒙古自治区', '包头市', '土默特右旗', '3265', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3994', '150222', '150200', '内蒙古自治区', '包头市', '固阳县', '3264', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3995', '150223', '150200', '内蒙古自治区', '包头市', '达尔罕茂明安联合旗', '3263', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3996', '150300', '150000', '内蒙古自治区', '乌海市', '', '3262', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3997', '150302', '150300', '内蒙古自治区', '乌海市', '海勃湾区', '3261', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3998', '150303', '150300', '内蒙古自治区', '乌海市', '海南区', '3260', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('3999', '150304', '150300', '内蒙古自治区', '乌海市', '乌达区', '3259', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4000', '150400', '150000', '内蒙古自治区', '赤峰市', '', '3258', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4001', '150402', '150400', '内蒙古自治区', '赤峰市', '红山区', '3257', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4002', '150403', '150400', '内蒙古自治区', '赤峰市', '元宝山区', '3256', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4003', '150404', '150400', '内蒙古自治区', '赤峰市', '松山区', '3255', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4004', '150421', '150400', '内蒙古自治区', '赤峰市', '阿鲁科尔沁旗', '3254', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4005', '150422', '150400', '内蒙古自治区', '赤峰市', '巴林左旗', '3253', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4006', '150423', '150400', '内蒙古自治区', '赤峰市', '巴林右旗', '3252', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4007', '150424', '150400', '内蒙古自治区', '赤峰市', '林西县', '3251', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4008', '150425', '150400', '内蒙古自治区', '赤峰市', '克什克腾旗', '3250', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4009', '150426', '150400', '内蒙古自治区', '赤峰市', '翁牛特旗', '3249', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4010', '150428', '150400', '内蒙古自治区', '赤峰市', '喀喇沁旗', '3248', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4011', '150429', '150400', '内蒙古自治区', '赤峰市', '宁城县', '3247', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4012', '150430', '150400', '内蒙古自治区', '赤峰市', '敖汉旗', '3246', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4013', '150500', '150000', '内蒙古自治区', '通辽市', '', '3245', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4014', '150502', '150500', '内蒙古自治区', '通辽市', '科尔沁区', '3244', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4015', '150521', '150500', '内蒙古自治区', '通辽市', '科尔沁左翼中旗', '3243', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4016', '150522', '150500', '内蒙古自治区', '通辽市', '科尔沁左翼后旗', '3242', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4017', '150523', '150500', '内蒙古自治区', '通辽市', '开鲁县', '3241', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4018', '150524', '150500', '内蒙古自治区', '通辽市', '库伦旗', '3240', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4019', '150525', '150500', '内蒙古自治区', '通辽市', '奈曼旗', '3239', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4020', '150526', '150500', '内蒙古自治区', '通辽市', '扎鲁特旗', '3238', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4021', '150581', '150500', '内蒙古自治区', '通辽市', '霍林郭勒市', '3237', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4022', '150600', '150000', '内蒙古自治区', '鄂尔多斯市', '', '3236', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4023', '150602', '150600', '内蒙古自治区', '鄂尔多斯市', '东胜区', '3235', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4024', '150603', '150600', '内蒙古自治区', '鄂尔多斯市', '康巴什区', '3234', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4025', '150621', '150600', '内蒙古自治区', '鄂尔多斯市', '达拉特旗', '3233', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4026', '150622', '150600', '内蒙古自治区', '鄂尔多斯市', '准格尔旗', '3232', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4027', '150623', '150600', '内蒙古自治区', '鄂尔多斯市', '鄂托克前旗', '3231', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4028', '150624', '150600', '内蒙古自治区', '鄂尔多斯市', '鄂托克旗', '3230', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4029', '150625', '150600', '内蒙古自治区', '鄂尔多斯市', '杭锦旗', '3229', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4030', '150626', '150600', '内蒙古自治区', '鄂尔多斯市', '乌审旗', '3228', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4031', '150627', '150600', '内蒙古自治区', '鄂尔多斯市', '伊金霍洛旗', '3227', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4032', '150700', '150000', '内蒙古自治区', '呼伦贝尔市', '', '3226', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4033', '150702', '150700', '内蒙古自治区', '呼伦贝尔市', '海拉尔区', '3225', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4034', '150703', '150700', '内蒙古自治区', '呼伦贝尔市', '扎赉诺尔区', '3224', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4035', '150721', '150700', '内蒙古自治区', '呼伦贝尔市', '阿荣旗', '3223', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4036', '150722', '150700', '内蒙古自治区', '呼伦贝尔市', '莫力达瓦达斡尔族自治旗', '3222', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4037', '150723', '150700', '内蒙古自治区', '呼伦贝尔市', '鄂伦春自治旗', '3221', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4038', '150724', '150700', '内蒙古自治区', '呼伦贝尔市', '鄂温克族自治旗', '3220', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4039', '150725', '150700', '内蒙古自治区', '呼伦贝尔市', '陈巴尔虎旗', '3219', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4040', '150726', '150700', '内蒙古自治区', '呼伦贝尔市', '新巴尔虎左旗', '3218', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4041', '150727', '150700', '内蒙古自治区', '呼伦贝尔市', '新巴尔虎右旗', '3217', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4042', '150781', '150700', '内蒙古自治区', '呼伦贝尔市', '满洲里市', '3216', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4043', '150782', '150700', '内蒙古自治区', '呼伦贝尔市', '牙克石市', '3215', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4044', '150783', '150700', '内蒙古自治区', '呼伦贝尔市', '扎兰屯市', '3214', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4045', '150784', '150700', '内蒙古自治区', '呼伦贝尔市', '额尔古纳市', '3213', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4046', '150785', '150700', '内蒙古自治区', '呼伦贝尔市', '根河市', '3212', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4047', '150800', '150000', '内蒙古自治区', '巴彦淖尔市', '', '3211', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4048', '150802', '150800', '内蒙古自治区', '巴彦淖尔市', '临河区', '3210', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4049', '150821', '150800', '内蒙古自治区', '巴彦淖尔市', '五原县', '3209', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4050', '150822', '150800', '内蒙古自治区', '巴彦淖尔市', '磴口县', '3208', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4051', '150823', '150800', '内蒙古自治区', '巴彦淖尔市', '乌拉特前旗', '3207', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4052', '150824', '150800', '内蒙古自治区', '巴彦淖尔市', '乌拉特中旗', '3206', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4053', '150825', '150800', '内蒙古自治区', '巴彦淖尔市', '乌拉特后旗', '3205', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4054', '150826', '150800', '内蒙古自治区', '巴彦淖尔市', '杭锦后旗', '3204', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4055', '150900', '150000', '内蒙古自治区', '乌兰察布市', '', '3203', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4056', '150902', '150900', '内蒙古自治区', '乌兰察布市', '集宁区', '3202', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4057', '150921', '150900', '内蒙古自治区', '乌兰察布市', '卓资县', '3201', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4058', '150922', '150900', '内蒙古自治区', '乌兰察布市', '化德县', '3200', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4059', '150923', '150900', '内蒙古自治区', '乌兰察布市', '商都县', '3199', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4060', '150924', '150900', '内蒙古自治区', '乌兰察布市', '兴和县', '3198', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4061', '150925', '150900', '内蒙古自治区', '乌兰察布市', '凉城县', '3197', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4062', '150926', '150900', '内蒙古自治区', '乌兰察布市', '察哈尔右翼前旗', '3196', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4063', '150927', '150900', '内蒙古自治区', '乌兰察布市', '察哈尔右翼中旗', '3195', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4064', '150928', '150900', '内蒙古自治区', '乌兰察布市', '察哈尔右翼后旗', '3194', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4065', '150929', '150900', '内蒙古自治区', '乌兰察布市', '四子王旗', '3193', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4066', '150981', '150900', '内蒙古自治区', '乌兰察布市', '丰镇市', '3192', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4067', '152200', '150000', '内蒙古自治区', '兴安盟', '', '3191', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4068', '152201', '152200', '内蒙古自治区', '兴安盟', '乌兰浩特市', '3190', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4069', '152202', '152200', '内蒙古自治区', '兴安盟', '阿尔山市', '3189', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4070', '152221', '152200', '内蒙古自治区', '兴安盟', '科尔沁右翼前旗', '3188', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4071', '152222', '152200', '内蒙古自治区', '兴安盟', '科尔沁右翼中旗', '3187', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4072', '152223', '152200', '内蒙古自治区', '兴安盟', '扎赉特旗', '3186', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4073', '152224', '152200', '内蒙古自治区', '兴安盟', '突泉县', '3185', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4074', '152500', '150000', '内蒙古自治区', '锡林郭勒盟', '', '3184', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4075', '152501', '152500', '内蒙古自治区', '锡林郭勒盟', '二连浩特市', '3183', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4076', '152502', '152500', '内蒙古自治区', '锡林郭勒盟', '锡林浩特市', '3182', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4077', '152522', '152500', '内蒙古自治区', '锡林郭勒盟', '阿巴嘎旗', '3181', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4078', '152523', '152500', '内蒙古自治区', '锡林郭勒盟', '苏尼特左旗', '3180', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4079', '152524', '152500', '内蒙古自治区', '锡林郭勒盟', '苏尼特右旗', '3179', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4080', '152525', '152500', '内蒙古自治区', '锡林郭勒盟', '东乌珠穆沁旗', '3178', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4081', '152526', '152500', '内蒙古自治区', '锡林郭勒盟', '西乌珠穆沁旗', '3177', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4082', '152527', '152500', '内蒙古自治区', '锡林郭勒盟', '太仆寺旗', '3176', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4083', '152528', '152500', '内蒙古自治区', '锡林郭勒盟', '镶黄旗', '3175', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4084', '152529', '152500', '内蒙古自治区', '锡林郭勒盟', '正镶白旗', '3174', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4085', '152530', '152500', '内蒙古自治区', '锡林郭勒盟', '正蓝旗', '3173', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4086', '152531', '152500', '内蒙古自治区', '锡林郭勒盟', '多伦县', '3172', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4087', '152900', '150000', '内蒙古自治区', '阿拉善盟', '', '3171', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4088', '152921', '152900', '内蒙古自治区', '阿拉善盟', '阿拉善左旗', '3170', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4089', '152922', '152900', '内蒙古自治区', '阿拉善盟', '阿拉善右旗', '3169', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4090', '152923', '152900', '内蒙古自治区', '阿拉善盟', '额济纳旗', '3168', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4091', '210000', '-1', '辽宁省', '', '', '3167', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4092', '210100', '210000', '辽宁省', '沈阳市', '', '3166', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4093', '210102', '210100', '辽宁省', '沈阳市', '和平区', '3165', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4094', '210103', '210100', '辽宁省', '沈阳市', '沈河区', '3164', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4095', '210104', '210100', '辽宁省', '沈阳市', '大东区', '3163', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4096', '210105', '210100', '辽宁省', '沈阳市', '皇姑区', '3162', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4097', '210106', '210100', '辽宁省', '沈阳市', '铁西区', '3161', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4098', '210111', '210100', '辽宁省', '沈阳市', '苏家屯区', '3160', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4099', '210112', '210100', '辽宁省', '沈阳市', '浑南区', '3159', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4100', '210113', '210100', '辽宁省', '沈阳市', '沈北新区', '3158', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4101', '210114', '210100', '辽宁省', '沈阳市', '于洪区', '3157', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4102', '210115', '210100', '辽宁省', '沈阳市', '辽中区', '3156', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4103', '210123', '210100', '辽宁省', '沈阳市', '康平县', '3155', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4104', '210124', '210100', '辽宁省', '沈阳市', '法库县', '3154', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4105', '210181', '210100', '辽宁省', '沈阳市', '新民市', '3153', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4106', '210200', '210000', '辽宁省', '大连市', '', '3152', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4107', '210202', '210200', '辽宁省', '大连市', '中山区', '3151', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4108', '210203', '210200', '辽宁省', '大连市', '西岗区', '3150', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4109', '210204', '210200', '辽宁省', '大连市', '沙河口区', '3149', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4110', '210211', '210200', '辽宁省', '大连市', '甘井子区', '3148', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4111', '210212', '210200', '辽宁省', '大连市', '旅顺口区', '3147', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4112', '210213', '210200', '辽宁省', '大连市', '金州区', '3146', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4113', '210214', '210200', '辽宁省', '大连市', '普兰店区', '3145', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4114', '210224', '210200', '辽宁省', '大连市', '长海县', '3144', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4115', '210281', '210200', '辽宁省', '大连市', '瓦房店市', '3143', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4116', '210283', '210200', '辽宁省', '大连市', '庄河市', '3142', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4117', '210300', '210000', '辽宁省', '鞍山市', '', '3141', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4118', '210302', '210300', '辽宁省', '鞍山市', '铁东区', '3140', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4119', '210303', '210300', '辽宁省', '鞍山市', '铁西区', '3139', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4120', '210304', '210300', '辽宁省', '鞍山市', '立山区', '3138', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4121', '210311', '210300', '辽宁省', '鞍山市', '千山区', '3137', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4122', '210321', '210300', '辽宁省', '鞍山市', '台安县', '3136', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4123', '210323', '210300', '辽宁省', '鞍山市', '岫岩满族自治县', '3135', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4124', '210381', '210300', '辽宁省', '鞍山市', '海城市', '3134', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4125', '210400', '210000', '辽宁省', '抚顺市', '', '3133', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4126', '210402', '210400', '辽宁省', '抚顺市', '新抚区', '3132', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4127', '210403', '210400', '辽宁省', '抚顺市', '东洲区', '3131', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4128', '210404', '210400', '辽宁省', '抚顺市', '望花区', '3130', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4129', '210411', '210400', '辽宁省', '抚顺市', '顺城区', '3129', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4130', '210421', '210400', '辽宁省', '抚顺市', '抚顺县', '3128', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4131', '210422', '210400', '辽宁省', '抚顺市', '新宾满族自治县', '3127', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4132', '210423', '210400', '辽宁省', '抚顺市', '清原满族自治县', '3126', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4133', '210500', '210000', '辽宁省', '本溪市', '', '3125', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4134', '210502', '210500', '辽宁省', '本溪市', '平山区', '3124', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4135', '210503', '210500', '辽宁省', '本溪市', '溪湖区', '3123', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4136', '210504', '210500', '辽宁省', '本溪市', '明山区', '3122', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4137', '210505', '210500', '辽宁省', '本溪市', '南芬区', '3121', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4138', '210521', '210500', '辽宁省', '本溪市', '本溪满族自治县', '3120', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4139', '210522', '210500', '辽宁省', '本溪市', '桓仁满族自治县', '3119', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4140', '210600', '210000', '辽宁省', '丹东市', '', '3118', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4141', '210602', '210600', '辽宁省', '丹东市', '元宝区', '3117', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4142', '210603', '210600', '辽宁省', '丹东市', '振兴区', '3116', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4143', '210604', '210600', '辽宁省', '丹东市', '振安区', '3115', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4144', '210624', '210600', '辽宁省', '丹东市', '宽甸满族自治县', '3114', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4145', '210681', '210600', '辽宁省', '丹东市', '东港市', '3113', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4146', '210682', '210600', '辽宁省', '丹东市', '凤城市', '3112', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4147', '210700', '210000', '辽宁省', '锦州市', '', '3111', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4148', '210702', '210700', '辽宁省', '锦州市', '古塔区', '3110', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4149', '210703', '210700', '辽宁省', '锦州市', '凌河区', '3109', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4150', '210711', '210700', '辽宁省', '锦州市', '太和区', '3108', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4151', '210726', '210700', '辽宁省', '锦州市', '黑山县', '3107', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4152', '210727', '210700', '辽宁省', '锦州市', '义县', '3106', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4153', '210781', '210700', '辽宁省', '锦州市', '凌海市', '3105', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4154', '210782', '210700', '辽宁省', '锦州市', '北镇市', '3104', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4155', '210800', '210000', '辽宁省', '营口市', '', '3103', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4156', '210802', '210800', '辽宁省', '营口市', '站前区', '3102', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4157', '210803', '210800', '辽宁省', '营口市', '西市区', '3101', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4158', '210804', '210800', '辽宁省', '营口市', '鲅鱼圈区', '3100', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4159', '210811', '210800', '辽宁省', '营口市', '老边区', '3099', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4160', '210881', '210800', '辽宁省', '营口市', '盖州市', '3098', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4161', '210882', '210800', '辽宁省', '营口市', '大石桥市', '3097', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4162', '210900', '210000', '辽宁省', '阜新市', '', '3096', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4163', '210902', '210900', '辽宁省', '阜新市', '海州区', '3095', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4164', '210903', '210900', '辽宁省', '阜新市', '新邱区', '3094', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4165', '210904', '210900', '辽宁省', '阜新市', '太平区', '3093', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4166', '210905', '210900', '辽宁省', '阜新市', '清河门区', '3092', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4167', '210911', '210900', '辽宁省', '阜新市', '细河区', '3091', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4168', '210921', '210900', '辽宁省', '阜新市', '阜新蒙古族自治县', '3090', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4169', '210922', '210900', '辽宁省', '阜新市', '彰武县', '3089', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4170', '211000', '210000', '辽宁省', '辽阳市', '', '3088', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4171', '211002', '211000', '辽宁省', '辽阳市', '白塔区', '3087', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4172', '211003', '211000', '辽宁省', '辽阳市', '文圣区', '3086', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4173', '211004', '211000', '辽宁省', '辽阳市', '宏伟区', '3085', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4174', '211005', '211000', '辽宁省', '辽阳市', '弓长岭区', '3084', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4175', '211011', '211000', '辽宁省', '辽阳市', '太子河区', '3083', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4176', '211021', '211000', '辽宁省', '辽阳市', '辽阳县', '3082', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4177', '211081', '211000', '辽宁省', '辽阳市', '灯塔市', '3081', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4178', '211100', '210000', '辽宁省', '盘锦市', '', '3080', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4179', '211102', '211100', '辽宁省', '盘锦市', '双台子区', '3079', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4180', '211103', '211100', '辽宁省', '盘锦市', '兴隆台区', '3078', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4181', '211104', '211100', '辽宁省', '盘锦市', '大洼区', '3077', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4182', '211122', '211100', '辽宁省', '盘锦市', '盘山县', '3076', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4183', '211200', '210000', '辽宁省', '铁岭市', '', '3075', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4184', '211202', '211200', '辽宁省', '铁岭市', '银州区', '3074', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4185', '211204', '211200', '辽宁省', '铁岭市', '清河区', '3073', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4186', '211221', '211200', '辽宁省', '铁岭市', '铁岭县', '3072', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4187', '211223', '211200', '辽宁省', '铁岭市', '西丰县', '3071', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4188', '211224', '211200', '辽宁省', '铁岭市', '昌图县', '3070', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4189', '211281', '211200', '辽宁省', '铁岭市', '调兵山市', '3069', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4190', '211282', '211200', '辽宁省', '铁岭市', '开原市', '3068', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4191', '211300', '210000', '辽宁省', '朝阳市', '', '3067', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4192', '211302', '211300', '辽宁省', '朝阳市', '双塔区', '3066', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4193', '211303', '211300', '辽宁省', '朝阳市', '龙城区', '3065', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4194', '211321', '211300', '辽宁省', '朝阳市', '朝阳县', '3064', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4195', '211322', '211300', '辽宁省', '朝阳市', '建平县', '3063', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4196', '211324', '211300', '辽宁省', '朝阳市', '喀喇沁左翼蒙古族自治县', '3062', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4197', '211381', '211300', '辽宁省', '朝阳市', '北票市', '3061', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4198', '211382', '211300', '辽宁省', '朝阳市', '凌源市', '3060', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4199', '211400', '210000', '辽宁省', '葫芦岛市', '', '3059', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4200', '211402', '211400', '辽宁省', '葫芦岛市', '连山区', '3058', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4201', '211403', '211400', '辽宁省', '葫芦岛市', '龙港区', '3057', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4202', '211404', '211400', '辽宁省', '葫芦岛市', '南票区', '3056', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4203', '211421', '211400', '辽宁省', '葫芦岛市', '绥中县', '3055', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4204', '211422', '211400', '辽宁省', '葫芦岛市', '建昌县', '3054', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4205', '211481', '211400', '辽宁省', '葫芦岛市', '兴城市', '3053', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4206', '220000', '-1', '吉林省', '', '', '3052', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4207', '220100', '220000', '吉林省', '长春市', '', '3051', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4208', '220102', '220100', '吉林省', '长春市', '南关区', '3050', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4209', '220103', '220100', '吉林省', '长春市', '宽城区', '3049', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4210', '220104', '220100', '吉林省', '长春市', '朝阳区', '3048', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4211', '220105', '220100', '吉林省', '长春市', '二道区', '3047', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4212', '220106', '220100', '吉林省', '长春市', '绿园区', '3046', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4213', '220112', '220100', '吉林省', '长春市', '双阳区', '3045', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4214', '220113', '220100', '吉林省', '长春市', '九台区', '3044', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4215', '220122', '220100', '吉林省', '长春市', '农安县', '3043', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4216', '220182', '220100', '吉林省', '长春市', '榆树市', '3042', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4217', '220183', '220100', '吉林省', '长春市', '德惠市', '3041', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4218', '220200', '220000', '吉林省', '吉林市', '', '3040', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4219', '220202', '220200', '吉林省', '吉林市', '昌邑区', '3039', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4220', '220203', '220200', '吉林省', '吉林市', '龙潭区', '3038', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4221', '220204', '220200', '吉林省', '吉林市', '船营区', '3037', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4222', '220211', '220200', '吉林省', '吉林市', '丰满区', '3036', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4223', '220221', '220200', '吉林省', '吉林市', '永吉县', '3035', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4224', '220281', '220200', '吉林省', '吉林市', '蛟河市', '3034', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4225', '220282', '220200', '吉林省', '吉林市', '桦甸市', '3033', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4226', '220283', '220200', '吉林省', '吉林市', '舒兰市', '3032', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4227', '220284', '220200', '吉林省', '吉林市', '磐石市', '3031', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4228', '220300', '220000', '吉林省', '四平市', '', '3030', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4229', '220302', '220300', '吉林省', '四平市', '铁西区', '3029', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4230', '220303', '220300', '吉林省', '四平市', '铁东区', '3028', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4231', '220322', '220300', '吉林省', '四平市', '梨树县', '3027', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4232', '220323', '220300', '吉林省', '四平市', '伊通满族自治县', '3026', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4233', '220381', '220300', '吉林省', '四平市', '公主岭市', '3025', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4234', '220382', '220300', '吉林省', '四平市', '双辽市', '3024', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4235', '220400', '220000', '吉林省', '辽源市', '', '3023', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4236', '220402', '220400', '吉林省', '辽源市', '龙山区', '3022', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4237', '220403', '220400', '吉林省', '辽源市', '西安区', '3021', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4238', '220421', '220400', '吉林省', '辽源市', '东丰县', '3020', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4239', '220422', '220400', '吉林省', '辽源市', '东辽县', '3019', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4240', '220500', '220000', '吉林省', '通化市', '', '3018', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4241', '220502', '220500', '吉林省', '通化市', '东昌区', '3017', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4242', '220503', '220500', '吉林省', '通化市', '二道江区', '3016', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4243', '220521', '220500', '吉林省', '通化市', '通化县', '3015', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4244', '220523', '220500', '吉林省', '通化市', '辉南县', '3014', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4245', '220524', '220500', '吉林省', '通化市', '柳河县', '3013', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4246', '220581', '220500', '吉林省', '通化市', '梅河口市', '3012', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4247', '220582', '220500', '吉林省', '通化市', '集安市', '3011', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4248', '220600', '220000', '吉林省', '白山市', '', '3010', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4249', '220602', '220600', '吉林省', '白山市', '浑江区', '3009', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4250', '220605', '220600', '吉林省', '白山市', '江源区', '3008', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4251', '220621', '220600', '吉林省', '白山市', '抚松县', '3007', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4252', '220622', '220600', '吉林省', '白山市', '靖宇县', '3006', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4253', '220623', '220600', '吉林省', '白山市', '长白朝鲜族自治县', '3005', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4254', '220681', '220600', '吉林省', '白山市', '临江市', '3004', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4255', '220700', '220000', '吉林省', '松原市', '', '3003', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4256', '220702', '220700', '吉林省', '松原市', '宁江区', '3002', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4257', '220721', '220700', '吉林省', '松原市', '前郭尔罗斯蒙古族自治县', '3001', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4258', '220722', '220700', '吉林省', '松原市', '长岭县', '3000', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4259', '220723', '220700', '吉林省', '松原市', '乾安县', '2999', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4260', '220781', '220700', '吉林省', '松原市', '扶余市', '2998', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4261', '220800', '220000', '吉林省', '白城市', '', '2997', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4262', '220802', '220800', '吉林省', '白城市', '洮北区', '2996', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4263', '220821', '220800', '吉林省', '白城市', '镇赉县', '2995', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4264', '220822', '220800', '吉林省', '白城市', '通榆县', '2994', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4265', '220881', '220800', '吉林省', '白城市', '洮南市', '2993', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4266', '220882', '220800', '吉林省', '白城市', '大安市', '2992', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4267', '222400', '220000', '吉林省', '延边朝鲜族自治州', '', '2991', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4268', '222401', '222400', '吉林省', '延边朝鲜族自治州', '延吉市', '2990', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4269', '222402', '222400', '吉林省', '延边朝鲜族自治州', '图们市', '2989', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4270', '222403', '222400', '吉林省', '延边朝鲜族自治州', '敦化市', '2988', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4271', '222404', '222400', '吉林省', '延边朝鲜族自治州', '珲春市', '2987', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4272', '222405', '222400', '吉林省', '延边朝鲜族自治州', '龙井市', '2986', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4273', '222406', '222400', '吉林省', '延边朝鲜族自治州', '和龙市', '2985', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4274', '222424', '222400', '吉林省', '延边朝鲜族自治州', '汪清县', '2984', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4275', '222426', '222400', '吉林省', '延边朝鲜族自治州', '安图县', '2983', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4276', '230000', '-1', '黑龙江省', '', '', '2982', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4277', '230100', '230000', '黑龙江省', '哈尔滨市', '', '2981', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4278', '230102', '230100', '黑龙江省', '哈尔滨市', '道里区', '2980', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4279', '230103', '230100', '黑龙江省', '哈尔滨市', '南岗区', '2979', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4280', '230104', '230100', '黑龙江省', '哈尔滨市', '道外区', '2978', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4281', '230108', '230100', '黑龙江省', '哈尔滨市', '平房区', '2977', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4282', '230109', '230100', '黑龙江省', '哈尔滨市', '松北区', '2976', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4283', '230110', '230100', '黑龙江省', '哈尔滨市', '香坊区', '2975', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4284', '230111', '230100', '黑龙江省', '哈尔滨市', '呼兰区', '2974', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4285', '230112', '230100', '黑龙江省', '哈尔滨市', '阿城区', '2973', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4286', '230113', '230100', '黑龙江省', '哈尔滨市', '双城区', '2972', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4287', '230123', '230100', '黑龙江省', '哈尔滨市', '依兰县', '2971', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4288', '230124', '230100', '黑龙江省', '哈尔滨市', '方正县', '2970', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4289', '230125', '230100', '黑龙江省', '哈尔滨市', '宾县', '2969', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4290', '230126', '230100', '黑龙江省', '哈尔滨市', '巴彦县', '2968', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4291', '230127', '230100', '黑龙江省', '哈尔滨市', '木兰县', '2967', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4292', '230128', '230100', '黑龙江省', '哈尔滨市', '通河县', '2966', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4293', '230129', '230100', '黑龙江省', '哈尔滨市', '延寿县', '2965', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4294', '230183', '230100', '黑龙江省', '哈尔滨市', '尚志市', '2964', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4295', '230184', '230100', '黑龙江省', '哈尔滨市', '五常市', '2963', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4296', '230200', '230000', '黑龙江省', '齐齐哈尔市', '', '2962', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4297', '230202', '230200', '黑龙江省', '齐齐哈尔市', '龙沙区', '2961', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4298', '230203', '230200', '黑龙江省', '齐齐哈尔市', '建华区', '2960', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4299', '230204', '230200', '黑龙江省', '齐齐哈尔市', '铁锋区', '2959', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4300', '230205', '230200', '黑龙江省', '齐齐哈尔市', '昂昂溪区', '2958', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4301', '230206', '230200', '黑龙江省', '齐齐哈尔市', '富拉尔基区', '2957', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4302', '230207', '230200', '黑龙江省', '齐齐哈尔市', '碾子山区', '2956', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4303', '230208', '230200', '黑龙江省', '齐齐哈尔市', '梅里斯达斡尔族区', '2955', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4304', '230221', '230200', '黑龙江省', '齐齐哈尔市', '龙江县', '2954', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4305', '230223', '230200', '黑龙江省', '齐齐哈尔市', '依安县', '2953', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4306', '230224', '230200', '黑龙江省', '齐齐哈尔市', '泰来县', '2952', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4307', '230225', '230200', '黑龙江省', '齐齐哈尔市', '甘南县', '2951', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4308', '230227', '230200', '黑龙江省', '齐齐哈尔市', '富裕县', '2950', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4309', '230229', '230200', '黑龙江省', '齐齐哈尔市', '克山县', '2949', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4310', '230230', '230200', '黑龙江省', '齐齐哈尔市', '克东县', '2948', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4311', '230231', '230200', '黑龙江省', '齐齐哈尔市', '拜泉县', '2947', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4312', '230281', '230200', '黑龙江省', '齐齐哈尔市', '讷河市', '2946', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4313', '230300', '230000', '黑龙江省', '鸡西市', '', '2945', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4314', '230302', '230300', '黑龙江省', '鸡西市', '鸡冠区', '2944', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4315', '230303', '230300', '黑龙江省', '鸡西市', '恒山区', '2943', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4316', '230304', '230300', '黑龙江省', '鸡西市', '滴道区', '2942', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4317', '230305', '230300', '黑龙江省', '鸡西市', '梨树区', '2941', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4318', '230306', '230300', '黑龙江省', '鸡西市', '城子河区', '2940', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4319', '230307', '230300', '黑龙江省', '鸡西市', '麻山区', '2939', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4320', '230321', '230300', '黑龙江省', '鸡西市', '鸡东县', '2938', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4321', '230381', '230300', '黑龙江省', '鸡西市', '虎林市', '2937', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4322', '230382', '230300', '黑龙江省', '鸡西市', '密山市', '2936', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4323', '230400', '230000', '黑龙江省', '鹤岗市', '', '2935', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4324', '230402', '230400', '黑龙江省', '鹤岗市', '向阳区', '2934', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4325', '230403', '230400', '黑龙江省', '鹤岗市', '工农区', '2933', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4326', '230404', '230400', '黑龙江省', '鹤岗市', '南山区', '2932', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4327', '230405', '230400', '黑龙江省', '鹤岗市', '兴安区', '2931', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4328', '230406', '230400', '黑龙江省', '鹤岗市', '东山区', '2930', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4329', '230407', '230400', '黑龙江省', '鹤岗市', '兴山区', '2929', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4330', '230421', '230400', '黑龙江省', '鹤岗市', '萝北县', '2928', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4331', '230422', '230400', '黑龙江省', '鹤岗市', '绥滨县', '2927', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4332', '230500', '230000', '黑龙江省', '双鸭山市', '', '2926', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4333', '230502', '230500', '黑龙江省', '双鸭山市', '尖山区', '2925', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4334', '230503', '230500', '黑龙江省', '双鸭山市', '岭东区', '2924', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4335', '230505', '230500', '黑龙江省', '双鸭山市', '四方台区', '2923', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4336', '230506', '230500', '黑龙江省', '双鸭山市', '宝山区', '2922', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4337', '230521', '230500', '黑龙江省', '双鸭山市', '集贤县', '2921', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4338', '230522', '230500', '黑龙江省', '双鸭山市', '友谊县', '2920', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4339', '230523', '230500', '黑龙江省', '双鸭山市', '宝清县', '2919', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4340', '230524', '230500', '黑龙江省', '双鸭山市', '饶河县', '2918', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4341', '230600', '230000', '黑龙江省', '大庆市', '', '2917', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4342', '230602', '230600', '黑龙江省', '大庆市', '萨尔图区', '2916', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4343', '230603', '230600', '黑龙江省', '大庆市', '龙凤区', '2915', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4344', '230604', '230600', '黑龙江省', '大庆市', '让胡路区', '2914', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4345', '230605', '230600', '黑龙江省', '大庆市', '红岗区', '2913', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4346', '230606', '230600', '黑龙江省', '大庆市', '大同区', '2912', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4347', '230621', '230600', '黑龙江省', '大庆市', '肇州县', '2911', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4348', '230622', '230600', '黑龙江省', '大庆市', '肇源县', '2910', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4349', '230623', '230600', '黑龙江省', '大庆市', '林甸县', '2909', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4350', '230624', '230600', '黑龙江省', '大庆市', '杜尔伯特蒙古族自治县', '2908', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4351', '230700', '230000', '黑龙江省', '伊春市', '', '2907', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4352', '230702', '230700', '黑龙江省', '伊春市', '伊春区', '2906', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4353', '230703', '230700', '黑龙江省', '伊春市', '南岔区', '2905', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4354', '230704', '230700', '黑龙江省', '伊春市', '友好区', '2904', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4355', '230705', '230700', '黑龙江省', '伊春市', '西林区', '2903', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4356', '230706', '230700', '黑龙江省', '伊春市', '翠峦区', '2902', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4357', '230707', '230700', '黑龙江省', '伊春市', '新青区', '2901', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4358', '230708', '230700', '黑龙江省', '伊春市', '美溪区', '2900', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4359', '230709', '230700', '黑龙江省', '伊春市', '金山屯区', '2899', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4360', '230710', '230700', '黑龙江省', '伊春市', '五营区', '2898', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4361', '230711', '230700', '黑龙江省', '伊春市', '乌马河区', '2897', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4362', '230712', '230700', '黑龙江省', '伊春市', '汤旺河区', '2896', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4363', '230713', '230700', '黑龙江省', '伊春市', '带岭区', '2895', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4364', '230714', '230700', '黑龙江省', '伊春市', '乌伊岭区', '2894', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4365', '230715', '230700', '黑龙江省', '伊春市', '红星区', '2893', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4366', '230716', '230700', '黑龙江省', '伊春市', '上甘岭区', '2892', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4367', '230722', '230700', '黑龙江省', '伊春市', '嘉荫县', '2891', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4368', '230781', '230700', '黑龙江省', '伊春市', '铁力市', '2890', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4369', '230800', '230000', '黑龙江省', '佳木斯市', '', '2889', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4370', '230803', '230800', '黑龙江省', '佳木斯市', '向阳区', '2888', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4371', '230804', '230800', '黑龙江省', '佳木斯市', '前进区', '2887', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4372', '230805', '230800', '黑龙江省', '佳木斯市', '东风区', '2886', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4373', '230811', '230800', '黑龙江省', '佳木斯市', '郊区', '2885', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4374', '230822', '230800', '黑龙江省', '佳木斯市', '桦南县', '2884', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4375', '230826', '230800', '黑龙江省', '佳木斯市', '桦川县', '2883', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4376', '230828', '230800', '黑龙江省', '佳木斯市', '汤原县', '2882', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4377', '230881', '230800', '黑龙江省', '佳木斯市', '同江市', '2881', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4378', '230882', '230800', '黑龙江省', '佳木斯市', '富锦市', '2880', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4379', '230883', '230800', '黑龙江省', '佳木斯市', '抚远市', '2879', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4380', '230900', '230000', '黑龙江省', '七台河市', '', '2878', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4381', '230902', '230900', '黑龙江省', '七台河市', '新兴区', '2877', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4382', '230903', '230900', '黑龙江省', '七台河市', '桃山区', '2876', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4383', '230904', '230900', '黑龙江省', '七台河市', '茄子河区', '2875', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4384', '230921', '230900', '黑龙江省', '七台河市', '勃利县', '2874', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4385', '231000', '230000', '黑龙江省', '牡丹江市', '', '2873', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4386', '231002', '231000', '黑龙江省', '牡丹江市', '东安区', '2872', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4387', '231003', '231000', '黑龙江省', '牡丹江市', '阳明区', '2871', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4388', '231004', '231000', '黑龙江省', '牡丹江市', '爱民区', '2870', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4389', '231005', '231000', '黑龙江省', '牡丹江市', '西安区', '2869', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4390', '231025', '231000', '黑龙江省', '牡丹江市', '林口县', '2868', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4391', '231081', '231000', '黑龙江省', '牡丹江市', '绥芬河市', '2867', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4392', '231083', '231000', '黑龙江省', '牡丹江市', '海林市', '2866', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4393', '231084', '231000', '黑龙江省', '牡丹江市', '宁安市', '2865', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4394', '231085', '231000', '黑龙江省', '牡丹江市', '穆棱市', '2864', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4395', '231086', '231000', '黑龙江省', '牡丹江市', '东宁市', '2863', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4396', '231100', '230000', '黑龙江省', '黑河市', '', '2862', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4397', '231102', '231100', '黑龙江省', '黑河市', '爱辉区', '2861', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4398', '231121', '231100', '黑龙江省', '黑河市', '嫩江县', '2860', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4399', '231123', '231100', '黑龙江省', '黑河市', '逊克县', '2859', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4400', '231124', '231100', '黑龙江省', '黑河市', '孙吴县', '2858', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4401', '231181', '231100', '黑龙江省', '黑河市', '北安市', '2857', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4402', '231182', '231100', '黑龙江省', '黑河市', '五大连池市', '2856', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4403', '231200', '230000', '黑龙江省', '绥化市', '', '2855', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4404', '231202', '231200', '黑龙江省', '绥化市', '北林区', '2854', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4405', '231221', '231200', '黑龙江省', '绥化市', '望奎县', '2853', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4406', '231222', '231200', '黑龙江省', '绥化市', '兰西县', '2852', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4407', '231223', '231200', '黑龙江省', '绥化市', '青冈县', '2851', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4408', '231224', '231200', '黑龙江省', '绥化市', '庆安县', '2850', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4409', '231225', '231200', '黑龙江省', '绥化市', '明水县', '2849', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4410', '231226', '231200', '黑龙江省', '绥化市', '绥棱县', '2848', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4411', '231281', '231200', '黑龙江省', '绥化市', '安达市', '2847', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4412', '231282', '231200', '黑龙江省', '绥化市', '肇东市', '2846', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4413', '231283', '231200', '黑龙江省', '绥化市', '海伦市', '2845', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4414', '232700', '230000', '黑龙江省', '大兴安岭地区', '', '2844', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4415', '232701', '232700', '黑龙江省', '大兴安岭地区', '加格达奇区', '2843', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4416', '232721', '232700', '黑龙江省', '大兴安岭地区', '呼玛县', '2842', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4417', '232722', '232700', '黑龙江省', '大兴安岭地区', '塔河县', '2841', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4418', '232723', '232700', '黑龙江省', '大兴安岭地区', '漠河县', '2840', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4419', '310000', '-1', '', '上海市', '', '2839', '1', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4420', '310101', '310000', '', '上海市', '黄浦区', '2838', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4421', '310104', '310000', '', '上海市', '徐汇区', '2837', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4422', '310105', '310000', '', '上海市', '长宁区', '2836', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4423', '310106', '310000', '', '上海市', '静安区', '2835', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4424', '310107', '310000', '', '上海市', '普陀区', '2834', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4425', '310109', '310000', '', '上海市', '虹口区', '2833', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4426', '310110', '310000', '', '上海市', '杨浦区', '2832', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4427', '310112', '310000', '', '上海市', '闵行区', '2831', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4428', '310113', '310000', '', '上海市', '宝山区', '2830', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4429', '310114', '310000', '', '上海市', '嘉定区', '2829', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4430', '310115', '310000', '', '上海市', '浦东新区', '2828', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4431', '310116', '310000', '', '上海市', '金山区', '2827', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4432', '310117', '310000', '', '上海市', '松江区', '2826', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4433', '310118', '310000', '', '上海市', '青浦区', '2825', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4434', '310120', '310000', '', '上海市', '奉贤区', '2824', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4435', '310151', '310000', '', '上海市', '崇明区', '2823', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4436', '320000', '-1', '江苏省', '', '', '2822', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4437', '320100', '320000', '江苏省', '南京市', '', '2821', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4438', '320102', '320100', '江苏省', '南京市', '玄武区', '2820', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4439', '320104', '320100', '江苏省', '南京市', '秦淮区', '2819', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4440', '320105', '320100', '江苏省', '南京市', '建邺区', '2818', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4441', '320106', '320100', '江苏省', '南京市', '鼓楼区', '2817', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4442', '320111', '320100', '江苏省', '南京市', '浦口区', '2816', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4443', '320113', '320100', '江苏省', '南京市', '栖霞区', '2815', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4444', '320114', '320100', '江苏省', '南京市', '雨花台区', '2814', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4445', '320115', '320100', '江苏省', '南京市', '江宁区', '2813', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4446', '320116', '320100', '江苏省', '南京市', '六合区', '2812', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4447', '320117', '320100', '江苏省', '南京市', '溧水区', '2811', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4448', '320118', '320100', '江苏省', '南京市', '高淳区', '2810', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4449', '320200', '320000', '江苏省', '无锡市', '', '2809', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4450', '320205', '320200', '江苏省', '无锡市', '锡山区', '2808', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4451', '320206', '320200', '江苏省', '无锡市', '惠山区', '2807', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4452', '320211', '320200', '江苏省', '无锡市', '滨湖区', '2806', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4453', '320213', '320200', '江苏省', '无锡市', '梁溪区', '2805', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4454', '320214', '320200', '江苏省', '无锡市', '新吴区', '2804', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4455', '320281', '320200', '江苏省', '无锡市', '江阴市', '2803', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4456', '320282', '320200', '江苏省', '无锡市', '宜兴市', '2802', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4457', '320300', '320000', '江苏省', '徐州市', '', '2801', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4458', '320302', '320300', '江苏省', '徐州市', '鼓楼区', '2800', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4459', '320303', '320300', '江苏省', '徐州市', '云龙区', '2799', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4460', '320305', '320300', '江苏省', '徐州市', '贾汪区', '2798', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4461', '320311', '320300', '江苏省', '徐州市', '泉山区', '2797', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4462', '320312', '320300', '江苏省', '徐州市', '铜山区', '2796', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4463', '320321', '320300', '江苏省', '徐州市', '丰县', '2795', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4464', '320322', '320300', '江苏省', '徐州市', '沛县', '2794', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4465', '320324', '320300', '江苏省', '徐州市', '睢宁县', '2793', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4466', '320381', '320300', '江苏省', '徐州市', '新沂市', '2792', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4467', '320382', '320300', '江苏省', '徐州市', '邳州市', '2791', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4468', '320400', '320000', '江苏省', '常州市', '', '2790', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4469', '320402', '320400', '江苏省', '常州市', '天宁区', '2789', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4470', '320404', '320400', '江苏省', '常州市', '钟楼区', '2788', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4471', '320411', '320400', '江苏省', '常州市', '新北区', '2787', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4472', '320412', '320400', '江苏省', '常州市', '武进区', '2786', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4473', '320413', '320400', '江苏省', '常州市', '金坛区', '2785', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4474', '320481', '320400', '江苏省', '常州市', '溧阳市', '2784', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4475', '320500', '320000', '江苏省', '苏州市', '', '2783', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4476', '320505', '320500', '江苏省', '苏州市', '虎丘区', '2782', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4477', '320506', '320500', '江苏省', '苏州市', '吴中区', '2781', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4478', '320507', '320500', '江苏省', '苏州市', '相城区', '2780', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4479', '320508', '320500', '江苏省', '苏州市', '姑苏区', '2779', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4480', '320509', '320500', '江苏省', '苏州市', '吴江区', '2778', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4481', '320581', '320500', '江苏省', '苏州市', '常熟市', '2777', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4482', '320582', '320500', '江苏省', '苏州市', '张家港市', '2776', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4483', '320583', '320500', '江苏省', '苏州市', '昆山市', '2775', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4484', '320585', '320500', '江苏省', '苏州市', '太仓市', '2774', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4485', '320600', '320000', '江苏省', '南通市', '', '2773', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4486', '320602', '320600', '江苏省', '南通市', '崇川区', '2772', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4487', '320611', '320600', '江苏省', '南通市', '港闸区', '2771', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4488', '320612', '320600', '江苏省', '南通市', '通州区', '2770', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4489', '320621', '320600', '江苏省', '南通市', '海安县', '2769', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4490', '320623', '320600', '江苏省', '南通市', '如东县', '2768', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4491', '320681', '320600', '江苏省', '南通市', '启东市', '2767', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4492', '320682', '320600', '江苏省', '南通市', '如皋市', '2766', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4493', '320684', '320600', '江苏省', '南通市', '海门市', '2765', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4494', '320700', '320000', '江苏省', '连云港市', '', '2764', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4495', '320703', '320700', '江苏省', '连云港市', '连云区', '2763', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4496', '320706', '320700', '江苏省', '连云港市', '海州区', '2762', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4497', '320707', '320700', '江苏省', '连云港市', '赣榆区', '2761', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4498', '320722', '320700', '江苏省', '连云港市', '东海县', '2760', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4499', '320723', '320700', '江苏省', '连云港市', '灌云县', '2759', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4500', '320724', '320700', '江苏省', '连云港市', '灌南县', '2758', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4501', '320800', '320000', '江苏省', '淮安市', '', '2757', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4502', '320803', '320800', '江苏省', '淮安市', '淮安区', '2756', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4503', '320804', '320800', '江苏省', '淮安市', '淮阴区', '2755', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4504', '320812', '320800', '江苏省', '淮安市', '清江浦区', '2754', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4505', '320813', '320800', '江苏省', '淮安市', '洪泽区', '2753', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4506', '320826', '320800', '江苏省', '淮安市', '涟水县', '2752', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4507', '320830', '320800', '江苏省', '淮安市', '盱眙县', '2751', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4508', '320831', '320800', '江苏省', '淮安市', '金湖县', '2750', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4509', '320900', '320000', '江苏省', '盐城市', '', '2749', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4510', '320902', '320900', '江苏省', '盐城市', '亭湖区', '2748', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4511', '320903', '320900', '江苏省', '盐城市', '盐都区', '2747', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4512', '320904', '320900', '江苏省', '盐城市', '大丰区', '2746', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4513', '320921', '320900', '江苏省', '盐城市', '响水县', '2745', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4514', '320922', '320900', '江苏省', '盐城市', '滨海县', '2744', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4515', '320923', '320900', '江苏省', '盐城市', '阜宁县', '2743', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4516', '320924', '320900', '江苏省', '盐城市', '射阳县', '2742', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4517', '320925', '320900', '江苏省', '盐城市', '建湖县', '2741', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4518', '320981', '320900', '江苏省', '盐城市', '东台市', '2740', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4519', '321000', '320000', '江苏省', '扬州市', '', '2739', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4520', '321002', '321000', '江苏省', '扬州市', '广陵区', '2738', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4521', '321003', '321000', '江苏省', '扬州市', '邗江区', '2737', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4522', '321012', '321000', '江苏省', '扬州市', '江都区', '2736', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4523', '321023', '321000', '江苏省', '扬州市', '宝应县', '2735', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4524', '321081', '321000', '江苏省', '扬州市', '仪征市', '2734', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4525', '321084', '321000', '江苏省', '扬州市', '高邮市', '2733', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4526', '321100', '320000', '江苏省', '镇江市', '', '2732', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4527', '321102', '321100', '江苏省', '镇江市', '京口区', '2731', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4528', '321111', '321100', '江苏省', '镇江市', '润州区', '2730', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4529', '321112', '321100', '江苏省', '镇江市', '丹徒区', '2729', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4530', '321181', '321100', '江苏省', '镇江市', '丹阳市', '2728', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4531', '321182', '321100', '江苏省', '镇江市', '扬中市', '2727', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4532', '321183', '321100', '江苏省', '镇江市', '句容市', '2726', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4533', '321200', '320000', '江苏省', '泰州市', '', '2725', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4534', '321202', '321200', '江苏省', '泰州市', '海陵区', '2724', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4535', '321203', '321200', '江苏省', '泰州市', '高港区', '2723', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4536', '321204', '321200', '江苏省', '泰州市', '姜堰区', '2722', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4537', '321281', '321200', '江苏省', '泰州市', '兴化市', '2721', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4538', '321282', '321200', '江苏省', '泰州市', '靖江市', '2720', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4539', '321283', '321200', '江苏省', '泰州市', '泰兴市', '2719', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4540', '321300', '320000', '江苏省', '宿迁市', '', '2718', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4541', '321302', '321300', '江苏省', '宿迁市', '宿城区', '2717', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4542', '321311', '321300', '江苏省', '宿迁市', '宿豫区', '2716', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4543', '321322', '321300', '江苏省', '宿迁市', '沭阳县', '2715', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4544', '321323', '321300', '江苏省', '宿迁市', '泗阳县', '2714', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4545', '321324', '321300', '江苏省', '宿迁市', '泗洪县', '2713', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4546', '330000', '-1', '浙江省', '', '', '2712', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4547', '330100', '330000', '浙江省', '杭州市', '', '2711', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4548', '330102', '330100', '浙江省', '杭州市', '上城区', '2710', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4549', '330103', '330100', '浙江省', '杭州市', '下城区', '2709', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4550', '330104', '330100', '浙江省', '杭州市', '江干区', '2708', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4551', '330105', '330100', '浙江省', '杭州市', '拱墅区', '2707', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4552', '330106', '330100', '浙江省', '杭州市', '西湖区', '2706', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4553', '330108', '330100', '浙江省', '杭州市', '滨江区', '2705', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4554', '330109', '330100', '浙江省', '杭州市', '萧山区', '2704', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4555', '330110', '330100', '浙江省', '杭州市', '余杭区', '2703', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4556', '330111', '330100', '浙江省', '杭州市', '富阳区', '2702', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4557', '330122', '330100', '浙江省', '杭州市', '桐庐县', '2701', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4558', '330127', '330100', '浙江省', '杭州市', '淳安县', '2700', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4559', '330182', '330100', '浙江省', '杭州市', '建德市', '2699', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4560', '330185', '330100', '浙江省', '杭州市', '临安市', '2698', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4561', '330200', '330000', '浙江省', '宁波市', '', '2697', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4562', '330203', '330200', '浙江省', '宁波市', '海曙区', '2696', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4563', '330205', '330200', '浙江省', '宁波市', '江北区', '2695', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4564', '330206', '330200', '浙江省', '宁波市', '北仑区', '2694', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4565', '330211', '330200', '浙江省', '宁波市', '镇海区', '2693', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4566', '330212', '330200', '浙江省', '宁波市', '鄞州区', '2692', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4567', '330213', '330200', '浙江省', '宁波市', '奉化区', '2691', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4568', '330225', '330200', '浙江省', '宁波市', '象山县', '2690', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4569', '330226', '330200', '浙江省', '宁波市', '宁海县', '2689', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4570', '330281', '330200', '浙江省', '宁波市', '余姚市', '2688', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4571', '330282', '330200', '浙江省', '宁波市', '慈溪市', '2687', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4572', '330300', '330000', '浙江省', '温州市', '', '2686', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4573', '330302', '330300', '浙江省', '温州市', '鹿城区', '2685', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4574', '330303', '330300', '浙江省', '温州市', '龙湾区', '2684', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4575', '330304', '330300', '浙江省', '温州市', '瓯海区', '2683', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4576', '330305', '330300', '浙江省', '温州市', '洞头区', '2682', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4577', '330324', '330300', '浙江省', '温州市', '永嘉县', '2681', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4578', '330326', '330300', '浙江省', '温州市', '平阳县', '2680', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4579', '330327', '330300', '浙江省', '温州市', '苍南县', '2679', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4580', '330328', '330300', '浙江省', '温州市', '文成县', '2678', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4581', '330329', '330300', '浙江省', '温州市', '泰顺县', '2677', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4582', '330381', '330300', '浙江省', '温州市', '瑞安市', '2676', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4583', '330382', '330300', '浙江省', '温州市', '乐清市', '2675', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4584', '330400', '330000', '浙江省', '嘉兴市', '', '2674', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4585', '330402', '330400', '浙江省', '嘉兴市', '南湖区', '2673', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4586', '330411', '330400', '浙江省', '嘉兴市', '秀洲区', '2672', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4587', '330421', '330400', '浙江省', '嘉兴市', '嘉善县', '2671', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4588', '330424', '330400', '浙江省', '嘉兴市', '海盐县', '2670', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4589', '330481', '330400', '浙江省', '嘉兴市', '海宁市', '2669', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4590', '330482', '330400', '浙江省', '嘉兴市', '平湖市', '2668', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4591', '330483', '330400', '浙江省', '嘉兴市', '桐乡市', '2667', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4592', '330500', '330000', '浙江省', '湖州市', '', '2666', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4593', '330502', '330500', '浙江省', '湖州市', '吴兴区', '2665', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4594', '330503', '330500', '浙江省', '湖州市', '南浔区', '2664', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4595', '330521', '330500', '浙江省', '湖州市', '德清县', '2663', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4596', '330522', '330500', '浙江省', '湖州市', '长兴县', '2662', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4597', '330523', '330500', '浙江省', '湖州市', '安吉县', '2661', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4598', '330600', '330000', '浙江省', '绍兴市', '', '2660', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4599', '330602', '330600', '浙江省', '绍兴市', '越城区', '2659', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4600', '330603', '330600', '浙江省', '绍兴市', '柯桥区', '2658', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4601', '330604', '330600', '浙江省', '绍兴市', '上虞区', '2657', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4602', '330624', '330600', '浙江省', '绍兴市', '新昌县', '2656', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4603', '330681', '330600', '浙江省', '绍兴市', '诸暨市', '2655', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4604', '330683', '330600', '浙江省', '绍兴市', '嵊州市', '2654', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4605', '330700', '330000', '浙江省', '金华市', '', '2653', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4606', '330702', '330700', '浙江省', '金华市', '婺城区', '2652', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4607', '330703', '330700', '浙江省', '金华市', '金东区', '2651', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4608', '330723', '330700', '浙江省', '金华市', '武义县', '2650', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4609', '330726', '330700', '浙江省', '金华市', '浦江县', '2649', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4610', '330727', '330700', '浙江省', '金华市', '磐安县', '2648', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4611', '330781', '330700', '浙江省', '金华市', '兰溪市', '2647', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4612', '330782', '330700', '浙江省', '金华市', '义乌市', '2646', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4613', '330783', '330700', '浙江省', '金华市', '东阳市', '2645', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4614', '330784', '330700', '浙江省', '金华市', '永康市', '2644', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4615', '330800', '330000', '浙江省', '衢州市', '', '2643', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4616', '330802', '330800', '浙江省', '衢州市', '柯城区', '2642', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4617', '330803', '330800', '浙江省', '衢州市', '衢江区', '2641', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4618', '330822', '330800', '浙江省', '衢州市', '常山县', '2640', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4619', '330824', '330800', '浙江省', '衢州市', '开化县', '2639', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4620', '330825', '330800', '浙江省', '衢州市', '龙游县', '2638', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4621', '330881', '330800', '浙江省', '衢州市', '江山市', '2637', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4622', '330900', '330000', '浙江省', '舟山市', '', '2636', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4623', '330902', '330900', '浙江省', '舟山市', '定海区', '2635', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4624', '330903', '330900', '浙江省', '舟山市', '普陀区', '2634', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4625', '330921', '330900', '浙江省', '舟山市', '岱山县', '2633', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4626', '330922', '330900', '浙江省', '舟山市', '嵊泗县', '2632', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4627', '331000', '330000', '浙江省', '台州市', '', '2631', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4628', '331002', '331000', '浙江省', '台州市', '椒江区', '2630', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4629', '331003', '331000', '浙江省', '台州市', '黄岩区', '2629', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4630', '331004', '331000', '浙江省', '台州市', '路桥区', '2628', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4631', '331022', '331000', '浙江省', '台州市', '三门县', '2627', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4632', '331023', '331000', '浙江省', '台州市', '天台县', '2626', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4633', '331024', '331000', '浙江省', '台州市', '仙居县', '2625', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4634', '331081', '331000', '浙江省', '台州市', '温岭市', '2624', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4635', '331082', '331000', '浙江省', '台州市', '临海市', '2623', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4636', '331083', '331000', '浙江省', '台州市', '玉环市', '2622', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4637', '331100', '330000', '浙江省', '丽水市', '', '2621', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4638', '331102', '331100', '浙江省', '丽水市', '莲都区', '2620', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4639', '331121', '331100', '浙江省', '丽水市', '青田县', '2619', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4640', '331122', '331100', '浙江省', '丽水市', '缙云县', '2618', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4641', '331123', '331100', '浙江省', '丽水市', '遂昌县', '2617', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4642', '331124', '331100', '浙江省', '丽水市', '松阳县', '2616', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4643', '331125', '331100', '浙江省', '丽水市', '云和县', '2615', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4644', '331126', '331100', '浙江省', '丽水市', '庆元县', '2614', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4645', '331127', '331100', '浙江省', '丽水市', '景宁畲族自治县', '2613', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4646', '331181', '331100', '浙江省', '丽水市', '龙泉市', '2612', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4647', '340000', '-1', '安徽省', '', '', '2611', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4648', '340100', '340000', '安徽省', '合肥市', '', '2610', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4649', '340102', '340100', '安徽省', '合肥市', '瑶海区', '2609', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4650', '340103', '340100', '安徽省', '合肥市', '庐阳区', '2608', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4651', '340104', '340100', '安徽省', '合肥市', '蜀山区', '2607', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4652', '340111', '340100', '安徽省', '合肥市', '包河区', '2606', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4653', '340121', '340100', '安徽省', '合肥市', '长丰县', '2605', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4654', '340122', '340100', '安徽省', '合肥市', '肥东县', '2604', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4655', '340123', '340100', '安徽省', '合肥市', '肥西县', '2603', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4656', '340124', '340100', '安徽省', '合肥市', '庐江县', '2602', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4657', '340181', '340100', '安徽省', '合肥市', '巢湖市', '2601', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4658', '340200', '340000', '安徽省', '芜湖市', '', '2600', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4659', '340202', '340200', '安徽省', '芜湖市', '镜湖区', '2599', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4660', '340203', '340200', '安徽省', '芜湖市', '弋江区', '2598', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4661', '340207', '340200', '安徽省', '芜湖市', '鸠江区', '2597', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4662', '340208', '340200', '安徽省', '芜湖市', '三山区', '2596', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4663', '340221', '340200', '安徽省', '芜湖市', '芜湖县', '2595', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4664', '340222', '340200', '安徽省', '芜湖市', '繁昌县', '2594', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4665', '340223', '340200', '安徽省', '芜湖市', '南陵县', '2593', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4666', '340225', '340200', '安徽省', '芜湖市', '无为县', '2592', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4667', '340300', '340000', '安徽省', '蚌埠市', '', '2591', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4668', '340302', '340300', '安徽省', '蚌埠市', '龙子湖区', '2590', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4669', '340303', '340300', '安徽省', '蚌埠市', '蚌山区', '2589', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4670', '340304', '340300', '安徽省', '蚌埠市', '禹会区', '2588', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4671', '340311', '340300', '安徽省', '蚌埠市', '淮上区', '2587', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4672', '340321', '340300', '安徽省', '蚌埠市', '怀远县', '2586', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4673', '340322', '340300', '安徽省', '蚌埠市', '五河县', '2585', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4674', '340323', '340300', '安徽省', '蚌埠市', '固镇县', '2584', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4675', '340400', '340000', '安徽省', '淮南市', '', '2583', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4676', '340402', '340400', '安徽省', '淮南市', '大通区', '2582', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4677', '340403', '340400', '安徽省', '淮南市', '田家庵区', '2581', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4678', '340404', '340400', '安徽省', '淮南市', '谢家集区', '2580', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4679', '340405', '340400', '安徽省', '淮南市', '八公山区', '2579', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4680', '340406', '340400', '安徽省', '淮南市', '潘集区', '2578', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4681', '340421', '340400', '安徽省', '淮南市', '凤台县', '2577', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4682', '340422', '340400', '安徽省', '淮南市', '寿县', '2576', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4683', '340500', '340000', '安徽省', '马鞍山市', '', '2575', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4684', '340503', '340500', '安徽省', '马鞍山市', '花山区', '2574', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4685', '340504', '340500', '安徽省', '马鞍山市', '雨山区', '2573', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4686', '340506', '340500', '安徽省', '马鞍山市', '博望区', '2572', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4687', '340521', '340500', '安徽省', '马鞍山市', '当涂县', '2571', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4688', '340522', '340500', '安徽省', '马鞍山市', '含山县', '2570', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4689', '340523', '340500', '安徽省', '马鞍山市', '和县', '2569', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4690', '340600', '340000', '安徽省', '淮北市', '', '2568', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4691', '340602', '340600', '安徽省', '淮北市', '杜集区', '2567', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4692', '340603', '340600', '安徽省', '淮北市', '相山区', '2566', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4693', '340604', '340600', '安徽省', '淮北市', '烈山区', '2565', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4694', '340621', '340600', '安徽省', '淮北市', '濉溪县', '2564', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4695', '340700', '340000', '安徽省', '铜陵市', '', '2563', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4696', '340705', '340700', '安徽省', '铜陵市', '铜官区', '2562', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4697', '340706', '340700', '安徽省', '铜陵市', '义安区', '2561', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4698', '340711', '340700', '安徽省', '铜陵市', '郊区', '2560', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4699', '340722', '340700', '安徽省', '铜陵市', '枞阳县', '2559', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4700', '340800', '340000', '安徽省', '安庆市', '', '2558', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4701', '340802', '340800', '安徽省', '安庆市', '迎江区', '2557', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4702', '340803', '340800', '安徽省', '安庆市', '大观区', '2556', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4703', '340811', '340800', '安徽省', '安庆市', '宜秀区', '2555', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4704', '340822', '340800', '安徽省', '安庆市', '怀宁县', '2554', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4705', '340824', '340800', '安徽省', '安庆市', '潜山县', '2553', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4706', '340825', '340800', '安徽省', '安庆市', '太湖县', '2552', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4707', '340826', '340800', '安徽省', '安庆市', '宿松县', '2551', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4708', '340827', '340800', '安徽省', '安庆市', '望江县', '2550', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4709', '340828', '340800', '安徽省', '安庆市', '岳西县', '2549', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4710', '340881', '340800', '安徽省', '安庆市', '桐城市', '2548', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4711', '341000', '340000', '安徽省', '黄山市', '', '2547', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4712', '341002', '341000', '安徽省', '黄山市', '屯溪区', '2546', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4713', '341003', '341000', '安徽省', '黄山市', '黄山区', '2545', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4714', '341004', '341000', '安徽省', '黄山市', '徽州区', '2544', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4715', '341021', '341000', '安徽省', '黄山市', '歙县', '2543', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4716', '341022', '341000', '安徽省', '黄山市', '休宁县', '2542', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4717', '341023', '341000', '安徽省', '黄山市', '黟县', '2541', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4718', '341024', '341000', '安徽省', '黄山市', '祁门县', '2540', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4719', '341100', '340000', '安徽省', '滁州市', '', '2539', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4720', '341102', '341100', '安徽省', '滁州市', '琅琊区', '2538', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4721', '341103', '341100', '安徽省', '滁州市', '南谯区', '2537', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4722', '341122', '341100', '安徽省', '滁州市', '来安县', '2536', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4723', '341124', '341100', '安徽省', '滁州市', '全椒县', '2535', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4724', '341125', '341100', '安徽省', '滁州市', '定远县', '2534', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4725', '341126', '341100', '安徽省', '滁州市', '凤阳县', '2533', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4726', '341181', '341100', '安徽省', '滁州市', '天长市', '2532', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4727', '341182', '341100', '安徽省', '滁州市', '明光市', '2531', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4728', '341200', '340000', '安徽省', '阜阳市', '', '2530', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4729', '341202', '341200', '安徽省', '阜阳市', '颍州区', '2529', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4730', '341203', '341200', '安徽省', '阜阳市', '颍东区', '2528', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4731', '341204', '341200', '安徽省', '阜阳市', '颍泉区', '2527', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4732', '341221', '341200', '安徽省', '阜阳市', '临泉县', '2526', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4733', '341222', '341200', '安徽省', '阜阳市', '太和县', '2525', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4734', '341225', '341200', '安徽省', '阜阳市', '阜南县', '2524', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4735', '341226', '341200', '安徽省', '阜阳市', '颍上县', '2523', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4736', '341282', '341200', '安徽省', '阜阳市', '界首市', '2522', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4737', '341300', '340000', '安徽省', '宿州市', '', '2521', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4738', '341302', '341300', '安徽省', '宿州市', '埇桥区', '2520', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4739', '341321', '341300', '安徽省', '宿州市', '砀山县', '2519', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4740', '341322', '341300', '安徽省', '宿州市', '萧县', '2518', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4741', '341323', '341300', '安徽省', '宿州市', '灵璧县', '2517', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4742', '341324', '341300', '安徽省', '宿州市', '泗县', '2516', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4743', '341500', '340000', '安徽省', '六安市', '', '2515', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4744', '341502', '341500', '安徽省', '六安市', '金安区', '2514', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4745', '341503', '341500', '安徽省', '六安市', '裕安区', '2513', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4746', '341504', '341500', '安徽省', '六安市', '叶集区', '2512', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4747', '341522', '341500', '安徽省', '六安市', '霍邱县', '2511', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4748', '341523', '341500', '安徽省', '六安市', '舒城县', '2510', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4749', '341524', '341500', '安徽省', '六安市', '金寨县', '2509', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4750', '341525', '341500', '安徽省', '六安市', '霍山县', '2508', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4751', '341600', '340000', '安徽省', '亳州市', '', '2507', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4752', '341602', '341600', '安徽省', '亳州市', '谯城区', '2506', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4753', '341621', '341600', '安徽省', '亳州市', '涡阳县', '2505', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4754', '341622', '341600', '安徽省', '亳州市', '蒙城县', '2504', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4755', '341623', '341600', '安徽省', '亳州市', '利辛县', '2503', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4756', '341700', '340000', '安徽省', '池州市', '', '2502', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4757', '341702', '341700', '安徽省', '池州市', '贵池区', '2501', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4758', '341721', '341700', '安徽省', '池州市', '东至县', '2500', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4759', '341722', '341700', '安徽省', '池州市', '石台县', '2499', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4760', '341723', '341700', '安徽省', '池州市', '青阳县', '2498', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4761', '341800', '340000', '安徽省', '宣城市', '', '2497', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4762', '341802', '341800', '安徽省', '宣城市', '宣州区', '2496', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4763', '341821', '341800', '安徽省', '宣城市', '郎溪县', '2495', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4764', '341822', '341800', '安徽省', '宣城市', '广德县', '2494', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4765', '341823', '341800', '安徽省', '宣城市', '泾县', '2493', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4766', '341824', '341800', '安徽省', '宣城市', '绩溪县', '2492', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4767', '341825', '341800', '安徽省', '宣城市', '旌德县', '2491', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4768', '341881', '341800', '安徽省', '宣城市', '宁国市', '2490', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4769', '350000', '-1', '福建省', '', '', '2489', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4770', '350100', '350000', '福建省', '福州市', '', '2488', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4771', '350102', '350100', '福建省', '福州市', '鼓楼区', '2487', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4772', '350103', '350100', '福建省', '福州市', '台江区', '2486', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4773', '350104', '350100', '福建省', '福州市', '仓山区', '2485', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4774', '350105', '350100', '福建省', '福州市', '马尾区', '2484', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4775', '350111', '350100', '福建省', '福州市', '晋安区', '2483', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4776', '350121', '350100', '福建省', '福州市', '闽侯县', '2482', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4777', '350122', '350100', '福建省', '福州市', '连江县', '2481', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4778', '350123', '350100', '福建省', '福州市', '罗源县', '2480', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4779', '350124', '350100', '福建省', '福州市', '闽清县', '2479', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4780', '350125', '350100', '福建省', '福州市', '永泰县', '2478', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4781', '350128', '350100', '福建省', '福州市', '平潭县', '2477', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4782', '350181', '350100', '福建省', '福州市', '福清市', '2476', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4783', '350182', '350100', '福建省', '福州市', '长乐市', '2475', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4784', '350200', '350000', '福建省', '厦门市', '', '2474', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4785', '350203', '350200', '福建省', '厦门市', '思明区', '2473', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4786', '350205', '350200', '福建省', '厦门市', '海沧区', '2472', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4787', '350206', '350200', '福建省', '厦门市', '湖里区', '2471', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4788', '350211', '350200', '福建省', '厦门市', '集美区', '2470', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4789', '350212', '350200', '福建省', '厦门市', '同安区', '2469', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4790', '350213', '350200', '福建省', '厦门市', '翔安区', '2468', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4791', '350300', '350000', '福建省', '莆田市', '', '2467', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4792', '350302', '350300', '福建省', '莆田市', '城厢区', '2466', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4793', '350303', '350300', '福建省', '莆田市', '涵江区', '2465', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4794', '350304', '350300', '福建省', '莆田市', '荔城区', '2464', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4795', '350305', '350300', '福建省', '莆田市', '秀屿区', '2463', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4796', '350322', '350300', '福建省', '莆田市', '仙游县', '2462', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4797', '350400', '350000', '福建省', '三明市', '', '2461', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4798', '350402', '350400', '福建省', '三明市', '梅列区', '2460', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4799', '350403', '350400', '福建省', '三明市', '三元区', '2459', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4800', '350421', '350400', '福建省', '三明市', '明溪县', '2458', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4801', '350423', '350400', '福建省', '三明市', '清流县', '2457', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4802', '350424', '350400', '福建省', '三明市', '宁化县', '2456', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4803', '350425', '350400', '福建省', '三明市', '大田县', '2455', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4804', '350426', '350400', '福建省', '三明市', '尤溪县', '2454', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4805', '350427', '350400', '福建省', '三明市', '沙县', '2453', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4806', '350428', '350400', '福建省', '三明市', '将乐县', '2452', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4807', '350429', '350400', '福建省', '三明市', '泰宁县', '2451', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4808', '350430', '350400', '福建省', '三明市', '建宁县', '2450', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4809', '350481', '350400', '福建省', '三明市', '永安市', '2449', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4810', '350500', '350000', '福建省', '泉州市', '', '2448', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4811', '350502', '350500', '福建省', '泉州市', '鲤城区', '2447', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4812', '350503', '350500', '福建省', '泉州市', '丰泽区', '2446', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4813', '350504', '350500', '福建省', '泉州市', '洛江区', '2445', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4814', '350505', '350500', '福建省', '泉州市', '泉港区', '2444', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4815', '350521', '350500', '福建省', '泉州市', '惠安县', '2443', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4816', '350524', '350500', '福建省', '泉州市', '安溪县', '2442', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4817', '350525', '350500', '福建省', '泉州市', '永春县', '2441', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4818', '350526', '350500', '福建省', '泉州市', '德化县', '2440', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4819', '350527', '350500', '福建省', '泉州市', '金门县', '2439', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4820', '350581', '350500', '福建省', '泉州市', '石狮市', '2438', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4821', '350582', '350500', '福建省', '泉州市', '晋江市', '2437', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4822', '350583', '350500', '福建省', '泉州市', '南安市', '2436', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4823', '350600', '350000', '福建省', '漳州市', '', '2435', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4824', '350602', '350600', '福建省', '漳州市', '芗城区', '2434', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4825', '350603', '350600', '福建省', '漳州市', '龙文区', '2433', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4826', '350622', '350600', '福建省', '漳州市', '云霄县', '2432', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4827', '350623', '350600', '福建省', '漳州市', '漳浦县', '2431', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4828', '350624', '350600', '福建省', '漳州市', '诏安县', '2430', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4829', '350625', '350600', '福建省', '漳州市', '长泰县', '2429', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4830', '350626', '350600', '福建省', '漳州市', '东山县', '2428', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4831', '350627', '350600', '福建省', '漳州市', '南靖县', '2427', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4832', '350628', '350600', '福建省', '漳州市', '平和县', '2426', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4833', '350629', '350600', '福建省', '漳州市', '华安县', '2425', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4834', '350681', '350600', '福建省', '漳州市', '龙海市', '2424', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4835', '350700', '350000', '福建省', '南平市', '', '2423', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4836', '350702', '350700', '福建省', '南平市', '延平区', '2422', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4837', '350703', '350700', '福建省', '南平市', '建阳区', '2421', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4838', '350721', '350700', '福建省', '南平市', '顺昌县', '2420', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4839', '350722', '350700', '福建省', '南平市', '浦城县', '2419', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4840', '350723', '350700', '福建省', '南平市', '光泽县', '2418', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4841', '350724', '350700', '福建省', '南平市', '松溪县', '2417', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4842', '350725', '350700', '福建省', '南平市', '政和县', '2416', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4843', '350781', '350700', '福建省', '南平市', '邵武市', '2415', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4844', '350782', '350700', '福建省', '南平市', '武夷山市', '2414', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4845', '350783', '350700', '福建省', '南平市', '建瓯市', '2413', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4846', '350800', '350000', '福建省', '龙岩市', '', '2412', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4847', '350802', '350800', '福建省', '龙岩市', '新罗区', '2411', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4848', '350803', '350800', '福建省', '龙岩市', '永定区', '2410', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4849', '350821', '350800', '福建省', '龙岩市', '长汀县', '2409', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4850', '350823', '350800', '福建省', '龙岩市', '上杭县', '2408', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4851', '350824', '350800', '福建省', '龙岩市', '武平县', '2407', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4852', '350825', '350800', '福建省', '龙岩市', '连城县', '2406', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4853', '350881', '350800', '福建省', '龙岩市', '漳平市', '2405', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4854', '350900', '350000', '福建省', '宁德市', '', '2404', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4855', '350902', '350900', '福建省', '宁德市', '蕉城区', '2403', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4856', '350921', '350900', '福建省', '宁德市', '霞浦县', '2402', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4857', '350922', '350900', '福建省', '宁德市', '古田县', '2401', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4858', '350923', '350900', '福建省', '宁德市', '屏南县', '2400', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4859', '350924', '350900', '福建省', '宁德市', '寿宁县', '2399', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4860', '350925', '350900', '福建省', '宁德市', '周宁县', '2398', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4861', '350926', '350900', '福建省', '宁德市', '柘荣县', '2397', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4862', '350981', '350900', '福建省', '宁德市', '福安市', '2396', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4863', '350982', '350900', '福建省', '宁德市', '福鼎市', '2395', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4864', '360000', '-1', '江西省', '', '', '2394', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4865', '360100', '360000', '江西省', '南昌市', '', '2393', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4866', '360102', '360100', '江西省', '南昌市', '东湖区', '2392', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4867', '360103', '360100', '江西省', '南昌市', '西湖区', '2391', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4868', '360104', '360100', '江西省', '南昌市', '青云谱区', '2390', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4869', '360105', '360100', '江西省', '南昌市', '湾里区', '2389', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4870', '360111', '360100', '江西省', '南昌市', '青山湖区', '2388', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4871', '360112', '360100', '江西省', '南昌市', '新建区', '2387', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4872', '360121', '360100', '江西省', '南昌市', '南昌县', '2386', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4873', '360123', '360100', '江西省', '南昌市', '安义县', '2385', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4874', '360124', '360100', '江西省', '南昌市', '进贤县', '2384', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4875', '360200', '360000', '江西省', '景德镇市', '', '2383', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4876', '360202', '360200', '江西省', '景德镇市', '昌江区', '2382', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4877', '360203', '360200', '江西省', '景德镇市', '珠山区', '2381', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4878', '360222', '360200', '江西省', '景德镇市', '浮梁县', '2380', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4879', '360281', '360200', '江西省', '景德镇市', '乐平市', '2379', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4880', '360300', '360000', '江西省', '萍乡市', '', '2378', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4881', '360302', '360300', '江西省', '萍乡市', '安源区', '2377', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4882', '360313', '360300', '江西省', '萍乡市', '湘东区', '2376', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4883', '360321', '360300', '江西省', '萍乡市', '莲花县', '2375', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4884', '360322', '360300', '江西省', '萍乡市', '上栗县', '2374', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4885', '360323', '360300', '江西省', '萍乡市', '芦溪县', '2373', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4886', '360400', '360000', '江西省', '九江市', '', '2372', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4887', '360402', '360400', '江西省', '九江市', '濂溪区', '2371', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4888', '360403', '360400', '江西省', '九江市', '浔阳区', '2370', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4889', '360421', '360400', '江西省', '九江市', '九江县', '2369', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4890', '360423', '360400', '江西省', '九江市', '武宁县', '2368', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4891', '360424', '360400', '江西省', '九江市', '修水县', '2367', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4892', '360425', '360400', '江西省', '九江市', '永修县', '2366', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4893', '360426', '360400', '江西省', '九江市', '德安县', '2365', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4894', '360428', '360400', '江西省', '九江市', '都昌县', '2364', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4895', '360429', '360400', '江西省', '九江市', '湖口县', '2363', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4896', '360430', '360400', '江西省', '九江市', '彭泽县', '2362', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4897', '360481', '360400', '江西省', '九江市', '瑞昌市', '2361', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4898', '360482', '360400', '江西省', '九江市', '共青城市', '2360', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4899', '360483', '360400', '江西省', '九江市', '庐山市', '2359', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4900', '360500', '360000', '江西省', '新余市', '', '2358', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4901', '360502', '360500', '江西省', '新余市', '渝水区', '2357', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4902', '360521', '360500', '江西省', '新余市', '分宜县', '2356', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4903', '360600', '360000', '江西省', '鹰潭市', '', '2355', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4904', '360602', '360600', '江西省', '鹰潭市', '月湖区', '2354', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4905', '360622', '360600', '江西省', '鹰潭市', '余江县', '2353', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4906', '360681', '360600', '江西省', '鹰潭市', '贵溪市', '2352', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4907', '360700', '360000', '江西省', '赣州市', '', '2351', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4908', '360702', '360700', '江西省', '赣州市', '章贡区', '2350', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4909', '360703', '360700', '江西省', '赣州市', '南康区', '2349', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4910', '360704', '360700', '江西省', '赣州市', '赣县区', '2348', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4911', '360722', '360700', '江西省', '赣州市', '信丰县', '2347', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4912', '360723', '360700', '江西省', '赣州市', '大余县', '2346', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4913', '360724', '360700', '江西省', '赣州市', '上犹县', '2345', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4914', '360725', '360700', '江西省', '赣州市', '崇义县', '2344', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4915', '360726', '360700', '江西省', '赣州市', '安远县', '2343', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4916', '360727', '360700', '江西省', '赣州市', '龙南县', '2342', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4917', '360728', '360700', '江西省', '赣州市', '定南县', '2341', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4918', '360729', '360700', '江西省', '赣州市', '全南县', '2340', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4919', '360730', '360700', '江西省', '赣州市', '宁都县', '2339', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4920', '360731', '360700', '江西省', '赣州市', '于都县', '2338', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4921', '360732', '360700', '江西省', '赣州市', '兴国县', '2337', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4922', '360733', '360700', '江西省', '赣州市', '会昌县', '2336', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4923', '360734', '360700', '江西省', '赣州市', '寻乌县', '2335', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4924', '360735', '360700', '江西省', '赣州市', '石城县', '2334', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4925', '360781', '360700', '江西省', '赣州市', '瑞金市', '2333', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4926', '360800', '360000', '江西省', '吉安市', '', '2332', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4927', '360802', '360800', '江西省', '吉安市', '吉州区', '2331', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4928', '360803', '360800', '江西省', '吉安市', '青原区', '2330', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4929', '360821', '360800', '江西省', '吉安市', '吉安县', '2329', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4930', '360822', '360800', '江西省', '吉安市', '吉水县', '2328', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4931', '360823', '360800', '江西省', '吉安市', '峡江县', '2327', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4932', '360824', '360800', '江西省', '吉安市', '新干县', '2326', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4933', '360825', '360800', '江西省', '吉安市', '永丰县', '2325', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4934', '360826', '360800', '江西省', '吉安市', '泰和县', '2324', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4935', '360827', '360800', '江西省', '吉安市', '遂川县', '2323', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4936', '360828', '360800', '江西省', '吉安市', '万安县', '2322', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4937', '360829', '360800', '江西省', '吉安市', '安福县', '2321', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4938', '360830', '360800', '江西省', '吉安市', '永新县', '2320', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4939', '360881', '360800', '江西省', '吉安市', '井冈山市', '2319', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4940', '360900', '360000', '江西省', '宜春市', '', '2318', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4941', '360902', '360900', '江西省', '宜春市', '袁州区', '2317', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4942', '360921', '360900', '江西省', '宜春市', '奉新县', '2316', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4943', '360922', '360900', '江西省', '宜春市', '万载县', '2315', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4944', '360923', '360900', '江西省', '宜春市', '上高县', '2314', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4945', '360924', '360900', '江西省', '宜春市', '宜丰县', '2313', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4946', '360925', '360900', '江西省', '宜春市', '靖安县', '2312', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4947', '360926', '360900', '江西省', '宜春市', '铜鼓县', '2311', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4948', '360981', '360900', '江西省', '宜春市', '丰城市', '2310', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4949', '360982', '360900', '江西省', '宜春市', '樟树市', '2309', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4950', '360983', '360900', '江西省', '宜春市', '高安市', '2308', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4951', '361000', '360000', '江西省', '抚州市', '', '2307', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4952', '361002', '361000', '江西省', '抚州市', '临川区', '2306', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4953', '361003', '361000', '江西省', '抚州市', '东乡区', '2305', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4954', '361021', '361000', '江西省', '抚州市', '南城县', '2304', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4955', '361022', '361000', '江西省', '抚州市', '黎川县', '2303', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4956', '361023', '361000', '江西省', '抚州市', '南丰县', '2302', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4957', '361024', '361000', '江西省', '抚州市', '崇仁县', '2301', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4958', '361025', '361000', '江西省', '抚州市', '乐安县', '2300', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4959', '361026', '361000', '江西省', '抚州市', '宜黄县', '2299', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4960', '361027', '361000', '江西省', '抚州市', '金溪县', '2298', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4961', '361028', '361000', '江西省', '抚州市', '资溪县', '2297', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4962', '361030', '361000', '江西省', '抚州市', '广昌县', '2296', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4963', '361100', '360000', '江西省', '上饶市', '', '2295', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4964', '361102', '361100', '江西省', '上饶市', '信州区', '2294', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4965', '361103', '361100', '江西省', '上饶市', '广丰区', '2293', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4966', '361121', '361100', '江西省', '上饶市', '上饶县', '2292', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4967', '361123', '361100', '江西省', '上饶市', '玉山县', '2291', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4968', '361124', '361100', '江西省', '上饶市', '铅山县', '2290', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4969', '361125', '361100', '江西省', '上饶市', '横峰县', '2289', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4970', '361126', '361100', '江西省', '上饶市', '弋阳县', '2288', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4971', '361127', '361100', '江西省', '上饶市', '余干县', '2287', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4972', '361128', '361100', '江西省', '上饶市', '鄱阳县', '2286', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4973', '361129', '361100', '江西省', '上饶市', '万年县', '2285', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4974', '361130', '361100', '江西省', '上饶市', '婺源县', '2284', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4975', '361181', '361100', '江西省', '上饶市', '德兴市', '2283', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4976', '370000', '-1', '山东省', '', '', '2282', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4977', '370100', '370000', '山东省', '济南市', '', '2281', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4978', '370102', '370100', '山东省', '济南市', '历下区', '2280', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4979', '370103', '370100', '山东省', '济南市', '市中区', '2279', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4980', '370104', '370100', '山东省', '济南市', '槐荫区', '2278', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4981', '370105', '370100', '山东省', '济南市', '天桥区', '2277', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4982', '370112', '370100', '山东省', '济南市', '历城区', '2276', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4983', '370113', '370100', '山东省', '济南市', '长清区', '2275', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4984', '370114', '370100', '山东省', '济南市', '章丘区', '2274', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4985', '370124', '370100', '山东省', '济南市', '平阴县', '2273', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4986', '370125', '370100', '山东省', '济南市', '济阳县', '2272', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4987', '370126', '370100', '山东省', '济南市', '商河县', '2271', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4988', '370200', '370000', '山东省', '青岛市', '', '2270', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4989', '370202', '370200', '山东省', '青岛市', '市南区', '2269', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4990', '370203', '370200', '山东省', '青岛市', '市北区', '2268', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4991', '370211', '370200', '山东省', '青岛市', '黄岛区', '2267', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4992', '370212', '370200', '山东省', '青岛市', '崂山区', '2266', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4993', '370213', '370200', '山东省', '青岛市', '李沧区', '2265', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4994', '370214', '370200', '山东省', '青岛市', '城阳区', '2264', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4995', '370281', '370200', '山东省', '青岛市', '胶州市', '2263', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4996', '370282', '370200', '山东省', '青岛市', '即墨市', '2262', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4997', '370283', '370200', '山东省', '青岛市', '平度市', '2261', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4998', '370285', '370200', '山东省', '青岛市', '莱西市', '2260', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('4999', '370300', '370000', '山东省', '淄博市', '', '2259', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5000', '370302', '370300', '山东省', '淄博市', '淄川区', '2258', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5001', '370303', '370300', '山东省', '淄博市', '张店区', '2257', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5002', '370304', '370300', '山东省', '淄博市', '博山区', '2256', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5003', '370305', '370300', '山东省', '淄博市', '临淄区', '2255', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5004', '370306', '370300', '山东省', '淄博市', '周村区', '2254', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5005', '370321', '370300', '山东省', '淄博市', '桓台县', '2253', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5006', '370322', '370300', '山东省', '淄博市', '高青县', '2252', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5007', '370323', '370300', '山东省', '淄博市', '沂源县', '2251', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5008', '370400', '370000', '山东省', '枣庄市', '', '2250', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5009', '370402', '370400', '山东省', '枣庄市', '市中区', '2249', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5010', '370403', '370400', '山东省', '枣庄市', '薛城区', '2248', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5011', '370404', '370400', '山东省', '枣庄市', '峄城区', '2247', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5012', '370405', '370400', '山东省', '枣庄市', '台儿庄区', '2246', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5013', '370406', '370400', '山东省', '枣庄市', '山亭区', '2245', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5014', '370481', '370400', '山东省', '枣庄市', '滕州市', '2244', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5015', '370500', '370000', '山东省', '东营市', '', '2243', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5016', '370502', '370500', '山东省', '东营市', '东营区', '2242', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5017', '370503', '370500', '山东省', '东营市', '河口区', '2241', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5018', '370505', '370500', '山东省', '东营市', '垦利区', '2240', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5019', '370522', '370500', '山东省', '东营市', '利津县', '2239', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5020', '370523', '370500', '山东省', '东营市', '广饶县', '2238', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5021', '370600', '370000', '山东省', '烟台市', '', '2237', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5022', '370602', '370600', '山东省', '烟台市', '芝罘区', '2236', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5023', '370611', '370600', '山东省', '烟台市', '福山区', '2235', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5024', '370612', '370600', '山东省', '烟台市', '牟平区', '2234', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5025', '370613', '370600', '山东省', '烟台市', '莱山区', '2233', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5026', '370634', '370600', '山东省', '烟台市', '长岛县', '2232', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5027', '370681', '370600', '山东省', '烟台市', '龙口市', '2231', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5028', '370682', '370600', '山东省', '烟台市', '莱阳市', '2230', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5029', '370683', '370600', '山东省', '烟台市', '莱州市', '2229', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5030', '370684', '370600', '山东省', '烟台市', '蓬莱市', '2228', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5031', '370685', '370600', '山东省', '烟台市', '招远市', '2227', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5032', '370686', '370600', '山东省', '烟台市', '栖霞市', '2226', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5033', '370687', '370600', '山东省', '烟台市', '海阳市', '2225', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5034', '370700', '370000', '山东省', '潍坊市', '', '2224', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5035', '370702', '370700', '山东省', '潍坊市', '潍城区', '2223', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5036', '370703', '370700', '山东省', '潍坊市', '寒亭区', '2222', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5037', '370704', '370700', '山东省', '潍坊市', '坊子区', '2221', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5038', '370705', '370700', '山东省', '潍坊市', '奎文区', '2220', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5039', '370724', '370700', '山东省', '潍坊市', '临朐县', '2219', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5040', '370725', '370700', '山东省', '潍坊市', '昌乐县', '2218', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5041', '370781', '370700', '山东省', '潍坊市', '青州市', '2217', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5042', '370782', '370700', '山东省', '潍坊市', '诸城市', '2216', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5043', '370783', '370700', '山东省', '潍坊市', '寿光市', '2215', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5044', '370784', '370700', '山东省', '潍坊市', '安丘市', '2214', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5045', '370785', '370700', '山东省', '潍坊市', '高密市', '2213', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5046', '370786', '370700', '山东省', '潍坊市', '昌邑市', '2212', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5047', '370800', '370000', '山东省', '济宁市', '', '2211', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5048', '370811', '370800', '山东省', '济宁市', '任城区', '2210', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5049', '370812', '370800', '山东省', '济宁市', '兖州区', '2209', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5050', '370826', '370800', '山东省', '济宁市', '微山县', '2208', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5051', '370827', '370800', '山东省', '济宁市', '鱼台县', '2207', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5052', '370828', '370800', '山东省', '济宁市', '金乡县', '2206', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5053', '370829', '370800', '山东省', '济宁市', '嘉祥县', '2205', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5054', '370830', '370800', '山东省', '济宁市', '汶上县', '2204', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5055', '370831', '370800', '山东省', '济宁市', '泗水县', '2203', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5056', '370832', '370800', '山东省', '济宁市', '梁山县', '2202', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5057', '370881', '370800', '山东省', '济宁市', '曲阜市', '2201', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5058', '370883', '370800', '山东省', '济宁市', '邹城市', '2200', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5059', '370900', '370000', '山东省', '泰安市', '', '2199', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5060', '370902', '370900', '山东省', '泰安市', '泰山区', '2198', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5061', '370911', '370900', '山东省', '泰安市', '岱岳区', '2197', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5062', '370921', '370900', '山东省', '泰安市', '宁阳县', '2196', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5063', '370923', '370900', '山东省', '泰安市', '东平县', '2195', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5064', '370982', '370900', '山东省', '泰安市', '新泰市', '2194', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5065', '370983', '370900', '山东省', '泰安市', '肥城市', '2193', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5066', '371000', '370000', '山东省', '威海市', '', '2192', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5067', '371002', '371000', '山东省', '威海市', '环翠区', '2191', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5068', '371003', '371000', '山东省', '威海市', '文登区', '2190', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5069', '371082', '371000', '山东省', '威海市', '荣成市', '2189', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5070', '371083', '371000', '山东省', '威海市', '乳山市', '2188', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5071', '371100', '370000', '山东省', '日照市', '', '2187', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5072', '371102', '371100', '山东省', '日照市', '东港区', '2186', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5073', '371103', '371100', '山东省', '日照市', '岚山区', '2185', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5074', '371121', '371100', '山东省', '日照市', '五莲县', '2184', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5075', '371122', '371100', '山东省', '日照市', '莒县', '2183', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5076', '371200', '370000', '山东省', '莱芜市', '', '2182', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5077', '371202', '371200', '山东省', '莱芜市', '莱城区', '2181', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5078', '371203', '371200', '山东省', '莱芜市', '钢城区', '2180', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5079', '371300', '370000', '山东省', '临沂市', '', '2179', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5080', '371302', '371300', '山东省', '临沂市', '兰山区', '2178', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5081', '371311', '371300', '山东省', '临沂市', '罗庄区', '2177', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5082', '371312', '371300', '山东省', '临沂市', '河东区', '2176', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5083', '371321', '371300', '山东省', '临沂市', '沂南县', '2175', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5084', '371322', '371300', '山东省', '临沂市', '郯城县', '2174', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5085', '371323', '371300', '山东省', '临沂市', '沂水县', '2173', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5086', '371324', '371300', '山东省', '临沂市', '兰陵县', '2172', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5087', '371325', '371300', '山东省', '临沂市', '费县', '2171', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5088', '371326', '371300', '山东省', '临沂市', '平邑县', '2170', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5089', '371327', '371300', '山东省', '临沂市', '莒南县', '2169', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5090', '371328', '371300', '山东省', '临沂市', '蒙阴县', '2168', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5091', '371329', '371300', '山东省', '临沂市', '临沭县', '2167', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5092', '371400', '370000', '山东省', '德州市', '', '2166', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5093', '371402', '371400', '山东省', '德州市', '德城区', '2165', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5094', '371403', '371400', '山东省', '德州市', '陵城区', '2164', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5095', '371422', '371400', '山东省', '德州市', '宁津县', '2163', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5096', '371423', '371400', '山东省', '德州市', '庆云县', '2162', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5097', '371424', '371400', '山东省', '德州市', '临邑县', '2161', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5098', '371425', '371400', '山东省', '德州市', '齐河县', '2160', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5099', '371426', '371400', '山东省', '德州市', '平原县', '2159', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5100', '371427', '371400', '山东省', '德州市', '夏津县', '2158', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5101', '371428', '371400', '山东省', '德州市', '武城县', '2157', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5102', '371481', '371400', '山东省', '德州市', '乐陵市', '2156', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5103', '371482', '371400', '山东省', '德州市', '禹城市', '2155', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5104', '371500', '370000', '山东省', '聊城市', '', '2154', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5105', '371502', '371500', '山东省', '聊城市', '东昌府区', '2153', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5106', '371521', '371500', '山东省', '聊城市', '阳谷县', '2152', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5107', '371522', '371500', '山东省', '聊城市', '莘县', '2151', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5108', '371523', '371500', '山东省', '聊城市', '茌平县', '2150', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5109', '371524', '371500', '山东省', '聊城市', '东阿县', '2149', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5110', '371525', '371500', '山东省', '聊城市', '冠县', '2148', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5111', '371526', '371500', '山东省', '聊城市', '高唐县', '2147', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5112', '371581', '371500', '山东省', '聊城市', '临清市', '2146', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5113', '371600', '370000', '山东省', '滨州市', '', '2145', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5114', '371602', '371600', '山东省', '滨州市', '滨城区', '2144', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5115', '371603', '371600', '山东省', '滨州市', '沾化区', '2143', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5116', '371621', '371600', '山东省', '滨州市', '惠民县', '2142', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5117', '371622', '371600', '山东省', '滨州市', '阳信县', '2141', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5118', '371623', '371600', '山东省', '滨州市', '无棣县', '2140', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5119', '371625', '371600', '山东省', '滨州市', '博兴县', '2139', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5120', '371626', '371600', '山东省', '滨州市', '邹平县', '2138', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5121', '371700', '370000', '山东省', '菏泽市', '', '2137', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5122', '371702', '371700', '山东省', '菏泽市', '牡丹区', '2136', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5123', '371703', '371700', '山东省', '菏泽市', '定陶区', '2135', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5124', '371721', '371700', '山东省', '菏泽市', '曹县', '2134', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5125', '371722', '371700', '山东省', '菏泽市', '单县', '2133', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5126', '371723', '371700', '山东省', '菏泽市', '成武县', '2132', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5127', '371724', '371700', '山东省', '菏泽市', '巨野县', '2131', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5128', '371725', '371700', '山东省', '菏泽市', '郓城县', '2130', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5129', '371726', '371700', '山东省', '菏泽市', '鄄城县', '2129', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5130', '371728', '371700', '山东省', '菏泽市', '东明县', '2128', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5131', '410000', '-1', '河南省', '', '', '2127', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5132', '410100', '410000', '河南省', '郑州市', '', '2126', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5133', '410102', '410100', '河南省', '郑州市', '中原区', '2125', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5134', '410103', '410100', '河南省', '郑州市', '二七区', '2124', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5135', '410104', '410100', '河南省', '郑州市', '管城回族区', '2123', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5136', '410105', '410100', '河南省', '郑州市', '金水区', '2122', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5137', '410106', '410100', '河南省', '郑州市', '上街区', '2121', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5138', '410108', '410100', '河南省', '郑州市', '惠济区', '2120', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5139', '410122', '410100', '河南省', '郑州市', '中牟县', '2119', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5140', '410181', '410100', '河南省', '郑州市', '巩义市', '2118', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5141', '410182', '410100', '河南省', '郑州市', '荥阳市', '2117', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5142', '410183', '410100', '河南省', '郑州市', '新密市', '2116', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5143', '410184', '410100', '河南省', '郑州市', '新郑市', '2115', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5144', '410185', '410100', '河南省', '郑州市', '登封市', '2114', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5145', '410200', '410000', '河南省', '开封市', '', '2113', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5146', '410202', '410200', '河南省', '开封市', '龙亭区', '2112', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5147', '410203', '410200', '河南省', '开封市', '顺河回族区', '2111', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5148', '410204', '410200', '河南省', '开封市', '鼓楼区', '2110', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5149', '410205', '410200', '河南省', '开封市', '禹王台区', '2109', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5150', '410212', '410200', '河南省', '开封市', '祥符区', '2108', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5151', '410221', '410200', '河南省', '开封市', '杞县', '2107', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5152', '410222', '410200', '河南省', '开封市', '通许县', '2106', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5153', '410223', '410200', '河南省', '开封市', '尉氏县', '2105', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5154', '410225', '410200', '河南省', '开封市', '兰考县', '2104', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5155', '410300', '410000', '河南省', '洛阳市', '', '2103', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5156', '410302', '410300', '河南省', '洛阳市', '老城区', '2102', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5157', '410303', '410300', '河南省', '洛阳市', '西工区', '2101', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5158', '410304', '410300', '河南省', '洛阳市', '瀍河回族区', '2100', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5159', '410305', '410300', '河南省', '洛阳市', '涧西区', '2099', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5160', '410306', '410300', '河南省', '洛阳市', '吉利区', '2098', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5161', '410311', '410300', '河南省', '洛阳市', '洛龙区', '2097', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5162', '410322', '410300', '河南省', '洛阳市', '孟津县', '2096', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5163', '410323', '410300', '河南省', '洛阳市', '新安县', '2095', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5164', '410324', '410300', '河南省', '洛阳市', '栾川县', '2094', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5165', '410325', '410300', '河南省', '洛阳市', '嵩县', '2093', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5166', '410326', '410300', '河南省', '洛阳市', '汝阳县', '2092', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5167', '410327', '410300', '河南省', '洛阳市', '宜阳县', '2091', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5168', '410328', '410300', '河南省', '洛阳市', '洛宁县', '2090', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5169', '410329', '410300', '河南省', '洛阳市', '伊川县', '2089', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5170', '410381', '410300', '河南省', '洛阳市', '偃师市', '2088', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5171', '410400', '410000', '河南省', '平顶山市', '', '2087', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5172', '410402', '410400', '河南省', '平顶山市', '新华区', '2086', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5173', '410403', '410400', '河南省', '平顶山市', '卫东区', '2085', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5174', '410404', '410400', '河南省', '平顶山市', '石龙区', '2084', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5175', '410411', '410400', '河南省', '平顶山市', '湛河区', '2083', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5176', '410421', '410400', '河南省', '平顶山市', '宝丰县', '2082', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5177', '410422', '410400', '河南省', '平顶山市', '叶县', '2081', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5178', '410423', '410400', '河南省', '平顶山市', '鲁山县', '2080', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5179', '410425', '410400', '河南省', '平顶山市', '郏县', '2079', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5180', '410481', '410400', '河南省', '平顶山市', '舞钢市', '2078', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5181', '410482', '410400', '河南省', '平顶山市', '汝州市', '2077', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5182', '410500', '410000', '河南省', '安阳市', '', '2076', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5183', '410502', '410500', '河南省', '安阳市', '文峰区', '2075', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5184', '410503', '410500', '河南省', '安阳市', '北关区', '2074', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5185', '410505', '410500', '河南省', '安阳市', '殷都区', '2073', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5186', '410506', '410500', '河南省', '安阳市', '龙安区', '2072', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5187', '410522', '410500', '河南省', '安阳市', '安阳县', '2071', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5188', '410523', '410500', '河南省', '安阳市', '汤阴县', '2070', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5189', '410526', '410500', '河南省', '安阳市', '滑县', '2069', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5190', '410527', '410500', '河南省', '安阳市', '内黄县', '2068', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5191', '410581', '410500', '河南省', '安阳市', '林州市', '2067', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5192', '410600', '410000', '河南省', '鹤壁市', '', '2066', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5193', '410602', '410600', '河南省', '鹤壁市', '鹤山区', '2065', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5194', '410603', '410600', '河南省', '鹤壁市', '山城区', '2064', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5195', '410611', '410600', '河南省', '鹤壁市', '淇滨区', '2063', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5196', '410621', '410600', '河南省', '鹤壁市', '浚县', '2062', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5197', '410622', '410600', '河南省', '鹤壁市', '淇县', '2061', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5198', '410700', '410000', '河南省', '新乡市', '', '2060', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5199', '410702', '410700', '河南省', '新乡市', '红旗区', '2059', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5200', '410703', '410700', '河南省', '新乡市', '卫滨区', '2058', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5201', '410704', '410700', '河南省', '新乡市', '凤泉区', '2057', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5202', '410711', '410700', '河南省', '新乡市', '牧野区', '2056', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5203', '410721', '410700', '河南省', '新乡市', '新乡县', '2055', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5204', '410724', '410700', '河南省', '新乡市', '获嘉县', '2054', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5205', '410725', '410700', '河南省', '新乡市', '原阳县', '2053', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5206', '410726', '410700', '河南省', '新乡市', '延津县', '2052', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5207', '410727', '410700', '河南省', '新乡市', '封丘县', '2051', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5208', '410728', '410700', '河南省', '新乡市', '长垣县', '2050', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5209', '410781', '410700', '河南省', '新乡市', '卫辉市', '2049', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5210', '410782', '410700', '河南省', '新乡市', '辉县市', '2048', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5211', '410800', '410000', '河南省', '焦作市', '', '2047', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5212', '410802', '410800', '河南省', '焦作市', '解放区', '2046', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5213', '410803', '410800', '河南省', '焦作市', '中站区', '2045', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5214', '410804', '410800', '河南省', '焦作市', '马村区', '2044', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5215', '410811', '410800', '河南省', '焦作市', '山阳区', '2043', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5216', '410821', '410800', '河南省', '焦作市', '修武县', '2042', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5217', '410822', '410800', '河南省', '焦作市', '博爱县', '2041', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5218', '410823', '410800', '河南省', '焦作市', '武陟县', '2040', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5219', '410825', '410800', '河南省', '焦作市', '温县', '2039', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5220', '410882', '410800', '河南省', '焦作市', '沁阳市', '2038', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5221', '410883', '410800', '河南省', '焦作市', '孟州市', '2037', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5222', '410900', '410000', '河南省', '濮阳市', '', '2036', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5223', '410902', '410900', '河南省', '濮阳市', '华龙区', '2035', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5224', '410922', '410900', '河南省', '濮阳市', '清丰县', '2034', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5225', '410923', '410900', '河南省', '濮阳市', '南乐县', '2033', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5226', '410926', '410900', '河南省', '濮阳市', '范县', '2032', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5227', '410927', '410900', '河南省', '濮阳市', '台前县', '2031', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5228', '410928', '410900', '河南省', '濮阳市', '濮阳县', '2030', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5229', '411000', '410000', '河南省', '许昌市', '', '2029', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5230', '411002', '411000', '河南省', '许昌市', '魏都区', '2028', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5231', '411003', '411000', '河南省', '许昌市', '建安区', '2027', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5232', '411024', '411000', '河南省', '许昌市', '鄢陵县', '2026', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5233', '411025', '411000', '河南省', '许昌市', '襄城县', '2025', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5234', '411081', '411000', '河南省', '许昌市', '禹州市', '2024', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5235', '411082', '411000', '河南省', '许昌市', '长葛市', '2023', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5236', '411100', '410000', '河南省', '漯河市', '', '2022', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5237', '411102', '411100', '河南省', '漯河市', '源汇区', '2021', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5238', '411103', '411100', '河南省', '漯河市', '郾城区', '2020', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5239', '411104', '411100', '河南省', '漯河市', '召陵区', '2019', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5240', '411121', '411100', '河南省', '漯河市', '舞阳县', '2018', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5241', '411122', '411100', '河南省', '漯河市', '临颍县', '2017', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5242', '411200', '410000', '河南省', '三门峡市', '', '2016', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5243', '411202', '411200', '河南省', '三门峡市', '湖滨区', '2015', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5244', '411203', '411200', '河南省', '三门峡市', '陕州区', '2014', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5245', '411221', '411200', '河南省', '三门峡市', '渑池县', '2013', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5246', '411224', '411200', '河南省', '三门峡市', '卢氏县', '2012', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5247', '411281', '411200', '河南省', '三门峡市', '义马市', '2011', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5248', '411282', '411200', '河南省', '三门峡市', '灵宝市', '2010', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5249', '411300', '410000', '河南省', '南阳市', '', '2009', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5250', '411302', '411300', '河南省', '南阳市', '宛城区', '2008', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5251', '411303', '411300', '河南省', '南阳市', '卧龙区', '2007', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5252', '411321', '411300', '河南省', '南阳市', '南召县', '2006', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5253', '411322', '411300', '河南省', '南阳市', '方城县', '2005', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5254', '411323', '411300', '河南省', '南阳市', '西峡县', '2004', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5255', '411324', '411300', '河南省', '南阳市', '镇平县', '2003', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5256', '411325', '411300', '河南省', '南阳市', '内乡县', '2002', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5257', '411326', '411300', '河南省', '南阳市', '淅川县', '2001', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5258', '411327', '411300', '河南省', '南阳市', '社旗县', '2000', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5259', '411328', '411300', '河南省', '南阳市', '唐河县', '1999', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5260', '411329', '411300', '河南省', '南阳市', '新野县', '1998', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5261', '411330', '411300', '河南省', '南阳市', '桐柏县', '1997', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5262', '411381', '411300', '河南省', '南阳市', '邓州市', '1996', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5263', '411400', '410000', '河南省', '商丘市', '', '1995', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5264', '411402', '411400', '河南省', '商丘市', '梁园区', '1994', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5265', '411403', '411400', '河南省', '商丘市', '睢阳区', '1993', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5266', '411421', '411400', '河南省', '商丘市', '民权县', '1992', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5267', '411422', '411400', '河南省', '商丘市', '睢县', '1991', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5268', '411423', '411400', '河南省', '商丘市', '宁陵县', '1990', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5269', '411424', '411400', '河南省', '商丘市', '柘城县', '1989', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5270', '411425', '411400', '河南省', '商丘市', '虞城县', '1988', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5271', '411426', '411400', '河南省', '商丘市', '夏邑县', '1987', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5272', '411481', '411400', '河南省', '商丘市', '永城市', '1986', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5273', '411500', '410000', '河南省', '信阳市', '', '1985', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5274', '411502', '411500', '河南省', '信阳市', '浉河区', '1984', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5275', '411503', '411500', '河南省', '信阳市', '平桥区', '1983', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5276', '411521', '411500', '河南省', '信阳市', '罗山县', '1982', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5277', '411522', '411500', '河南省', '信阳市', '光山县', '1981', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5278', '411523', '411500', '河南省', '信阳市', '新县', '1980', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5279', '411524', '411500', '河南省', '信阳市', '商城县', '1979', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5280', '411525', '411500', '河南省', '信阳市', '固始县', '1978', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5281', '411526', '411500', '河南省', '信阳市', '潢川县', '1977', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5282', '411527', '411500', '河南省', '信阳市', '淮滨县', '1976', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5283', '411528', '411500', '河南省', '信阳市', '息县', '1975', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5284', '411600', '410000', '河南省', '周口市', '', '1974', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5285', '411602', '411600', '河南省', '周口市', '川汇区', '1973', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5286', '411621', '411600', '河南省', '周口市', '扶沟县', '1972', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5287', '411622', '411600', '河南省', '周口市', '西华县', '1971', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5288', '411623', '411600', '河南省', '周口市', '商水县', '1970', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5289', '411624', '411600', '河南省', '周口市', '沈丘县', '1969', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5290', '411625', '411600', '河南省', '周口市', '郸城县', '1968', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5291', '411626', '411600', '河南省', '周口市', '淮阳县', '1967', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5292', '411627', '411600', '河南省', '周口市', '太康县', '1966', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5293', '411628', '411600', '河南省', '周口市', '鹿邑县', '1965', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5294', '411681', '411600', '河南省', '周口市', '项城市', '1964', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5295', '411700', '410000', '河南省', '驻马店市', '', '1963', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5296', '411702', '411700', '河南省', '驻马店市', '驿城区', '1962', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5297', '411721', '411700', '河南省', '驻马店市', '西平县', '1961', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5298', '411722', '411700', '河南省', '驻马店市', '上蔡县', '1960', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5299', '411723', '411700', '河南省', '驻马店市', '平舆县', '1959', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5300', '411724', '411700', '河南省', '驻马店市', '正阳县', '1958', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5301', '411725', '411700', '河南省', '驻马店市', '确山县', '1957', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5302', '411726', '411700', '河南省', '驻马店市', '泌阳县', '1956', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5303', '411727', '411700', '河南省', '驻马店市', '汝南县', '1955', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5304', '411728', '411700', '河南省', '驻马店市', '遂平县', '1954', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5305', '411729', '411700', '河南省', '驻马店市', '新蔡县', '1953', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5306', '419001', 'null', '河南省', '济源市', '济源市', '1952', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5307', '420000', '-1', '湖北省', '', '', '1951', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5308', '420100', '420000', '湖北省', '武汉市', '', '1950', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5309', '420102', '420100', '湖北省', '武汉市', '江岸区', '1949', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5310', '420103', '420100', '湖北省', '武汉市', '江汉区', '1948', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5311', '420104', '420100', '湖北省', '武汉市', '硚口区', '1947', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5312', '420105', '420100', '湖北省', '武汉市', '汉阳区', '1946', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5313', '420106', '420100', '湖北省', '武汉市', '武昌区', '1945', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5314', '420107', '420100', '湖北省', '武汉市', '青山区', '1944', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5315', '420111', '420100', '湖北省', '武汉市', '洪山区', '1943', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5316', '420112', '420100', '湖北省', '武汉市', '东西湖区', '1942', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5317', '420113', '420100', '湖北省', '武汉市', '汉南区', '1941', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5318', '420114', '420100', '湖北省', '武汉市', '蔡甸区', '1940', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5319', '420115', '420100', '湖北省', '武汉市', '江夏区', '1939', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5320', '420116', '420100', '湖北省', '武汉市', '黄陂区', '1938', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5321', '420117', '420100', '湖北省', '武汉市', '新洲区', '1937', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5322', '420200', '420000', '湖北省', '黄石市', '', '1936', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5323', '420202', '420200', '湖北省', '黄石市', '黄石港区', '1935', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5324', '420203', '420200', '湖北省', '黄石市', '西塞山区', '1934', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5325', '420204', '420200', '湖北省', '黄石市', '下陆区', '1933', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5326', '420205', '420200', '湖北省', '黄石市', '铁山区', '1932', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5327', '420222', '420200', '湖北省', '黄石市', '阳新县', '1931', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5328', '420281', '420200', '湖北省', '黄石市', '大冶市', '1930', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5329', '420300', '420000', '湖北省', '十堰市', '', '1929', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5330', '420302', '420300', '湖北省', '十堰市', '茅箭区', '1928', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5331', '420303', '420300', '湖北省', '十堰市', '张湾区', '1927', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5332', '420304', '420300', '湖北省', '十堰市', '郧阳区', '1926', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5333', '420322', '420300', '湖北省', '十堰市', '郧西县', '1925', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5334', '420323', '420300', '湖北省', '十堰市', '竹山县', '1924', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5335', '420324', '420300', '湖北省', '十堰市', '竹溪县', '1923', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5336', '420325', '420300', '湖北省', '十堰市', '房县', '1922', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5337', '420381', '420300', '湖北省', '十堰市', '丹江口市', '1921', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5338', '420500', '420000', '湖北省', '宜昌市', '', '1920', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5339', '420502', '420500', '湖北省', '宜昌市', '西陵区', '1919', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5340', '420503', '420500', '湖北省', '宜昌市', '伍家岗区', '1918', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5341', '420504', '420500', '湖北省', '宜昌市', '点军区', '1917', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5342', '420505', '420500', '湖北省', '宜昌市', '猇亭区', '1916', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5343', '420506', '420500', '湖北省', '宜昌市', '夷陵区', '1915', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5344', '420525', '420500', '湖北省', '宜昌市', '远安县', '1914', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5345', '420526', '420500', '湖北省', '宜昌市', '兴山县', '1913', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5346', '420527', '420500', '湖北省', '宜昌市', '秭归县', '1912', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5347', '420528', '420500', '湖北省', '宜昌市', '长阳土家族自治县', '1911', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5348', '420529', '420500', '湖北省', '宜昌市', '五峰土家族自治县', '1910', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5349', '420581', '420500', '湖北省', '宜昌市', '宜都市', '1909', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5350', '420582', '420500', '湖北省', '宜昌市', '当阳市', '1908', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5351', '420583', '420500', '湖北省', '宜昌市', '枝江市', '1907', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5352', '420600', '420000', '湖北省', '襄阳市', '', '1906', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5353', '420602', '420600', '湖北省', '襄阳市', '襄城区', '1905', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5354', '420606', '420600', '湖北省', '襄阳市', '樊城区', '1904', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5355', '420607', '420600', '湖北省', '襄阳市', '襄州区', '1903', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5356', '420624', '420600', '湖北省', '襄阳市', '南漳县', '1902', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5357', '420625', '420600', '湖北省', '襄阳市', '谷城县', '1901', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5358', '420626', '420600', '湖北省', '襄阳市', '保康县', '1900', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5359', '420682', '420600', '湖北省', '襄阳市', '老河口市', '1899', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5360', '420683', '420600', '湖北省', '襄阳市', '枣阳市', '1898', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5361', '420684', '420600', '湖北省', '襄阳市', '宜城市', '1897', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5362', '420700', '420000', '湖北省', '鄂州市', '', '1896', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5363', '420702', '420700', '湖北省', '鄂州市', '梁子湖区', '1895', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5364', '420703', '420700', '湖北省', '鄂州市', '华容区', '1894', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5365', '420704', '420700', '湖北省', '鄂州市', '鄂城区', '1893', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5366', '420800', '420000', '湖北省', '荆门市', '', '1892', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5367', '420802', '420800', '湖北省', '荆门市', '东宝区', '1891', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5368', '420804', '420800', '湖北省', '荆门市', '掇刀区', '1890', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5369', '420821', '420800', '湖北省', '荆门市', '京山县', '1889', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5370', '420822', '420800', '湖北省', '荆门市', '沙洋县', '1888', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5371', '420881', '420800', '湖北省', '荆门市', '钟祥市', '1887', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5372', '420900', '420000', '湖北省', '孝感市', '', '1886', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5373', '420902', '420900', '湖北省', '孝感市', '孝南区', '1885', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5374', '420921', '420900', '湖北省', '孝感市', '孝昌县', '1884', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5375', '420922', '420900', '湖北省', '孝感市', '大悟县', '1883', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5376', '420923', '420900', '湖北省', '孝感市', '云梦县', '1882', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5377', '420981', '420900', '湖北省', '孝感市', '应城市', '1881', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5378', '420982', '420900', '湖北省', '孝感市', '安陆市', '1880', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5379', '420984', '420900', '湖北省', '孝感市', '汉川市', '1879', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5380', '421000', '420000', '湖北省', '荆州市', '', '1878', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5381', '421002', '421000', '湖北省', '荆州市', '沙市区', '1877', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5382', '421003', '421000', '湖北省', '荆州市', '荆州区', '1876', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5383', '421022', '421000', '湖北省', '荆州市', '公安县', '1875', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5384', '421023', '421000', '湖北省', '荆州市', '监利县', '1874', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5385', '421024', '421000', '湖北省', '荆州市', '江陵县', '1873', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5386', '421081', '421000', '湖北省', '荆州市', '石首市', '1872', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5387', '421083', '421000', '湖北省', '荆州市', '洪湖市', '1871', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5388', '421087', '421000', '湖北省', '荆州市', '松滋市', '1870', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5389', '421100', '420000', '湖北省', '黄冈市', '', '1869', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5390', '421102', '421100', '湖北省', '黄冈市', '黄州区', '1868', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5391', '421121', '421100', '湖北省', '黄冈市', '团风县', '1867', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5392', '421122', '421100', '湖北省', '黄冈市', '红安县', '1866', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5393', '421123', '421100', '湖北省', '黄冈市', '罗田县', '1865', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5394', '421124', '421100', '湖北省', '黄冈市', '英山县', '1864', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5395', '421125', '421100', '湖北省', '黄冈市', '浠水县', '1863', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5396', '421126', '421100', '湖北省', '黄冈市', '蕲春县', '1862', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5397', '421127', '421100', '湖北省', '黄冈市', '黄梅县', '1861', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5398', '421181', '421100', '湖北省', '黄冈市', '麻城市', '1860', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5399', '421182', '421100', '湖北省', '黄冈市', '武穴市', '1859', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5400', '421200', '420000', '湖北省', '咸宁市', '', '1858', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5401', '421202', '421200', '湖北省', '咸宁市', '咸安区', '1857', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5402', '421221', '421200', '湖北省', '咸宁市', '嘉鱼县', '1856', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5403', '421222', '421200', '湖北省', '咸宁市', '通城县', '1855', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5404', '421223', '421200', '湖北省', '咸宁市', '崇阳县', '1854', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5405', '421224', '421200', '湖北省', '咸宁市', '通山县', '1853', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5406', '421281', '421200', '湖北省', '咸宁市', '赤壁市', '1852', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5407', '421300', '420000', '湖北省', '随州市', '', '1851', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5408', '421303', '421300', '湖北省', '随州市', '曾都区', '1850', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5409', '421321', '421300', '湖北省', '随州市', '随县', '1849', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5410', '421381', '421300', '湖北省', '随州市', '广水市', '1848', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5411', '422800', '420000', '湖北省', '恩施土家族苗族自治州', '', '1847', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5412', '422801', '422800', '湖北省', '恩施土家族苗族自治州', '恩施市', '1846', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5413', '422802', '422800', '湖北省', '恩施土家族苗族自治州', '利川市', '1845', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5414', '422822', '422800', '湖北省', '恩施土家族苗族自治州', '建始县', '1844', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5415', '422823', '422800', '湖北省', '恩施土家族苗族自治州', '巴东县', '1843', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5416', '422825', '422800', '湖北省', '恩施土家族苗族自治州', '宣恩县', '1842', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5417', '422826', '422800', '湖北省', '恩施土家族苗族自治州', '咸丰县', '1841', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5418', '422827', '422800', '湖北省', '恩施土家族苗族自治州', '来凤县', '1840', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5419', '422828', '422800', '湖北省', '恩施土家族苗族自治州', '鹤峰县', '1839', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5420', '429004', 'null', '湖北省', '仙桃市', '仙桃市', '1838', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5421', '429005', 'null', '湖北省', '潜江市', '潜江市', '1837', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5422', '429006', 'null', '湖北省', '天门市', '天门市', '1836', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5423', '429021', 'null', '湖北省', '神农架林区', '神农架林区', '1835', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5424', '430000', '-1', '湖南省', '', '', '1834', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5425', '430100', '430000', '湖南省', '长沙市', '', '1833', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5426', '430102', '430100', '湖南省', '长沙市', '芙蓉区', '1832', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5427', '430103', '430100', '湖南省', '长沙市', '天心区', '1831', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5428', '430104', '430100', '湖南省', '长沙市', '岳麓区', '1830', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5429', '430105', '430100', '湖南省', '长沙市', '开福区', '1829', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5430', '430111', '430100', '湖南省', '长沙市', '雨花区', '1828', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5431', '430112', '430100', '湖南省', '长沙市', '望城区', '1827', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5432', '430121', '430100', '湖南省', '长沙市', '长沙县', '1826', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5433', '430181', '430100', '湖南省', '长沙市', '浏阳市', '1825', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5434', '430182', '430100', '湖南省', '长沙市', '宁乡市', '1824', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5435', '430200', '430000', '湖南省', '株洲市', '', '1823', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5436', '430202', '430200', '湖南省', '株洲市', '荷塘区', '1822', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5437', '430203', '430200', '湖南省', '株洲市', '芦淞区', '1821', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5438', '430204', '430200', '湖南省', '株洲市', '石峰区', '1820', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5439', '430211', '430200', '湖南省', '株洲市', '天元区', '1819', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5440', '430221', '430200', '湖南省', '株洲市', '株洲县', '1818', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5441', '430223', '430200', '湖南省', '株洲市', '攸县', '1817', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5442', '430224', '430200', '湖南省', '株洲市', '茶陵县', '1816', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5443', '430225', '430200', '湖南省', '株洲市', '炎陵县', '1815', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5444', '430281', '430200', '湖南省', '株洲市', '醴陵市', '1814', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5445', '430300', '430000', '湖南省', '湘潭市', '', '1813', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5446', '430302', '430300', '湖南省', '湘潭市', '雨湖区', '1812', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5447', '430304', '430300', '湖南省', '湘潭市', '岳塘区', '1811', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5448', '430321', '430300', '湖南省', '湘潭市', '湘潭县', '1810', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5449', '430381', '430300', '湖南省', '湘潭市', '湘乡市', '1809', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5450', '430382', '430300', '湖南省', '湘潭市', '韶山市', '1808', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5451', '430400', '430000', '湖南省', '衡阳市', '', '1807', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5452', '430405', '430400', '湖南省', '衡阳市', '珠晖区', '1806', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5453', '430406', '430400', '湖南省', '衡阳市', '雁峰区', '1805', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5454', '430407', '430400', '湖南省', '衡阳市', '石鼓区', '1804', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5455', '430408', '430400', '湖南省', '衡阳市', '蒸湘区', '1803', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5456', '430412', '430400', '湖南省', '衡阳市', '南岳区', '1802', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5457', '430421', '430400', '湖南省', '衡阳市', '衡阳县', '1801', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5458', '430422', '430400', '湖南省', '衡阳市', '衡南县', '1800', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5459', '430423', '430400', '湖南省', '衡阳市', '衡山县', '1799', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5460', '430424', '430400', '湖南省', '衡阳市', '衡东县', '1798', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5461', '430426', '430400', '湖南省', '衡阳市', '祁东县', '1797', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5462', '430481', '430400', '湖南省', '衡阳市', '耒阳市', '1796', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5463', '430482', '430400', '湖南省', '衡阳市', '常宁市', '1795', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5464', '430500', '430000', '湖南省', '邵阳市', '', '1794', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5465', '430502', '430500', '湖南省', '邵阳市', '双清区', '1793', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5466', '430503', '430500', '湖南省', '邵阳市', '大祥区', '1792', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5467', '430511', '430500', '湖南省', '邵阳市', '北塔区', '1791', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5468', '430521', '430500', '湖南省', '邵阳市', '邵东县', '1790', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5469', '430522', '430500', '湖南省', '邵阳市', '新邵县', '1789', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5470', '430523', '430500', '湖南省', '邵阳市', '邵阳县', '1788', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5471', '430524', '430500', '湖南省', '邵阳市', '隆回县', '1787', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5472', '430525', '430500', '湖南省', '邵阳市', '洞口县', '1786', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5473', '430527', '430500', '湖南省', '邵阳市', '绥宁县', '1785', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5474', '430528', '430500', '湖南省', '邵阳市', '新宁县', '1784', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5475', '430529', '430500', '湖南省', '邵阳市', '城步苗族自治县', '1783', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5476', '430581', '430500', '湖南省', '邵阳市', '武冈市', '1782', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5477', '430600', '430000', '湖南省', '岳阳市', '', '1781', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5478', '430602', '430600', '湖南省', '岳阳市', '岳阳楼区', '1780', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5479', '430603', '430600', '湖南省', '岳阳市', '云溪区', '1779', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5480', '430611', '430600', '湖南省', '岳阳市', '君山区', '1778', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5481', '430621', '430600', '湖南省', '岳阳市', '岳阳县', '1777', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5482', '430623', '430600', '湖南省', '岳阳市', '华容县', '1776', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5483', '430624', '430600', '湖南省', '岳阳市', '湘阴县', '1775', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5484', '430626', '430600', '湖南省', '岳阳市', '平江县', '1774', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5485', '430681', '430600', '湖南省', '岳阳市', '汨罗市', '1773', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5486', '430682', '430600', '湖南省', '岳阳市', '临湘市', '1772', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5487', '430700', '430000', '湖南省', '常德市', '', '1771', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5488', '430702', '430700', '湖南省', '常德市', '武陵区', '1770', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5489', '430703', '430700', '湖南省', '常德市', '鼎城区', '1769', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5490', '430721', '430700', '湖南省', '常德市', '安乡县', '1768', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5491', '430722', '430700', '湖南省', '常德市', '汉寿县', '1767', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5492', '430723', '430700', '湖南省', '常德市', '澧县', '1766', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5493', '430724', '430700', '湖南省', '常德市', '临澧县', '1765', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5494', '430725', '430700', '湖南省', '常德市', '桃源县', '1764', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5495', '430726', '430700', '湖南省', '常德市', '石门县', '1763', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5496', '430781', '430700', '湖南省', '常德市', '津市市', '1762', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5497', '430800', '430000', '湖南省', '张家界市', '', '1761', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5498', '430802', '430800', '湖南省', '张家界市', '永定区', '1760', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5499', '430811', '430800', '湖南省', '张家界市', '武陵源区', '1759', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5500', '430821', '430800', '湖南省', '张家界市', '慈利县', '1758', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5501', '430822', '430800', '湖南省', '张家界市', '桑植县', '1757', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5502', '430900', '430000', '湖南省', '益阳市', '', '1756', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5503', '430902', '430900', '湖南省', '益阳市', '资阳区', '1755', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5504', '430903', '430900', '湖南省', '益阳市', '赫山区', '1754', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5505', '430921', '430900', '湖南省', '益阳市', '南县', '1753', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5506', '430922', '430900', '湖南省', '益阳市', '桃江县', '1752', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5507', '430923', '430900', '湖南省', '益阳市', '安化县', '1751', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5508', '430981', '430900', '湖南省', '益阳市', '沅江市', '1750', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5509', '431000', '430000', '湖南省', '郴州市', '', '1749', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5510', '431002', '431000', '湖南省', '郴州市', '北湖区', '1748', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5511', '431003', '431000', '湖南省', '郴州市', '苏仙区', '1747', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5512', '431021', '431000', '湖南省', '郴州市', '桂阳县', '1746', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5513', '431022', '431000', '湖南省', '郴州市', '宜章县', '1745', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5514', '431023', '431000', '湖南省', '郴州市', '永兴县', '1744', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5515', '431024', '431000', '湖南省', '郴州市', '嘉禾县', '1743', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5516', '431025', '431000', '湖南省', '郴州市', '临武县', '1742', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5517', '431026', '431000', '湖南省', '郴州市', '汝城县', '1741', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5518', '431027', '431000', '湖南省', '郴州市', '桂东县', '1740', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5519', '431028', '431000', '湖南省', '郴州市', '安仁县', '1739', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5520', '431081', '431000', '湖南省', '郴州市', '资兴市', '1738', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5521', '431100', '430000', '湖南省', '永州市', '', '1737', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5522', '431102', '431100', '湖南省', '永州市', '零陵区', '1736', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5523', '431103', '431100', '湖南省', '永州市', '冷水滩区', '1735', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5524', '431121', '431100', '湖南省', '永州市', '祁阳县', '1734', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5525', '431122', '431100', '湖南省', '永州市', '东安县', '1733', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5526', '431123', '431100', '湖南省', '永州市', '双牌县', '1732', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5527', '431124', '431100', '湖南省', '永州市', '道县', '1731', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5528', '431125', '431100', '湖南省', '永州市', '江永县', '1730', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5529', '431126', '431100', '湖南省', '永州市', '宁远县', '1729', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5530', '431127', '431100', '湖南省', '永州市', '蓝山县', '1728', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5531', '431128', '431100', '湖南省', '永州市', '新田县', '1727', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5532', '431129', '431100', '湖南省', '永州市', '江华瑶族自治县', '1726', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5533', '431200', '430000', '湖南省', '怀化市', '', '1725', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5534', '431202', '431200', '湖南省', '怀化市', '鹤城区', '1724', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5535', '431221', '431200', '湖南省', '怀化市', '中方县', '1723', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5536', '431222', '431200', '湖南省', '怀化市', '沅陵县', '1722', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5537', '431223', '431200', '湖南省', '怀化市', '辰溪县', '1721', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5538', '431224', '431200', '湖南省', '怀化市', '溆浦县', '1720', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5539', '431225', '431200', '湖南省', '怀化市', '会同县', '1719', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5540', '431226', '431200', '湖南省', '怀化市', '麻阳苗族自治县', '1718', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5541', '431227', '431200', '湖南省', '怀化市', '新晃侗族自治县', '1717', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5542', '431228', '431200', '湖南省', '怀化市', '芷江侗族自治县', '1716', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5543', '431229', '431200', '湖南省', '怀化市', '靖州苗族侗族自治县', '1715', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5544', '431230', '431200', '湖南省', '怀化市', '通道侗族自治县', '1714', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5545', '431281', '431200', '湖南省', '怀化市', '洪江市', '1713', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5546', '431300', '430000', '湖南省', '娄底市', '', '1712', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5547', '431302', '431300', '湖南省', '娄底市', '娄星区', '1711', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5548', '431321', '431300', '湖南省', '娄底市', '双峰县', '1710', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5549', '431322', '431300', '湖南省', '娄底市', '新化县', '1709', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5550', '431381', '431300', '湖南省', '娄底市', '冷水江市', '1708', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5551', '431382', '431300', '湖南省', '娄底市', '涟源市', '1707', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5552', '433100', '430000', '湖南省', '湘西土家族苗族自治州', '', '1706', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5553', '433101', '433100', '湖南省', '湘西土家族苗族自治州', '吉首市', '1705', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5554', '433122', '433100', '湖南省', '湘西土家族苗族自治州', '泸溪县', '1704', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5555', '433123', '433100', '湖南省', '湘西土家族苗族自治州', '凤凰县', '1703', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5556', '433124', '433100', '湖南省', '湘西土家族苗族自治州', '花垣县', '1702', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5557', '433125', '433100', '湖南省', '湘西土家族苗族自治州', '保靖县', '1701', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5558', '433126', '433100', '湖南省', '湘西土家族苗族自治州', '古丈县', '1700', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5559', '433127', '433100', '湖南省', '湘西土家族苗族自治州', '永顺县', '1699', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5560', '433130', '433100', '湖南省', '湘西土家族苗族自治州', '龙山县', '1698', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5561', '440000', '-1', '广东省', '', '', '1697', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5562', '440100', '440000', '广东省', '广州市', '', '1696', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5563', '440103', '440100', '广东省', '广州市', '荔湾区', '1695', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5564', '440104', '440100', '广东省', '广州市', '越秀区', '1694', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5565', '440105', '440100', '广东省', '广州市', '海珠区', '1693', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5566', '440106', '440100', '广东省', '广州市', '天河区', '1692', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5567', '440111', '440100', '广东省', '广州市', '白云区', '1691', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5568', '440112', '440100', '广东省', '广州市', '黄埔区', '1690', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5569', '440113', '440100', '广东省', '广州市', '番禺区', '1689', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5570', '440114', '440100', '广东省', '广州市', '花都区', '1688', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5571', '440115', '440100', '广东省', '广州市', '南沙区', '1687', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5572', '440117', '440100', '广东省', '广州市', '从化区', '1686', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5573', '440118', '440100', '广东省', '广州市', '增城区', '1685', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5574', '440200', '440000', '广东省', '韶关市', '', '1684', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5575', '440203', '440200', '广东省', '韶关市', '武江区', '1683', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5576', '440204', '440200', '广东省', '韶关市', '浈江区', '1682', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5577', '440205', '440200', '广东省', '韶关市', '曲江区', '1681', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5578', '440222', '440200', '广东省', '韶关市', '始兴县', '1680', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5579', '440224', '440200', '广东省', '韶关市', '仁化县', '1679', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5580', '440229', '440200', '广东省', '韶关市', '翁源县', '1678', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5581', '440232', '440200', '广东省', '韶关市', '乳源瑶族自治县', '1677', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5582', '440233', '440200', '广东省', '韶关市', '新丰县', '1676', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5583', '440281', '440200', '广东省', '韶关市', '乐昌市', '1675', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5584', '440282', '440200', '广东省', '韶关市', '南雄市', '1674', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5585', '440300', '440000', '广东省', '深圳市', '', '1673', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5586', '440303', '440300', '广东省', '深圳市', '罗湖区', '1672', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5587', '440304', '440300', '广东省', '深圳市', '福田区', '1671', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5588', '440305', '440300', '广东省', '深圳市', '南山区', '1670', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5589', '440306', '440300', '广东省', '深圳市', '宝安区', '1669', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5590', '440307', '440300', '广东省', '深圳市', '龙岗区', '1668', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5591', '440308', '440300', '广东省', '深圳市', '盐田区', '1667', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5592', '440309', '440300', '广东省', '深圳市', '龙华区', '1666', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5593', '440310', '440300', '广东省', '深圳市', '坪山区', '1665', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5594', '440400', '440000', '广东省', '珠海市', '', '1664', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5595', '440402', '440400', '广东省', '珠海市', '香洲区', '1663', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5596', '440403', '440400', '广东省', '珠海市', '斗门区', '1662', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5597', '440404', '440400', '广东省', '珠海市', '金湾区', '1661', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5598', '440499', '440400', '广东省', '珠海市', '香洲区(由澳门特别行政区实施管辖)', '1660', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5599', '440500', '440000', '广东省', '汕头市', '', '1659', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5600', '440507', '440500', '广东省', '汕头市', '龙湖区', '1658', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5601', '440511', '440500', '广东省', '汕头市', '金平区', '1657', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5602', '440512', '440500', '广东省', '汕头市', '濠江区', '1656', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5603', '440513', '440500', '广东省', '汕头市', '潮阳区', '1655', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5604', '440514', '440500', '广东省', '汕头市', '潮南区', '1654', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5605', '440515', '440500', '广东省', '汕头市', '澄海区', '1653', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5606', '440523', '440500', '广东省', '汕头市', '南澳县', '1652', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5607', '440600', '440000', '广东省', '佛山市', '', '1651', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5608', '440604', '440600', '广东省', '佛山市', '禅城区', '1650', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5609', '440605', '440600', '广东省', '佛山市', '南海区', '1649', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5610', '440606', '440600', '广东省', '佛山市', '顺德区', '1648', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5611', '440607', '440600', '广东省', '佛山市', '三水区', '1647', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5612', '440608', '440600', '广东省', '佛山市', '高明区', '1646', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5613', '440700', '440000', '广东省', '江门市', '', '1645', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5614', '440703', '440700', '广东省', '江门市', '蓬江区', '1644', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5615', '440704', '440700', '广东省', '江门市', '江海区', '1643', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5616', '440705', '440700', '广东省', '江门市', '新会区', '1642', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5617', '440781', '440700', '广东省', '江门市', '台山市', '1641', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5618', '440783', '440700', '广东省', '江门市', '开平市', '1640', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5619', '440784', '440700', '广东省', '江门市', '鹤山市', '1639', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5620', '440785', '440700', '广东省', '江门市', '恩平市', '1638', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5621', '440800', '440000', '广东省', '湛江市', '', '1637', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5622', '440802', '440800', '广东省', '湛江市', '赤坎区', '1636', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5623', '440803', '440800', '广东省', '湛江市', '霞山区', '1635', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5624', '440804', '440800', '广东省', '湛江市', '坡头区', '1634', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5625', '440811', '440800', '广东省', '湛江市', '麻章区', '1633', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5626', '440823', '440800', '广东省', '湛江市', '遂溪县', '1632', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5627', '440825', '440800', '广东省', '湛江市', '徐闻县', '1631', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5628', '440881', '440800', '广东省', '湛江市', '廉江市', '1630', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5629', '440882', '440800', '广东省', '湛江市', '雷州市', '1629', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5630', '440883', '440800', '广东省', '湛江市', '吴川市', '1628', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5631', '440900', '440000', '广东省', '茂名市', '', '1627', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5632', '440902', '440900', '广东省', '茂名市', '茂南区', '1626', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5633', '440904', '440900', '广东省', '茂名市', '电白区', '1625', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5634', '440981', '440900', '广东省', '茂名市', '高州市', '1624', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5635', '440982', '440900', '广东省', '茂名市', '化州市', '1623', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5636', '440983', '440900', '广东省', '茂名市', '信宜市', '1622', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5637', '441200', '440000', '广东省', '肇庆市', '', '1621', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5638', '441202', '441200', '广东省', '肇庆市', '端州区', '1620', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5639', '441203', '441200', '广东省', '肇庆市', '鼎湖区', '1619', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5640', '441204', '441200', '广东省', '肇庆市', '高要区', '1618', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5641', '441223', '441200', '广东省', '肇庆市', '广宁县', '1617', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5642', '441224', '441200', '广东省', '肇庆市', '怀集县', '1616', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5643', '441225', '441200', '广东省', '肇庆市', '封开县', '1615', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5644', '441226', '441200', '广东省', '肇庆市', '德庆县', '1614', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5645', '441284', '441200', '广东省', '肇庆市', '四会市', '1613', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5646', '441300', '440000', '广东省', '惠州市', '', '1612', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5647', '441302', '441300', '广东省', '惠州市', '惠城区', '1611', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5648', '441303', '441300', '广东省', '惠州市', '惠阳区', '1610', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5649', '441322', '441300', '广东省', '惠州市', '博罗县', '1609', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5650', '441323', '441300', '广东省', '惠州市', '惠东县', '1608', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5651', '441324', '441300', '广东省', '惠州市', '龙门县', '1607', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5652', '441400', '440000', '广东省', '梅州市', '', '1606', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5653', '441402', '441400', '广东省', '梅州市', '梅江区', '1605', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5654', '441403', '441400', '广东省', '梅州市', '梅县区', '1604', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5655', '441422', '441400', '广东省', '梅州市', '大埔县', '1603', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5656', '441423', '441400', '广东省', '梅州市', '丰顺县', '1602', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5657', '441424', '441400', '广东省', '梅州市', '五华县', '1601', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5658', '441426', '441400', '广东省', '梅州市', '平远县', '1600', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5659', '441427', '441400', '广东省', '梅州市', '蕉岭县', '1599', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5660', '441481', '441400', '广东省', '梅州市', '兴宁市', '1598', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5661', '441500', '440000', '广东省', '汕尾市', '', '1597', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5662', '441502', '441500', '广东省', '汕尾市', '城区', '1596', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5663', '441521', '441500', '广东省', '汕尾市', '海丰县', '1595', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5664', '441523', '441500', '广东省', '汕尾市', '陆河县', '1594', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5665', '441581', '441500', '广东省', '汕尾市', '陆丰市', '1593', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5666', '441600', '440000', '广东省', '河源市', '', '1592', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5667', '441602', '441600', '广东省', '河源市', '源城区', '1591', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5668', '441621', '441600', '广东省', '河源市', '紫金县', '1590', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5669', '441622', '441600', '广东省', '河源市', '龙川县', '1589', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5670', '441623', '441600', '广东省', '河源市', '连平县', '1588', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5671', '441624', '441600', '广东省', '河源市', '和平县', '1587', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5672', '441625', '441600', '广东省', '河源市', '东源县', '1586', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5673', '441700', '440000', '广东省', '阳江市', '', '1585', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5674', '441702', '441700', '广东省', '阳江市', '江城区', '1584', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5675', '441704', '441700', '广东省', '阳江市', '阳东区', '1583', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5676', '441721', '441700', '广东省', '阳江市', '阳西县', '1582', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5677', '441781', '441700', '广东省', '阳江市', '阳春市', '1581', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5678', '441800', '440000', '广东省', '清远市', '', '1580', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5679', '441802', '441800', '广东省', '清远市', '清城区', '1579', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5680', '441803', '441800', '广东省', '清远市', '清新区', '1578', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5681', '441821', '441800', '广东省', '清远市', '佛冈县', '1577', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5682', '441823', '441800', '广东省', '清远市', '阳山县', '1576', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5683', '441825', '441800', '广东省', '清远市', '连山壮族瑶族自治县', '1575', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5684', '441826', '441800', '广东省', '清远市', '连南瑶族自治县', '1574', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5685', '441881', '441800', '广东省', '清远市', '英德市', '1573', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5686', '441882', '441800', '广东省', '清远市', '连州市', '1572', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5687', '441900', '440000', '广东省', '东莞市', '', '1571', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5688', '442000', '440000', '广东省', '中山市', '', '1570', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5689', '445100', '440000', '广东省', '潮州市', '', '1569', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5690', '445102', '445100', '广东省', '潮州市', '湘桥区', '1568', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5691', '445103', '445100', '广东省', '潮州市', '潮安区', '1567', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5692', '445122', '445100', '广东省', '潮州市', '饶平县', '1566', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5693', '445200', '440000', '广东省', '揭阳市', '', '1565', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5694', '445202', '445200', '广东省', '揭阳市', '榕城区', '1564', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5695', '445203', '445200', '广东省', '揭阳市', '揭东区', '1563', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5696', '445222', '445200', '广东省', '揭阳市', '揭西县', '1562', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5697', '445224', '445200', '广东省', '揭阳市', '惠来县', '1561', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5698', '445281', '445200', '广东省', '揭阳市', '普宁市', '1560', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5699', '445300', '440000', '广东省', '云浮市', '', '1559', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5700', '445302', '445300', '广东省', '云浮市', '云城区', '1558', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5701', '445303', '445300', '广东省', '云浮市', '云安区', '1557', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5702', '445321', '445300', '广东省', '云浮市', '新兴县', '1556', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5703', '445322', '445300', '广东省', '云浮市', '郁南县', '1555', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5704', '445381', '445300', '广东省', '云浮市', '罗定市', '1554', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5705', '450000', '-1', '广西壮族自治区', '', '', '1553', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5706', '450100', '450000', '广西壮族自治区', '南宁市', '', '1552', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5707', '450102', '450100', '广西壮族自治区', '南宁市', '兴宁区', '1551', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5708', '450103', '450100', '广西壮族自治区', '南宁市', '青秀区', '1550', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5709', '450105', '450100', '广西壮族自治区', '南宁市', '江南区', '1549', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5710', '450107', '450100', '广西壮族自治区', '南宁市', '西乡塘区', '1548', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5711', '450108', '450100', '广西壮族自治区', '南宁市', '良庆区', '1547', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5712', '450109', '450100', '广西壮族自治区', '南宁市', '邕宁区', '1546', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5713', '450110', '450100', '广西壮族自治区', '南宁市', '武鸣区', '1545', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5714', '450123', '450100', '广西壮族自治区', '南宁市', '隆安县', '1544', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5715', '450124', '450100', '广西壮族自治区', '南宁市', '马山县', '1543', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5716', '450125', '450100', '广西壮族自治区', '南宁市', '上林县', '1542', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5717', '450126', '450100', '广西壮族自治区', '南宁市', '宾阳县', '1541', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5718', '450127', '450100', '广西壮族自治区', '南宁市', '横县', '1540', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5719', '450200', '450000', '广西壮族自治区', '柳州市', '', '1539', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5720', '450202', '450200', '广西壮族自治区', '柳州市', '城中区', '1538', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5721', '450203', '450200', '广西壮族自治区', '柳州市', '鱼峰区', '1537', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5722', '450204', '450200', '广西壮族自治区', '柳州市', '柳南区', '1536', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5723', '450205', '450200', '广西壮族自治区', '柳州市', '柳北区', '1535', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5724', '450206', '450200', '广西壮族自治区', '柳州市', '柳江区', '1534', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5725', '450222', '450200', '广西壮族自治区', '柳州市', '柳城县', '1533', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5726', '450223', '450200', '广西壮族自治区', '柳州市', '鹿寨县', '1532', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5727', '450224', '450200', '广西壮族自治区', '柳州市', '融安县', '1531', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5728', '450225', '450200', '广西壮族自治区', '柳州市', '融水苗族自治县', '1530', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5729', '450226', '450200', '广西壮族自治区', '柳州市', '三江侗族自治县', '1529', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5730', '450300', '450000', '广西壮族自治区', '桂林市', '', '1528', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5731', '450302', '450300', '广西壮族自治区', '桂林市', '秀峰区', '1527', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5732', '450303', '450300', '广西壮族自治区', '桂林市', '叠彩区', '1526', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5733', '450304', '450300', '广西壮族自治区', '桂林市', '象山区', '1525', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5734', '450305', '450300', '广西壮族自治区', '桂林市', '七星区', '1524', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5735', '450311', '450300', '广西壮族自治区', '桂林市', '雁山区', '1523', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5736', '450312', '450300', '广西壮族自治区', '桂林市', '临桂区', '1522', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5737', '450321', '450300', '广西壮族自治区', '桂林市', '阳朔县', '1521', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5738', '450323', '450300', '广西壮族自治区', '桂林市', '灵川县', '1520', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5739', '450324', '450300', '广西壮族自治区', '桂林市', '全州县', '1519', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5740', '450325', '450300', '广西壮族自治区', '桂林市', '兴安县', '1518', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5741', '450326', '450300', '广西壮族自治区', '桂林市', '永福县', '1517', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5742', '450327', '450300', '广西壮族自治区', '桂林市', '灌阳县', '1516', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5743', '450328', '450300', '广西壮族自治区', '桂林市', '龙胜各族自治县', '1515', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5744', '450329', '450300', '广西壮族自治区', '桂林市', '资源县', '1514', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5745', '450330', '450300', '广西壮族自治区', '桂林市', '平乐县', '1513', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5746', '450331', '450300', '广西壮族自治区', '桂林市', '荔浦县', '1512', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5747', '450332', '450300', '广西壮族自治区', '桂林市', '恭城瑶族自治县', '1511', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5748', '450400', '450000', '广西壮族自治区', '梧州市', '', '1510', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5749', '450403', '450400', '广西壮族自治区', '梧州市', '万秀区', '1509', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5750', '450405', '450400', '广西壮族自治区', '梧州市', '长洲区', '1508', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5751', '450406', '450400', '广西壮族自治区', '梧州市', '龙圩区', '1507', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5752', '450421', '450400', '广西壮族自治区', '梧州市', '苍梧县', '1506', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5753', '450422', '450400', '广西壮族自治区', '梧州市', '藤县', '1505', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5754', '450423', '450400', '广西壮族自治区', '梧州市', '蒙山县', '1504', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5755', '450481', '450400', '广西壮族自治区', '梧州市', '岑溪市', '1503', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5756', '450500', '450000', '广西壮族自治区', '北海市', '', '1502', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5757', '450502', '450500', '广西壮族自治区', '北海市', '海城区', '1501', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5758', '450503', '450500', '广西壮族自治区', '北海市', '银海区', '1500', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5759', '450512', '450500', '广西壮族自治区', '北海市', '铁山港区', '1499', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5760', '450521', '450500', '广西壮族自治区', '北海市', '合浦县', '1498', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5761', '450600', '450000', '广西壮族自治区', '防城港市', '', '1497', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5762', '450602', '450600', '广西壮族自治区', '防城港市', '港口区', '1496', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5763', '450603', '450600', '广西壮族自治区', '防城港市', '防城区', '1495', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5764', '450621', '450600', '广西壮族自治区', '防城港市', '上思县', '1494', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5765', '450681', '450600', '广西壮族自治区', '防城港市', '东兴市', '1493', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5766', '450700', '450000', '广西壮族自治区', '钦州市', '', '1492', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5767', '450702', '450700', '广西壮族自治区', '钦州市', '钦南区', '1491', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5768', '450703', '450700', '广西壮族自治区', '钦州市', '钦北区', '1490', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5769', '450721', '450700', '广西壮族自治区', '钦州市', '灵山县', '1489', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5770', '450722', '450700', '广西壮族自治区', '钦州市', '浦北县', '1488', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5771', '450800', '450000', '广西壮族自治区', '贵港市', '', '1487', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5772', '450802', '450800', '广西壮族自治区', '贵港市', '港北区', '1486', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5773', '450803', '450800', '广西壮族自治区', '贵港市', '港南区', '1485', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5774', '450804', '450800', '广西壮族自治区', '贵港市', '覃塘区', '1484', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5775', '450821', '450800', '广西壮族自治区', '贵港市', '平南县', '1483', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5776', '450881', '450800', '广西壮族自治区', '贵港市', '桂平市', '1482', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5777', '450900', '450000', '广西壮族自治区', '玉林市', '', '1481', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5778', '450902', '450900', '广西壮族自治区', '玉林市', '玉州区', '1480', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5779', '450903', '450900', '广西壮族自治区', '玉林市', '福绵区', '1479', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5780', '450921', '450900', '广西壮族自治区', '玉林市', '容县', '1478', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5781', '450922', '450900', '广西壮族自治区', '玉林市', '陆川县', '1477', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5782', '450923', '450900', '广西壮族自治区', '玉林市', '博白县', '1476', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5783', '450924', '450900', '广西壮族自治区', '玉林市', '兴业县', '1475', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5784', '450981', '450900', '广西壮族自治区', '玉林市', '北流市', '1474', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5785', '451000', '450000', '广西壮族自治区', '百色市', '', '1473', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5786', '451002', '451000', '广西壮族自治区', '百色市', '右江区', '1472', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5787', '451021', '451000', '广西壮族自治区', '百色市', '田阳县', '1471', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5788', '451022', '451000', '广西壮族自治区', '百色市', '田东县', '1470', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5789', '451023', '451000', '广西壮族自治区', '百色市', '平果县', '1469', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5790', '451024', '451000', '广西壮族自治区', '百色市', '德保县', '1468', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5791', '451026', '451000', '广西壮族自治区', '百色市', '那坡县', '1467', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5792', '451027', '451000', '广西壮族自治区', '百色市', '凌云县', '1466', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5793', '451028', '451000', '广西壮族自治区', '百色市', '乐业县', '1465', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5794', '451029', '451000', '广西壮族自治区', '百色市', '田林县', '1464', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5795', '451030', '451000', '广西壮族自治区', '百色市', '西林县', '1463', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5796', '451031', '451000', '广西壮族自治区', '百色市', '隆林各族自治县', '1462', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5797', '451081', '451000', '广西壮族自治区', '百色市', '靖西市', '1461', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5798', '451100', '450000', '广西壮族自治区', '贺州市', '', '1460', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5799', '451102', '451100', '广西壮族自治区', '贺州市', '八步区', '1459', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5800', '451103', '451100', '广西壮族自治区', '贺州市', '平桂区', '1458', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5801', '451121', '451100', '广西壮族自治区', '贺州市', '昭平县', '1457', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5802', '451122', '451100', '广西壮族自治区', '贺州市', '钟山县', '1456', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5803', '451123', '451100', '广西壮族自治区', '贺州市', '富川瑶族自治县', '1455', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5804', '451200', '450000', '广西壮族自治区', '河池市', '', '1454', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5805', '451202', '451200', '广西壮族自治区', '河池市', '金城江区', '1453', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5806', '451203', '451200', '广西壮族自治区', '河池市', '宜州区', '1452', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5807', '451221', '451200', '广西壮族自治区', '河池市', '南丹县', '1451', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5808', '451222', '451200', '广西壮族自治区', '河池市', '天峨县', '1450', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5809', '451223', '451200', '广西壮族自治区', '河池市', '凤山县', '1449', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5810', '451224', '451200', '广西壮族自治区', '河池市', '东兰县', '1448', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5811', '451225', '451200', '广西壮族自治区', '河池市', '罗城仫佬族自治县', '1447', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5812', '451226', '451200', '广西壮族自治区', '河池市', '环江毛南族自治县', '1446', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5813', '451227', '451200', '广西壮族自治区', '河池市', '巴马瑶族自治县', '1445', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5814', '451228', '451200', '广西壮族自治区', '河池市', '都安瑶族自治县', '1444', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5815', '451229', '451200', '广西壮族自治区', '河池市', '大化瑶族自治县', '1443', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5816', '451300', '450000', '广西壮族自治区', '来宾市', '', '1442', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5817', '451302', '451300', '广西壮族自治区', '来宾市', '兴宾区', '1441', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5818', '451321', '451300', '广西壮族自治区', '来宾市', '忻城县', '1440', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5819', '451322', '451300', '广西壮族自治区', '来宾市', '象州县', '1439', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5820', '451323', '451300', '广西壮族自治区', '来宾市', '武宣县', '1438', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5821', '451324', '451300', '广西壮族自治区', '来宾市', '金秀瑶族自治县', '1437', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5822', '451381', '451300', '广西壮族自治区', '来宾市', '合山市', '1436', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5823', '451400', '450000', '广西壮族自治区', '崇左市', '', '1435', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5824', '451402', '451400', '广西壮族自治区', '崇左市', '江州区', '1434', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5825', '451421', '451400', '广西壮族自治区', '崇左市', '扶绥县', '1433', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5826', '451422', '451400', '广西壮族自治区', '崇左市', '宁明县', '1432', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5827', '451423', '451400', '广西壮族自治区', '崇左市', '龙州县', '1431', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5828', '451424', '451400', '广西壮族自治区', '崇左市', '大新县', '1430', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5829', '451425', '451400', '广西壮族自治区', '崇左市', '天等县', '1429', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5830', '451481', '451400', '广西壮族自治区', '崇左市', '凭祥市', '1428', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5831', '460000', '-1', '海南省', '', '', '1427', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5832', '460100', '460000', '海南省', '海口市', '', '1426', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5833', '460105', '460100', '海南省', '海口市', '秀英区', '1425', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5834', '460106', '460100', '海南省', '海口市', '龙华区', '1424', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5835', '460107', '460100', '海南省', '海口市', '琼山区', '1423', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5836', '460108', '460100', '海南省', '海口市', '美兰区', '1422', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5837', '460200', '460000', '海南省', '三亚市', '', '1421', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5838', '460202', '460200', '海南省', '三亚市', '海棠区', '1420', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5839', '460203', '460200', '海南省', '三亚市', '吉阳区', '1419', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5840', '460204', '460200', '海南省', '三亚市', '天涯区', '1418', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5841', '460205', '460200', '海南省', '三亚市', '崖州区', '1417', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5842', '460300', '460000', '海南省', '三沙市', '', '1416', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5843', '460321', '460300', '海南省', '三沙市', '西沙群岛', '1415', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5844', '460322', '460300', '海南省', '三沙市', '南沙群岛', '1414', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5845', '460323', '460300', '海南省', '三沙市', '中沙群岛的岛礁及其海域', '1413', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5846', '460400', '460000', '海南省', '儋州市', '', '1412', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5847', '469001', 'null', '海南省', '五指山市', '五指山市', '1411', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5848', '469002', 'null', '海南省', '琼海市', '琼海市', '1410', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5849', '469005', 'null', '海南省', '文昌市', '文昌市', '1409', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5850', '469006', 'null', '海南省', '万宁市', '万宁市', '1408', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5851', '469007', 'null', '海南省', '东方市', '东方市', '1407', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5852', '469021', 'null', '海南省', '定安县', '定安县', '1406', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5853', '469022', 'null', '海南省', '屯昌县', '屯昌县', '1405', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5854', '469023', 'null', '海南省', '澄迈县', '澄迈县', '1404', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5855', '469024', 'null', '海南省', '临高县', '临高县', '1403', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5856', '469025', 'null', '海南省', '白沙黎族自治县', '白沙黎族自治县', '1402', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5857', '469026', 'null', '海南省', '昌江黎族自治县', '昌江黎族自治县', '1401', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5858', '469027', 'null', '海南省', '乐东黎族自治县', '乐东黎族自治县', '1400', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5859', '469028', 'null', '海南省', '陵水黎族自治县', '陵水黎族自治县', '1399', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5860', '469029', 'null', '海南省', '保亭黎族苗族自治县', '保亭黎族苗族自治县', '1398', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5861', '469030', 'null', '海南省', '琼中黎族苗族自治县', '琼中黎族苗族自治县', '1397', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5862', '500000', '-1', '', '重庆市', '', '1396', '1', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5863', '500101', '500000', '', '重庆市', '万州区', '1395', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5864', '500102', '500000', '', '重庆市', '涪陵区', '1394', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5865', '500103', '500000', '', '重庆市', '渝中区', '1393', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5866', '500104', '500000', '', '重庆市', '大渡口区', '1392', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5867', '500105', '500000', '', '重庆市', '江北区', '1391', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5868', '500106', '500000', '', '重庆市', '沙坪坝区', '1390', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5869', '500107', '500000', '', '重庆市', '九龙坡区', '1389', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5870', '500108', '500000', '', '重庆市', '南岸区', '1388', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5871', '500109', '500000', '', '重庆市', '北碚区', '1387', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5872', '500110', '500000', '', '重庆市', '綦江区', '1386', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5873', '500111', '500000', '', '重庆市', '大足区', '1385', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5874', '500112', '500000', '', '重庆市', '渝北区', '1384', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5875', '500113', '500000', '', '重庆市', '巴南区', '1383', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5876', '500114', '500000', '', '重庆市', '黔江区', '1382', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5877', '500115', '500000', '', '重庆市', '长寿区', '1381', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5878', '500116', '500000', '', '重庆市', '江津区', '1380', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5879', '500117', '500000', '', '重庆市', '合川区', '1379', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5880', '500118', '500000', '', '重庆市', '永川区', '1378', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5881', '500119', '500000', '', '重庆市', '南川区', '1377', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5882', '500120', '500000', '', '重庆市', '璧山区', '1376', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5883', '500151', '500000', '', '重庆市', '铜梁区', '1375', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5884', '500152', '500000', '', '重庆市', '潼南区', '1374', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5885', '500153', '500000', '', '重庆市', '荣昌区', '1373', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5886', '500154', '500000', '', '重庆市', '开州区', '1372', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5887', '500155', '500000', '', '重庆市', '梁平区', '1371', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5888', '500156', '500000', '', '重庆市', '武隆区', '1370', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5889', '500229', '500000', '', '重庆市', '城口县', '1369', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5890', '500230', '500000', '', '重庆市', '丰都县', '1368', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5891', '500231', '500000', '', '重庆市', '垫江县', '1367', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5892', '500233', '500000', '', '重庆市', '忠县', '1366', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5893', '500235', '500000', '', '重庆市', '云阳县', '1365', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5894', '500236', '500000', '', '重庆市', '奉节县', '1364', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5895', '500237', '500000', '', '重庆市', '巫山县', '1363', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5896', '500238', '500000', '', '重庆市', '巫溪县', '1362', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5897', '500240', '500000', '', '重庆市', '石柱土家族自治县', '1361', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5898', '500241', '500000', '', '重庆市', '秀山土家族苗族自治县', '1360', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5899', '500242', '500000', '', '重庆市', '酉阳土家族苗族自治县', '1359', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5900', '500243', '500000', '', '重庆市', '彭水苗族土家族自治县', '1358', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5901', '510000', '-1', '四川省', '', '', '1357', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5902', '510100', '510000', '四川省', '成都市', '', '1356', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5903', '510104', '510100', '四川省', '成都市', '锦江区', '1355', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5904', '510105', '510100', '四川省', '成都市', '青羊区', '1354', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5905', '510106', '510100', '四川省', '成都市', '金牛区', '1353', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5906', '510107', '510100', '四川省', '成都市', '武侯区', '1352', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5907', '510108', '510100', '四川省', '成都市', '成华区', '1351', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5908', '510112', '510100', '四川省', '成都市', '龙泉驿区', '1350', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5909', '510113', '510100', '四川省', '成都市', '青白江区', '1349', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5910', '510114', '510100', '四川省', '成都市', '新都区', '1348', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5911', '510115', '510100', '四川省', '成都市', '温江区', '1347', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5912', '510116', '510100', '四川省', '成都市', '双流区', '1346', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5913', '510117', '510100', '四川省', '成都市', '郫都区', '1345', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5914', '510121', '510100', '四川省', '成都市', '金堂县', '1344', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5915', '510129', '510100', '四川省', '成都市', '大邑县', '1343', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5916', '510131', '510100', '四川省', '成都市', '蒲江县', '1342', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5917', '510132', '510100', '四川省', '成都市', '新津县', '1341', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5918', '510181', '510100', '四川省', '成都市', '都江堰市', '1340', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5919', '510182', '510100', '四川省', '成都市', '彭州市', '1339', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5920', '510183', '510100', '四川省', '成都市', '邛崃市', '1338', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5921', '510184', '510100', '四川省', '成都市', '崇州市', '1337', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5922', '510185', '510100', '四川省', '成都市', '简阳市', '1336', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5923', '510300', '510000', '四川省', '自贡市', '', '1335', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5924', '510302', '510300', '四川省', '自贡市', '自流井区', '1334', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5925', '510303', '510300', '四川省', '自贡市', '贡井区', '1333', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5926', '510304', '510300', '四川省', '自贡市', '大安区', '1332', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5927', '510311', '510300', '四川省', '自贡市', '沿滩区', '1331', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5928', '510321', '510300', '四川省', '自贡市', '荣县', '1330', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5929', '510322', '510300', '四川省', '自贡市', '富顺县', '1329', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5930', '510400', '510000', '四川省', '攀枝花市', '', '1328', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5931', '510402', '510400', '四川省', '攀枝花市', '东区', '1327', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5932', '510403', '510400', '四川省', '攀枝花市', '西区', '1326', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5933', '510411', '510400', '四川省', '攀枝花市', '仁和区', '1325', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5934', '510421', '510400', '四川省', '攀枝花市', '米易县', '1324', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5935', '510422', '510400', '四川省', '攀枝花市', '盐边县', '1323', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5936', '510500', '510000', '四川省', '泸州市', '', '1322', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5937', '510502', '510500', '四川省', '泸州市', '江阳区', '1321', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5938', '510503', '510500', '四川省', '泸州市', '纳溪区', '1320', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5939', '510504', '510500', '四川省', '泸州市', '龙马潭区', '1319', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5940', '510521', '510500', '四川省', '泸州市', '泸县', '1318', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5941', '510522', '510500', '四川省', '泸州市', '合江县', '1317', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5942', '510524', '510500', '四川省', '泸州市', '叙永县', '1316', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5943', '510525', '510500', '四川省', '泸州市', '古蔺县', '1315', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5944', '510600', '510000', '四川省', '德阳市', '', '1314', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5945', '510603', '510600', '四川省', '德阳市', '旌阳区', '1313', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5946', '510623', '510600', '四川省', '德阳市', '中江县', '1312', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5947', '510626', '510600', '四川省', '德阳市', '罗江县', '1311', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5948', '510681', '510600', '四川省', '德阳市', '广汉市', '1310', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5949', '510682', '510600', '四川省', '德阳市', '什邡市', '1309', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5950', '510683', '510600', '四川省', '德阳市', '绵竹市', '1308', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5951', '510700', '510000', '四川省', '绵阳市', '', '1307', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5952', '510703', '510700', '四川省', '绵阳市', '涪城区', '1306', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5953', '510704', '510700', '四川省', '绵阳市', '游仙区', '1305', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5954', '510705', '510700', '四川省', '绵阳市', '安州区', '1304', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5955', '510722', '510700', '四川省', '绵阳市', '三台县', '1303', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5956', '510723', '510700', '四川省', '绵阳市', '盐亭县', '1302', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5957', '510725', '510700', '四川省', '绵阳市', '梓潼县', '1301', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5958', '510726', '510700', '四川省', '绵阳市', '北川羌族自治县', '1300', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5959', '510727', '510700', '四川省', '绵阳市', '平武县', '1299', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5960', '510781', '510700', '四川省', '绵阳市', '江油市', '1298', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5961', '510800', '510000', '四川省', '广元市', '', '1297', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5962', '510802', '510800', '四川省', '广元市', '利州区', '1296', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5963', '510811', '510800', '四川省', '广元市', '昭化区', '1295', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5964', '510812', '510800', '四川省', '广元市', '朝天区', '1294', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5965', '510821', '510800', '四川省', '广元市', '旺苍县', '1293', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5966', '510822', '510800', '四川省', '广元市', '青川县', '1292', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5967', '510823', '510800', '四川省', '广元市', '剑阁县', '1291', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5968', '510824', '510800', '四川省', '广元市', '苍溪县', '1290', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5969', '510900', '510000', '四川省', '遂宁市', '', '1289', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5970', '510903', '510900', '四川省', '遂宁市', '船山区', '1288', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5971', '510904', '510900', '四川省', '遂宁市', '安居区', '1287', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5972', '510921', '510900', '四川省', '遂宁市', '蓬溪县', '1286', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5973', '510922', '510900', '四川省', '遂宁市', '射洪县', '1285', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5974', '510923', '510900', '四川省', '遂宁市', '大英县', '1284', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5975', '511000', '510000', '四川省', '内江市', '', '1283', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5976', '511002', '511000', '四川省', '内江市', '市中区', '1282', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5977', '511011', '511000', '四川省', '内江市', '东兴区', '1281', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5978', '511024', '511000', '四川省', '内江市', '威远县', '1280', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5979', '511025', '511000', '四川省', '内江市', '资中县', '1279', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5980', '511083', '511000', '四川省', '内江市', '隆昌市', '1278', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5981', '511100', '510000', '四川省', '乐山市', '', '1277', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5982', '511102', '511100', '四川省', '乐山市', '市中区', '1276', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5983', '511111', '511100', '四川省', '乐山市', '沙湾区', '1275', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5984', '511112', '511100', '四川省', '乐山市', '五通桥区', '1274', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5985', '511113', '511100', '四川省', '乐山市', '金口河区', '1273', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5986', '511123', '511100', '四川省', '乐山市', '犍为县', '1272', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5987', '511124', '511100', '四川省', '乐山市', '井研县', '1271', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5988', '511126', '511100', '四川省', '乐山市', '夹江县', '1270', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5989', '511129', '511100', '四川省', '乐山市', '沐川县', '1269', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5990', '511132', '511100', '四川省', '乐山市', '峨边彝族自治县', '1268', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5991', '511133', '511100', '四川省', '乐山市', '马边彝族自治县', '1267', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5992', '511181', '511100', '四川省', '乐山市', '峨眉山市', '1266', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5993', '511300', '510000', '四川省', '南充市', '', '1265', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5994', '511302', '511300', '四川省', '南充市', '顺庆区', '1264', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5995', '511303', '511300', '四川省', '南充市', '高坪区', '1263', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5996', '511304', '511300', '四川省', '南充市', '嘉陵区', '1262', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5997', '511321', '511300', '四川省', '南充市', '南部县', '1261', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5998', '511322', '511300', '四川省', '南充市', '营山县', '1260', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('5999', '511323', '511300', '四川省', '南充市', '蓬安县', '1259', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6000', '511324', '511300', '四川省', '南充市', '仪陇县', '1258', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6001', '511325', '511300', '四川省', '南充市', '西充县', '1257', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6002', '511381', '511300', '四川省', '南充市', '阆中市', '1256', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6003', '511400', '510000', '四川省', '眉山市', '', '1255', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6004', '511402', '511400', '四川省', '眉山市', '东坡区', '1254', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6005', '511403', '511400', '四川省', '眉山市', '彭山区', '1253', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6006', '511421', '511400', '四川省', '眉山市', '仁寿县', '1252', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6007', '511423', '511400', '四川省', '眉山市', '洪雅县', '1251', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6008', '511424', '511400', '四川省', '眉山市', '丹棱县', '1250', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6009', '511425', '511400', '四川省', '眉山市', '青神县', '1249', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6010', '511500', '510000', '四川省', '宜宾市', '', '1248', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6011', '511502', '511500', '四川省', '宜宾市', '翠屏区', '1247', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6012', '511503', '511500', '四川省', '宜宾市', '南溪区', '1246', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6013', '511521', '511500', '四川省', '宜宾市', '宜宾县', '1245', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6014', '511523', '511500', '四川省', '宜宾市', '江安县', '1244', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6015', '511524', '511500', '四川省', '宜宾市', '长宁县', '1243', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6016', '511525', '511500', '四川省', '宜宾市', '高县', '1242', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6017', '511526', '511500', '四川省', '宜宾市', '珙县', '1241', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6018', '511527', '511500', '四川省', '宜宾市', '筠连县', '1240', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6019', '511528', '511500', '四川省', '宜宾市', '兴文县', '1239', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6020', '511529', '511500', '四川省', '宜宾市', '屏山县', '1238', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6021', '511600', '510000', '四川省', '广安市', '', '1237', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6022', '511602', '511600', '四川省', '广安市', '广安区', '1236', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6023', '511603', '511600', '四川省', '广安市', '前锋区', '1235', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6024', '511621', '511600', '四川省', '广安市', '岳池县', '1234', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6025', '511622', '511600', '四川省', '广安市', '武胜县', '1233', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6026', '511623', '511600', '四川省', '广安市', '邻水县', '1232', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6027', '511681', '511600', '四川省', '广安市', '华蓥市', '1231', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6028', '511700', '510000', '四川省', '达州市', '', '1230', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6029', '511702', '511700', '四川省', '达州市', '通川区', '1229', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6030', '511703', '511700', '四川省', '达州市', '达川区', '1228', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6031', '511722', '511700', '四川省', '达州市', '宣汉县', '1227', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6032', '511723', '511700', '四川省', '达州市', '开江县', '1226', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6033', '511724', '511700', '四川省', '达州市', '大竹县', '1225', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6034', '511725', '511700', '四川省', '达州市', '渠县', '1224', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6035', '511781', '511700', '四川省', '达州市', '万源市', '1223', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6036', '511800', '510000', '四川省', '雅安市', '', '1222', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6037', '511802', '511800', '四川省', '雅安市', '雨城区', '1221', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6038', '511803', '511800', '四川省', '雅安市', '名山区', '1220', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6039', '511822', '511800', '四川省', '雅安市', '荥经县', '1219', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6040', '511823', '511800', '四川省', '雅安市', '汉源县', '1218', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6041', '511824', '511800', '四川省', '雅安市', '石棉县', '1217', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6042', '511825', '511800', '四川省', '雅安市', '天全县', '1216', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6043', '511826', '511800', '四川省', '雅安市', '芦山县', '1215', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6044', '511827', '511800', '四川省', '雅安市', '宝兴县', '1214', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6045', '511900', '510000', '四川省', '巴中市', '', '1213', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6046', '511902', '511900', '四川省', '巴中市', '巴州区', '1212', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6047', '511903', '511900', '四川省', '巴中市', '恩阳区', '1211', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6048', '511921', '511900', '四川省', '巴中市', '通江县', '1210', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6049', '511922', '511900', '四川省', '巴中市', '南江县', '1209', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6050', '511923', '511900', '四川省', '巴中市', '平昌县', '1208', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6051', '512000', '510000', '四川省', '资阳市', '', '1207', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6052', '512002', '512000', '四川省', '资阳市', '雁江区', '1206', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6053', '512021', '512000', '四川省', '资阳市', '安岳县', '1205', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6054', '512022', '512000', '四川省', '资阳市', '乐至县', '1204', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6055', '513200', '510000', '四川省', '阿坝藏族羌族自治州', '', '1203', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6056', '513201', '513200', '四川省', '阿坝藏族羌族自治州', '马尔康市', '1202', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6057', '513221', '513200', '四川省', '阿坝藏族羌族自治州', '汶川县', '1201', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6058', '513222', '513200', '四川省', '阿坝藏族羌族自治州', '理县', '1200', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6059', '513223', '513200', '四川省', '阿坝藏族羌族自治州', '茂县', '1199', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6060', '513224', '513200', '四川省', '阿坝藏族羌族自治州', '松潘县', '1198', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6061', '513225', '513200', '四川省', '阿坝藏族羌族自治州', '九寨沟县', '1197', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6062', '513226', '513200', '四川省', '阿坝藏族羌族自治州', '金川县', '1196', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6063', '513227', '513200', '四川省', '阿坝藏族羌族自治州', '小金县', '1195', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6064', '513228', '513200', '四川省', '阿坝藏族羌族自治州', '黑水县', '1194', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6065', '513230', '513200', '四川省', '阿坝藏族羌族自治州', '壤塘县', '1193', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6066', '513231', '513200', '四川省', '阿坝藏族羌族自治州', '阿坝县', '1192', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6067', '513232', '513200', '四川省', '阿坝藏族羌族自治州', '若尔盖县', '1191', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6068', '513233', '513200', '四川省', '阿坝藏族羌族自治州', '红原县', '1190', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6069', '513300', '510000', '四川省', '甘孜藏族自治州', '', '1189', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6070', '513301', '513300', '四川省', '甘孜藏族自治州', '康定市', '1188', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6071', '513322', '513300', '四川省', '甘孜藏族自治州', '泸定县', '1187', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6072', '513323', '513300', '四川省', '甘孜藏族自治州', '丹巴县', '1186', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6073', '513324', '513300', '四川省', '甘孜藏族自治州', '九龙县', '1185', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6074', '513325', '513300', '四川省', '甘孜藏族自治州', '雅江县', '1184', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6075', '513326', '513300', '四川省', '甘孜藏族自治州', '道孚县', '1183', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6076', '513327', '513300', '四川省', '甘孜藏族自治州', '炉霍县', '1182', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6077', '513328', '513300', '四川省', '甘孜藏族自治州', '甘孜县', '1181', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6078', '513329', '513300', '四川省', '甘孜藏族自治州', '新龙县', '1180', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6079', '513330', '513300', '四川省', '甘孜藏族自治州', '德格县', '1179', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6080', '513331', '513300', '四川省', '甘孜藏族自治州', '白玉县', '1178', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6081', '513332', '513300', '四川省', '甘孜藏族自治州', '石渠县', '1177', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6082', '513333', '513300', '四川省', '甘孜藏族自治州', '色达县', '1176', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6083', '513334', '513300', '四川省', '甘孜藏族自治州', '理塘县', '1175', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6084', '513335', '513300', '四川省', '甘孜藏族自治州', '巴塘县', '1174', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6085', '513336', '513300', '四川省', '甘孜藏族自治州', '乡城县', '1173', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6086', '513337', '513300', '四川省', '甘孜藏族自治州', '稻城县', '1172', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6087', '513338', '513300', '四川省', '甘孜藏族自治州', '得荣县', '1171', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6088', '513400', '510000', '四川省', '凉山彝族自治州', '', '1170', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6089', '513401', '513400', '四川省', '凉山彝族自治州', '西昌市', '1169', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6090', '513422', '513400', '四川省', '凉山彝族自治州', '木里藏族自治县', '1168', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6091', '513423', '513400', '四川省', '凉山彝族自治州', '盐源县', '1167', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6092', '513424', '513400', '四川省', '凉山彝族自治州', '德昌县', '1166', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6093', '513425', '513400', '四川省', '凉山彝族自治州', '会理县', '1165', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6094', '513426', '513400', '四川省', '凉山彝族自治州', '会东县', '1164', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6095', '513427', '513400', '四川省', '凉山彝族自治州', '宁南县', '1163', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6096', '513428', '513400', '四川省', '凉山彝族自治州', '普格县', '1162', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6097', '513429', '513400', '四川省', '凉山彝族自治州', '布拖县', '1161', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6098', '513430', '513400', '四川省', '凉山彝族自治州', '金阳县', '1160', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6099', '513431', '513400', '四川省', '凉山彝族自治州', '昭觉县', '1159', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6100', '513432', '513400', '四川省', '凉山彝族自治州', '喜德县', '1158', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6101', '513433', '513400', '四川省', '凉山彝族自治州', '冕宁县', '1157', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6102', '513434', '513400', '四川省', '凉山彝族自治州', '越西县', '1156', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6103', '513435', '513400', '四川省', '凉山彝族自治州', '甘洛县', '1155', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6104', '513436', '513400', '四川省', '凉山彝族自治州', '美姑县', '1154', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6105', '513437', '513400', '四川省', '凉山彝族自治州', '雷波县', '1153', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6106', '520000', '-1', '贵州省', '', '', '1152', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6107', '520100', '520000', '贵州省', '贵阳市', '', '1151', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6108', '520102', '520100', '贵州省', '贵阳市', '南明区', '1150', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6109', '520103', '520100', '贵州省', '贵阳市', '云岩区', '1149', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6110', '520111', '520100', '贵州省', '贵阳市', '花溪区', '1148', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6111', '520112', '520100', '贵州省', '贵阳市', '乌当区', '1147', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6112', '520113', '520100', '贵州省', '贵阳市', '白云区', '1146', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6113', '520115', '520100', '贵州省', '贵阳市', '观山湖区', '1145', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6114', '520121', '520100', '贵州省', '贵阳市', '开阳县', '1144', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6115', '520122', '520100', '贵州省', '贵阳市', '息烽县', '1143', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6116', '520123', '520100', '贵州省', '贵阳市', '修文县', '1142', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6117', '520181', '520100', '贵州省', '贵阳市', '清镇市', '1141', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6118', '520200', '520000', '贵州省', '六盘水市', '', '1140', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6119', '520201', '520200', '贵州省', '六盘水市', '钟山区', '1139', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6120', '520203', '520200', '贵州省', '六盘水市', '六枝特区', '1138', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6121', '520221', '520200', '贵州省', '六盘水市', '水城县', '1137', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6122', '520281', '520200', '贵州省', '六盘水市', '盘州市', '1136', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6123', '520300', '520000', '贵州省', '遵义市', '', '1135', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6124', '520302', '520300', '贵州省', '遵义市', '红花岗区', '1134', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6125', '520303', '520300', '贵州省', '遵义市', '汇川区', '1133', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6126', '520304', '520300', '贵州省', '遵义市', '播州区', '1132', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6127', '520322', '520300', '贵州省', '遵义市', '桐梓县', '1131', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6128', '520323', '520300', '贵州省', '遵义市', '绥阳县', '1130', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6129', '520324', '520300', '贵州省', '遵义市', '正安县', '1129', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6130', '520325', '520300', '贵州省', '遵义市', '道真仡佬族苗族自治县', '1128', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6131', '520326', '520300', '贵州省', '遵义市', '务川仡佬族苗族自治县', '1127', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6132', '520327', '520300', '贵州省', '遵义市', '凤冈县', '1126', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6133', '520328', '520300', '贵州省', '遵义市', '湄潭县', '1125', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6134', '520329', '520300', '贵州省', '遵义市', '余庆县', '1124', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6135', '520330', '520300', '贵州省', '遵义市', '习水县', '1123', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6136', '520381', '520300', '贵州省', '遵义市', '赤水市', '1122', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6137', '520382', '520300', '贵州省', '遵义市', '仁怀市', '1121', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6138', '520400', '520000', '贵州省', '安顺市', '', '1120', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6139', '520402', '520400', '贵州省', '安顺市', '西秀区', '1119', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6140', '520403', '520400', '贵州省', '安顺市', '平坝区', '1118', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6141', '520422', '520400', '贵州省', '安顺市', '普定县', '1117', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6142', '520423', '520400', '贵州省', '安顺市', '镇宁布依族苗族自治县', '1116', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6143', '520424', '520400', '贵州省', '安顺市', '关岭布依族苗族自治县', '1115', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6144', '520425', '520400', '贵州省', '安顺市', '紫云苗族布依族自治县', '1114', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6145', '520500', '520000', '贵州省', '毕节市', '', '1113', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6146', '520502', '520500', '贵州省', '毕节市', '七星关区', '1112', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6147', '520521', '520500', '贵州省', '毕节市', '大方县', '1111', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6148', '520522', '520500', '贵州省', '毕节市', '黔西县', '1110', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6149', '520523', '520500', '贵州省', '毕节市', '金沙县', '1109', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6150', '520524', '520500', '贵州省', '毕节市', '织金县', '1108', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6151', '520525', '520500', '贵州省', '毕节市', '纳雍县', '1107', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6152', '520526', '520500', '贵州省', '毕节市', '威宁彝族回族苗族自治县', '1106', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6153', '520527', '520500', '贵州省', '毕节市', '赫章县', '1105', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6154', '520600', '520000', '贵州省', '铜仁市', '', '1104', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6155', '520602', '520600', '贵州省', '铜仁市', '碧江区', '1103', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6156', '520603', '520600', '贵州省', '铜仁市', '万山区', '1102', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6157', '520621', '520600', '贵州省', '铜仁市', '江口县', '1101', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6158', '520622', '520600', '贵州省', '铜仁市', '玉屏侗族自治县', '1100', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6159', '520623', '520600', '贵州省', '铜仁市', '石阡县', '1099', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6160', '520624', '520600', '贵州省', '铜仁市', '思南县', '1098', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6161', '520625', '520600', '贵州省', '铜仁市', '印江土家族苗族自治县', '1097', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6162', '520626', '520600', '贵州省', '铜仁市', '德江县', '1096', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6163', '520627', '520600', '贵州省', '铜仁市', '沿河土家族自治县', '1095', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6164', '520628', '520600', '贵州省', '铜仁市', '松桃苗族自治县', '1094', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6165', '522300', '520000', '贵州省', '黔西南布依族苗族自治州', '', '1093', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6166', '522301', '522300', '贵州省', '黔西南布依族苗族自治州', '兴义市', '1092', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6167', '522322', '522300', '贵州省', '黔西南布依族苗族自治州', '兴仁县', '1091', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6168', '522323', '522300', '贵州省', '黔西南布依族苗族自治州', '普安县', '1090', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6169', '522324', '522300', '贵州省', '黔西南布依族苗族自治州', '晴隆县', '1089', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6170', '522325', '522300', '贵州省', '黔西南布依族苗族自治州', '贞丰县', '1088', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6171', '522326', '522300', '贵州省', '黔西南布依族苗族自治州', '望谟县', '1087', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6172', '522327', '522300', '贵州省', '黔西南布依族苗族自治州', '册亨县', '1086', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6173', '522328', '522300', '贵州省', '黔西南布依族苗族自治州', '安龙县', '1085', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6174', '522600', '520000', '贵州省', '黔东南苗族侗族自治州', '', '1084', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6175', '522601', '522600', '贵州省', '黔东南苗族侗族自治州', '凯里市', '1083', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6176', '522622', '522600', '贵州省', '黔东南苗族侗族自治州', '黄平县', '1082', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6177', '522623', '522600', '贵州省', '黔东南苗族侗族自治州', '施秉县', '1081', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6178', '522624', '522600', '贵州省', '黔东南苗族侗族自治州', '三穗县', '1080', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6179', '522625', '522600', '贵州省', '黔东南苗族侗族自治州', '镇远县', '1079', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6180', '522626', '522600', '贵州省', '黔东南苗族侗族自治州', '岑巩县', '1078', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6181', '522627', '522600', '贵州省', '黔东南苗族侗族自治州', '天柱县', '1077', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6182', '522628', '522600', '贵州省', '黔东南苗族侗族自治州', '锦屏县', '1076', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6183', '522629', '522600', '贵州省', '黔东南苗族侗族自治州', '剑河县', '1075', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6184', '522630', '522600', '贵州省', '黔东南苗族侗族自治州', '台江县', '1074', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6185', '522631', '522600', '贵州省', '黔东南苗族侗族自治州', '黎平县', '1073', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6186', '522632', '522600', '贵州省', '黔东南苗族侗族自治州', '榕江县', '1072', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6187', '522633', '522600', '贵州省', '黔东南苗族侗族自治州', '从江县', '1071', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6188', '522634', '522600', '贵州省', '黔东南苗族侗族自治州', '雷山县', '1070', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6189', '522635', '522600', '贵州省', '黔东南苗族侗族自治州', '麻江县', '1069', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6190', '522636', '522600', '贵州省', '黔东南苗族侗族自治州', '丹寨县', '1068', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6191', '522700', '520000', '贵州省', '黔南布依族苗族自治州', '', '1067', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6192', '522701', '522700', '贵州省', '黔南布依族苗族自治州', '都匀市', '1066', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6193', '522702', '522700', '贵州省', '黔南布依族苗族自治州', '福泉市', '1065', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6194', '522722', '522700', '贵州省', '黔南布依族苗族自治州', '荔波县', '1064', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6195', '522723', '522700', '贵州省', '黔南布依族苗族自治州', '贵定县', '1063', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6196', '522725', '522700', '贵州省', '黔南布依族苗族自治州', '瓮安县', '1062', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6197', '522726', '522700', '贵州省', '黔南布依族苗族自治州', '独山县', '1061', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6198', '522727', '522700', '贵州省', '黔南布依族苗族自治州', '平塘县', '1060', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6199', '522728', '522700', '贵州省', '黔南布依族苗族自治州', '罗甸县', '1059', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6200', '522729', '522700', '贵州省', '黔南布依族苗族自治州', '长顺县', '1058', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6201', '522730', '522700', '贵州省', '黔南布依族苗族自治州', '龙里县', '1057', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6202', '522731', '522700', '贵州省', '黔南布依族苗族自治州', '惠水县', '1056', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6203', '522732', '522700', '贵州省', '黔南布依族苗族自治州', '三都水族自治县', '1055', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6204', '530000', '-1', '云南省', '', '', '1054', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6205', '530100', '530000', '云南省', '昆明市', '', '1053', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6206', '530102', '530100', '云南省', '昆明市', '五华区', '1052', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6207', '530103', '530100', '云南省', '昆明市', '盘龙区', '1051', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6208', '530111', '530100', '云南省', '昆明市', '官渡区', '1050', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6209', '530112', '530100', '云南省', '昆明市', '西山区', '1049', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6210', '530113', '530100', '云南省', '昆明市', '东川区', '1048', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6211', '530114', '530100', '云南省', '昆明市', '呈贡区', '1047', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6212', '530115', '530100', '云南省', '昆明市', '晋宁区', '1046', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6213', '530124', '530100', '云南省', '昆明市', '富民县', '1045', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6214', '530125', '530100', '云南省', '昆明市', '宜良县', '1044', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6215', '530126', '530100', '云南省', '昆明市', '石林彝族自治县', '1043', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6216', '530127', '530100', '云南省', '昆明市', '嵩明县', '1042', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6217', '530128', '530100', '云南省', '昆明市', '禄劝彝族苗族自治县', '1041', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6218', '530129', '530100', '云南省', '昆明市', '寻甸回族彝族自治县', '1040', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6219', '530181', '530100', '云南省', '昆明市', '安宁市', '1039', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6220', '530300', '530000', '云南省', '曲靖市', '', '1038', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6221', '530302', '530300', '云南省', '曲靖市', '麒麟区', '1037', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6222', '530303', '530300', '云南省', '曲靖市', '沾益区', '1036', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6223', '530321', '530300', '云南省', '曲靖市', '马龙县', '1035', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6224', '530322', '530300', '云南省', '曲靖市', '陆良县', '1034', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6225', '530323', '530300', '云南省', '曲靖市', '师宗县', '1033', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6226', '530324', '530300', '云南省', '曲靖市', '罗平县', '1032', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6227', '530325', '530300', '云南省', '曲靖市', '富源县', '1031', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6228', '530326', '530300', '云南省', '曲靖市', '会泽县', '1030', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6229', '530381', '530300', '云南省', '曲靖市', '宣威市', '1029', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6230', '530400', '530000', '云南省', '玉溪市', '', '1028', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6231', '530402', '530400', '云南省', '玉溪市', '红塔区', '1027', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6232', '530403', '530400', '云南省', '玉溪市', '江川区', '1026', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6233', '530422', '530400', '云南省', '玉溪市', '澄江县', '1025', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6234', '530423', '530400', '云南省', '玉溪市', '通海县', '1024', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6235', '530424', '530400', '云南省', '玉溪市', '华宁县', '1023', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6236', '530425', '530400', '云南省', '玉溪市', '易门县', '1022', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6237', '530426', '530400', '云南省', '玉溪市', '峨山彝族自治县', '1021', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6238', '530427', '530400', '云南省', '玉溪市', '新平彝族傣族自治县', '1020', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6239', '530428', '530400', '云南省', '玉溪市', '元江哈尼族彝族傣族自治县', '1019', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6240', '530500', '530000', '云南省', '保山市', '', '1018', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6241', '530502', '530500', '云南省', '保山市', '隆阳区', '1017', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6242', '530521', '530500', '云南省', '保山市', '施甸县', '1016', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6243', '530523', '530500', '云南省', '保山市', '龙陵县', '1015', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6244', '530524', '530500', '云南省', '保山市', '昌宁县', '1014', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6245', '530581', '530500', '云南省', '保山市', '腾冲市', '1013', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6246', '530600', '530000', '云南省', '昭通市', '', '1012', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6247', '530602', '530600', '云南省', '昭通市', '昭阳区', '1011', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6248', '530621', '530600', '云南省', '昭通市', '鲁甸县', '1010', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6249', '530622', '530600', '云南省', '昭通市', '巧家县', '1009', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6250', '530623', '530600', '云南省', '昭通市', '盐津县', '1008', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6251', '530624', '530600', '云南省', '昭通市', '大关县', '1007', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6252', '530625', '530600', '云南省', '昭通市', '永善县', '1006', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6253', '530626', '530600', '云南省', '昭通市', '绥江县', '1005', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6254', '530627', '530600', '云南省', '昭通市', '镇雄县', '1004', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6255', '530628', '530600', '云南省', '昭通市', '彝良县', '1003', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6256', '530629', '530600', '云南省', '昭通市', '威信县', '1002', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6257', '530630', '530600', '云南省', '昭通市', '水富县', '1001', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6258', '530700', '530000', '云南省', '丽江市', '', '1000', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6259', '530702', '530700', '云南省', '丽江市', '古城区', '999', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6260', '530721', '530700', '云南省', '丽江市', '玉龙纳西族自治县', '998', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6261', '530722', '530700', '云南省', '丽江市', '永胜县', '997', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6262', '530723', '530700', '云南省', '丽江市', '华坪县', '996', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6263', '530724', '530700', '云南省', '丽江市', '宁蒗彝族自治县', '995', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6264', '530800', '530000', '云南省', '普洱市', '', '994', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6265', '530802', '530800', '云南省', '普洱市', '思茅区', '993', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6266', '530821', '530800', '云南省', '普洱市', '宁洱哈尼族彝族自治县', '992', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6267', '530822', '530800', '云南省', '普洱市', '墨江哈尼族自治县', '991', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6268', '530823', '530800', '云南省', '普洱市', '景东彝族自治县', '990', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6269', '530824', '530800', '云南省', '普洱市', '景谷傣族彝族自治县', '989', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6270', '530825', '530800', '云南省', '普洱市', '镇沅彝族哈尼族拉祜族自治县', '988', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6271', '530826', '530800', '云南省', '普洱市', '江城哈尼族彝族自治县', '987', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6272', '530827', '530800', '云南省', '普洱市', '孟连傣族拉祜族佤族自治县', '986', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6273', '530828', '530800', '云南省', '普洱市', '澜沧拉祜族自治县', '985', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6274', '530829', '530800', '云南省', '普洱市', '西盟佤族自治县', '984', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6275', '530900', '530000', '云南省', '临沧市', '', '983', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6276', '530902', '530900', '云南省', '临沧市', '临翔区', '982', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6277', '530921', '530900', '云南省', '临沧市', '凤庆县', '981', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6278', '530922', '530900', '云南省', '临沧市', '云县', '980', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6279', '530923', '530900', '云南省', '临沧市', '永德县', '979', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6280', '530924', '530900', '云南省', '临沧市', '镇康县', '978', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6281', '530925', '530900', '云南省', '临沧市', '双江拉祜族佤族布朗族傣族自治县', '977', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6282', '530926', '530900', '云南省', '临沧市', '耿马傣族佤族自治县', '976', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6283', '530927', '530900', '云南省', '临沧市', '沧源佤族自治县', '975', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6284', '532300', '530000', '云南省', '楚雄彝族自治州', '', '974', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6285', '532301', '532300', '云南省', '楚雄彝族自治州', '楚雄市', '973', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6286', '532322', '532300', '云南省', '楚雄彝族自治州', '双柏县', '972', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6287', '532323', '532300', '云南省', '楚雄彝族自治州', '牟定县', '971', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6288', '532324', '532300', '云南省', '楚雄彝族自治州', '南华县', '970', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6289', '532325', '532300', '云南省', '楚雄彝族自治州', '姚安县', '969', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6290', '532326', '532300', '云南省', '楚雄彝族自治州', '大姚县', '968', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6291', '532327', '532300', '云南省', '楚雄彝族自治州', '永仁县', '967', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6292', '532328', '532300', '云南省', '楚雄彝族自治州', '元谋县', '966', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6293', '532329', '532300', '云南省', '楚雄彝族自治州', '武定县', '965', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6294', '532331', '532300', '云南省', '楚雄彝族自治州', '禄丰县', '964', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6295', '532500', '530000', '云南省', '红河哈尼族彝族自治州', '', '963', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6296', '532501', '532500', '云南省', '红河哈尼族彝族自治州', '个旧市', '962', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6297', '532502', '532500', '云南省', '红河哈尼族彝族自治州', '开远市', '961', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6298', '532503', '532500', '云南省', '红河哈尼族彝族自治州', '蒙自市', '960', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6299', '532504', '532500', '云南省', '红河哈尼族彝族自治州', '弥勒市', '959', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6300', '532523', '532500', '云南省', '红河哈尼族彝族自治州', '屏边苗族自治县', '958', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6301', '532524', '532500', '云南省', '红河哈尼族彝族自治州', '建水县', '957', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6302', '532525', '532500', '云南省', '红河哈尼族彝族自治州', '石屏县', '956', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6303', '532527', '532500', '云南省', '红河哈尼族彝族自治州', '泸西县', '955', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6304', '532528', '532500', '云南省', '红河哈尼族彝族自治州', '元阳县', '954', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6305', '532529', '532500', '云南省', '红河哈尼族彝族自治州', '红河县', '953', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6306', '532530', '532500', '云南省', '红河哈尼族彝族自治州', '金平苗族瑶族傣族自治县', '952', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6307', '532531', '532500', '云南省', '红河哈尼族彝族自治州', '绿春县', '951', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6308', '532532', '532500', '云南省', '红河哈尼族彝族自治州', '河口瑶族自治县', '950', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6309', '532600', '530000', '云南省', '文山壮族苗族自治州', '', '949', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6310', '532601', '532600', '云南省', '文山壮族苗族自治州', '文山市', '948', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6311', '532622', '532600', '云南省', '文山壮族苗族自治州', '砚山县', '947', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6312', '532623', '532600', '云南省', '文山壮族苗族自治州', '西畴县', '946', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6313', '532624', '532600', '云南省', '文山壮族苗族自治州', '麻栗坡县', '945', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6314', '532625', '532600', '云南省', '文山壮族苗族自治州', '马关县', '944', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6315', '532626', '532600', '云南省', '文山壮族苗族自治州', '丘北县', '943', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6316', '532627', '532600', '云南省', '文山壮族苗族自治州', '广南县', '942', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6317', '532628', '532600', '云南省', '文山壮族苗族自治州', '富宁县', '941', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6318', '532800', '530000', '云南省', '西双版纳傣族自治州', '', '940', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6319', '532801', '532800', '云南省', '西双版纳傣族自治州', '景洪市', '939', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6320', '532822', '532800', '云南省', '西双版纳傣族自治州', '勐海县', '938', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6321', '532823', '532800', '云南省', '西双版纳傣族自治州', '勐腊县', '937', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6322', '532900', '530000', '云南省', '大理白族自治州', '', '936', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6323', '532901', '532900', '云南省', '大理白族自治州', '大理市', '935', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6324', '532922', '532900', '云南省', '大理白族自治州', '漾濞彝族自治县', '934', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6325', '532923', '532900', '云南省', '大理白族自治州', '祥云县', '933', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6326', '532924', '532900', '云南省', '大理白族自治州', '宾川县', '932', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6327', '532925', '532900', '云南省', '大理白族自治州', '弥渡县', '931', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6328', '532926', '532900', '云南省', '大理白族自治州', '南涧彝族自治县', '930', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6329', '532927', '532900', '云南省', '大理白族自治州', '巍山彝族回族自治县', '929', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6330', '532928', '532900', '云南省', '大理白族自治州', '永平县', '928', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6331', '532929', '532900', '云南省', '大理白族自治州', '云龙县', '927', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6332', '532930', '532900', '云南省', '大理白族自治州', '洱源县', '926', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6333', '532931', '532900', '云南省', '大理白族自治州', '剑川县', '925', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6334', '532932', '532900', '云南省', '大理白族自治州', '鹤庆县', '924', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6335', '533100', '530000', '云南省', '德宏傣族景颇族自治州', '', '923', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6336', '533102', '533100', '云南省', '德宏傣族景颇族自治州', '瑞丽市', '922', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6337', '533103', '533100', '云南省', '德宏傣族景颇族自治州', '芒市', '921', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6338', '533122', '533100', '云南省', '德宏傣族景颇族自治州', '梁河县', '920', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6339', '533123', '533100', '云南省', '德宏傣族景颇族自治州', '盈江县', '919', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6340', '533124', '533100', '云南省', '德宏傣族景颇族自治州', '陇川县', '918', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6341', '533300', '530000', '云南省', '怒江傈僳族自治州', '', '917', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6342', '533301', '533300', '云南省', '怒江傈僳族自治州', '泸水市', '916', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6343', '533323', '533300', '云南省', '怒江傈僳族自治州', '福贡县', '915', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6344', '533324', '533300', '云南省', '怒江傈僳族自治州', '贡山独龙族怒族自治县', '914', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6345', '533325', '533300', '云南省', '怒江傈僳族自治州', '兰坪白族普米族自治县', '913', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6346', '533400', '530000', '云南省', '迪庆藏族自治州', '', '912', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6347', '533401', '533400', '云南省', '迪庆藏族自治州', '香格里拉市', '911', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6348', '533422', '533400', '云南省', '迪庆藏族自治州', '德钦县', '910', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6349', '533423', '533400', '云南省', '迪庆藏族自治州', '维西傈僳族自治县', '909', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6350', '540000', '-1', '西藏自治区', '', '', '908', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6351', '540100', '540000', '西藏自治区', '拉萨市', '', '907', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6352', '540102', '540100', '西藏自治区', '拉萨市', '城关区', '906', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6353', '540103', '540100', '西藏自治区', '拉萨市', '堆龙德庆区', '905', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6354', '540121', '540100', '西藏自治区', '拉萨市', '林周县', '904', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6355', '540122', '540100', '西藏自治区', '拉萨市', '当雄县', '903', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6356', '540123', '540100', '西藏自治区', '拉萨市', '尼木县', '902', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6357', '540124', '540100', '西藏自治区', '拉萨市', '曲水县', '901', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6358', '540126', '540100', '西藏自治区', '拉萨市', '达孜县', '900', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6359', '540127', '540100', '西藏自治区', '拉萨市', '墨竹工卡县', '899', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6360', '540200', '540000', '西藏自治区', '日喀则市', '', '898', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6361', '540202', '540200', '西藏自治区', '日喀则市', '桑珠孜区', '897', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6362', '540221', '540200', '西藏自治区', '日喀则市', '南木林县', '896', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6363', '540222', '540200', '西藏自治区', '日喀则市', '江孜县', '895', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6364', '540223', '540200', '西藏自治区', '日喀则市', '定日县', '894', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6365', '540224', '540200', '西藏自治区', '日喀则市', '萨迦县', '893', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6366', '540225', '540200', '西藏自治区', '日喀则市', '拉孜县', '892', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6367', '540226', '540200', '西藏自治区', '日喀则市', '昂仁县', '891', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6368', '540227', '540200', '西藏自治区', '日喀则市', '谢通门县', '890', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6369', '540228', '540200', '西藏自治区', '日喀则市', '白朗县', '889', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6370', '540229', '540200', '西藏自治区', '日喀则市', '仁布县', '888', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6371', '540230', '540200', '西藏自治区', '日喀则市', '康马县', '887', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6372', '540231', '540200', '西藏自治区', '日喀则市', '定结县', '886', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6373', '540232', '540200', '西藏自治区', '日喀则市', '仲巴县', '885', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6374', '540233', '540200', '西藏自治区', '日喀则市', '亚东县', '884', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6375', '540234', '540200', '西藏自治区', '日喀则市', '吉隆县', '883', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6376', '540235', '540200', '西藏自治区', '日喀则市', '聂拉木县', '882', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6377', '540236', '540200', '西藏自治区', '日喀则市', '萨嘎县', '881', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6378', '540237', '540200', '西藏自治区', '日喀则市', '岗巴县', '880', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6379', '540300', '540000', '西藏自治区', '昌都市', '', '879', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6380', '540302', '540300', '西藏自治区', '昌都市', '卡若区', '878', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6381', '540321', '540300', '西藏自治区', '昌都市', '江达县', '877', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6382', '540322', '540300', '西藏自治区', '昌都市', '贡觉县', '876', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6383', '540323', '540300', '西藏自治区', '昌都市', '类乌齐县', '875', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6384', '540324', '540300', '西藏自治区', '昌都市', '丁青县', '874', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6385', '540325', '540300', '西藏自治区', '昌都市', '察雅县', '873', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6386', '540326', '540300', '西藏自治区', '昌都市', '八宿县', '872', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6387', '540327', '540300', '西藏自治区', '昌都市', '左贡县', '871', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6388', '540328', '540300', '西藏自治区', '昌都市', '芒康县', '870', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6389', '540329', '540300', '西藏自治区', '昌都市', '洛隆县', '869', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6390', '540330', '540300', '西藏自治区', '昌都市', '边坝县', '868', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6391', '540400', '540000', '西藏自治区', '林芝市', '', '867', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6392', '540402', '540400', '西藏自治区', '林芝市', '巴宜区', '866', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6393', '540421', '540400', '西藏自治区', '林芝市', '工布江达县', '865', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6394', '540422', '540400', '西藏自治区', '林芝市', '米林县', '864', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6395', '540423', '540400', '西藏自治区', '林芝市', '墨脱县', '863', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6396', '540424', '540400', '西藏自治区', '林芝市', '波密县', '862', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6397', '540425', '540400', '西藏自治区', '林芝市', '察隅县', '861', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6398', '540426', '540400', '西藏自治区', '林芝市', '朗县', '860', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6399', '540500', '540000', '西藏自治区', '山南市', '', '859', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6400', '540502', '540500', '西藏自治区', '山南市', '乃东区', '858', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6401', '540521', '540500', '西藏自治区', '山南市', '扎囊县', '857', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6402', '540522', '540500', '西藏自治区', '山南市', '贡嘎县', '856', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6403', '540523', '540500', '西藏自治区', '山南市', '桑日县', '855', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6404', '540524', '540500', '西藏自治区', '山南市', '琼结县', '854', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6405', '540525', '540500', '西藏自治区', '山南市', '曲松县', '853', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6406', '540526', '540500', '西藏自治区', '山南市', '措美县', '852', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6407', '540527', '540500', '西藏自治区', '山南市', '洛扎县', '851', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6408', '540528', '540500', '西藏自治区', '山南市', '加查县', '850', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6409', '540529', '540500', '西藏自治区', '山南市', '隆子县', '849', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6410', '540530', '540500', '西藏自治区', '山南市', '错那县', '848', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6411', '540531', '540500', '西藏自治区', '山南市', '浪卡子县', '847', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6412', '542400', '540000', '西藏自治区', '那曲地区', '', '846', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6413', '542421', '542400', '西藏自治区', '那曲地区', '那曲县', '845', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6414', '542422', '542400', '西藏自治区', '那曲地区', '嘉黎县', '844', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6415', '542423', '542400', '西藏自治区', '那曲地区', '比如县', '843', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6416', '542424', '542400', '西藏自治区', '那曲地区', '聂荣县', '842', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6417', '542425', '542400', '西藏自治区', '那曲地区', '安多县', '841', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6418', '542426', '542400', '西藏自治区', '那曲地区', '申扎县', '840', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6419', '542427', '542400', '西藏自治区', '那曲地区', '索县', '839', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6420', '542428', '542400', '西藏自治区', '那曲地区', '班戈县', '838', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6421', '542429', '542400', '西藏自治区', '那曲地区', '巴青县', '837', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6422', '542430', '542400', '西藏自治区', '那曲地区', '尼玛县', '836', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6423', '542431', '542400', '西藏自治区', '那曲地区', '双湖县', '835', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6424', '542500', '540000', '西藏自治区', '阿里地区', '', '834', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6425', '542521', '542500', '西藏自治区', '阿里地区', '普兰县', '833', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6426', '542522', '542500', '西藏自治区', '阿里地区', '札达县', '832', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6427', '542523', '542500', '西藏自治区', '阿里地区', '噶尔县', '831', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6428', '542524', '542500', '西藏自治区', '阿里地区', '日土县', '830', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6429', '542525', '542500', '西藏自治区', '阿里地区', '革吉县', '829', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6430', '542526', '542500', '西藏自治区', '阿里地区', '改则县', '828', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6431', '542527', '542500', '西藏自治区', '阿里地区', '措勤县', '827', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6432', '610000', '-1', '陕西省', '', '', '826', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6433', '610100', '610000', '陕西省', '西安市', '', '825', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6434', '610102', '610100', '陕西省', '西安市', '新城区', '824', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6435', '610103', '610100', '陕西省', '西安市', '碑林区', '823', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6436', '610104', '610100', '陕西省', '西安市', '莲湖区', '822', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6437', '610111', '610100', '陕西省', '西安市', '灞桥区', '821', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6438', '610112', '610100', '陕西省', '西安市', '未央区', '820', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6439', '610113', '610100', '陕西省', '西安市', '雁塔区', '819', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6440', '610114', '610100', '陕西省', '西安市', '阎良区', '818', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6441', '610115', '610100', '陕西省', '西安市', '临潼区', '817', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6442', '610116', '610100', '陕西省', '西安市', '长安区', '816', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6443', '610117', '610100', '陕西省', '西安市', '高陵区', '815', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6444', '610118', '610100', '陕西省', '西安市', '鄠邑区', '814', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6445', '610122', '610100', '陕西省', '西安市', '蓝田县', '813', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6446', '610124', '610100', '陕西省', '西安市', '周至县', '812', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6447', '610200', '610000', '陕西省', '铜川市', '', '811', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6448', '610202', '610200', '陕西省', '铜川市', '王益区', '810', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6449', '610203', '610200', '陕西省', '铜川市', '印台区', '809', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6450', '610204', '610200', '陕西省', '铜川市', '耀州区', '808', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6451', '610222', '610200', '陕西省', '铜川市', '宜君县', '807', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6452', '610300', '610000', '陕西省', '宝鸡市', '', '806', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6453', '610302', '610300', '陕西省', '宝鸡市', '渭滨区', '805', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6454', '610303', '610300', '陕西省', '宝鸡市', '金台区', '804', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6455', '610304', '610300', '陕西省', '宝鸡市', '陈仓区', '803', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6456', '610322', '610300', '陕西省', '宝鸡市', '凤翔县', '802', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6457', '610323', '610300', '陕西省', '宝鸡市', '岐山县', '801', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6458', '610324', '610300', '陕西省', '宝鸡市', '扶风县', '800', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6459', '610326', '610300', '陕西省', '宝鸡市', '眉县', '799', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6460', '610327', '610300', '陕西省', '宝鸡市', '陇县', '798', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6461', '610328', '610300', '陕西省', '宝鸡市', '千阳县', '797', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6462', '610329', '610300', '陕西省', '宝鸡市', '麟游县', '796', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6463', '610330', '610300', '陕西省', '宝鸡市', '凤县', '795', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6464', '610331', '610300', '陕西省', '宝鸡市', '太白县', '794', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6465', '610400', '610000', '陕西省', '咸阳市', '', '793', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6466', '610402', '610400', '陕西省', '咸阳市', '秦都区', '792', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6467', '610403', '610400', '陕西省', '咸阳市', '杨陵区', '791', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6468', '610404', '610400', '陕西省', '咸阳市', '渭城区', '790', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6469', '610422', '610400', '陕西省', '咸阳市', '三原县', '789', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6470', '610423', '610400', '陕西省', '咸阳市', '泾阳县', '788', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6471', '610424', '610400', '陕西省', '咸阳市', '乾县', '787', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6472', '610425', '610400', '陕西省', '咸阳市', '礼泉县', '786', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6473', '610426', '610400', '陕西省', '咸阳市', '永寿县', '785', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6474', '610427', '610400', '陕西省', '咸阳市', '彬县', '784', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6475', '610428', '610400', '陕西省', '咸阳市', '长武县', '783', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6476', '610429', '610400', '陕西省', '咸阳市', '旬邑县', '782', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6477', '610430', '610400', '陕西省', '咸阳市', '淳化县', '781', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6478', '610431', '610400', '陕西省', '咸阳市', '武功县', '780', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6479', '610481', '610400', '陕西省', '咸阳市', '兴平市', '779', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6480', '610500', '610000', '陕西省', '渭南市', '', '778', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6481', '610502', '610500', '陕西省', '渭南市', '临渭区', '777', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6482', '610503', '610500', '陕西省', '渭南市', '华州区', '776', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6483', '610522', '610500', '陕西省', '渭南市', '潼关县', '775', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6484', '610523', '610500', '陕西省', '渭南市', '大荔县', '774', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6485', '610524', '610500', '陕西省', '渭南市', '合阳县', '773', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6486', '610525', '610500', '陕西省', '渭南市', '澄城县', '772', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6487', '610526', '610500', '陕西省', '渭南市', '蒲城县', '771', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6488', '610527', '610500', '陕西省', '渭南市', '白水县', '770', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6489', '610528', '610500', '陕西省', '渭南市', '富平县', '769', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6490', '610581', '610500', '陕西省', '渭南市', '韩城市', '768', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6491', '610582', '610500', '陕西省', '渭南市', '华阴市', '767', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6492', '610600', '610000', '陕西省', '延安市', '', '766', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6493', '610602', '610600', '陕西省', '延安市', '宝塔区', '765', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6494', '610603', '610600', '陕西省', '延安市', '安塞区', '764', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6495', '610621', '610600', '陕西省', '延安市', '延长县', '763', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6496', '610622', '610600', '陕西省', '延安市', '延川县', '762', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6497', '610623', '610600', '陕西省', '延安市', '子长县', '761', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6498', '610625', '610600', '陕西省', '延安市', '志丹县', '760', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6499', '610626', '610600', '陕西省', '延安市', '吴起县', '759', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6500', '610627', '610600', '陕西省', '延安市', '甘泉县', '758', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6501', '610628', '610600', '陕西省', '延安市', '富县', '757', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6502', '610629', '610600', '陕西省', '延安市', '洛川县', '756', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6503', '610630', '610600', '陕西省', '延安市', '宜川县', '755', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6504', '610631', '610600', '陕西省', '延安市', '黄龙县', '754', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6505', '610632', '610600', '陕西省', '延安市', '黄陵县', '753', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6506', '610700', '610000', '陕西省', '汉中市', '', '752', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6507', '610702', '610700', '陕西省', '汉中市', '汉台区', '751', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6508', '610721', '610700', '陕西省', '汉中市', '南郑县', '750', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6509', '610722', '610700', '陕西省', '汉中市', '城固县', '749', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6510', '610723', '610700', '陕西省', '汉中市', '洋县', '748', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6511', '610724', '610700', '陕西省', '汉中市', '西乡县', '747', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6512', '610725', '610700', '陕西省', '汉中市', '勉县', '746', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6513', '610726', '610700', '陕西省', '汉中市', '宁强县', '745', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6514', '610727', '610700', '陕西省', '汉中市', '略阳县', '744', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6515', '610728', '610700', '陕西省', '汉中市', '镇巴县', '743', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6516', '610729', '610700', '陕西省', '汉中市', '留坝县', '742', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6517', '610730', '610700', '陕西省', '汉中市', '佛坪县', '741', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6518', '610800', '610000', '陕西省', '榆林市', '', '740', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6519', '610802', '610800', '陕西省', '榆林市', '榆阳区', '739', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6520', '610803', '610800', '陕西省', '榆林市', '横山区', '738', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6521', '610822', '610800', '陕西省', '榆林市', '府谷县', '737', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6522', '610824', '610800', '陕西省', '榆林市', '靖边县', '736', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6523', '610825', '610800', '陕西省', '榆林市', '定边县', '735', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6524', '610826', '610800', '陕西省', '榆林市', '绥德县', '734', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6525', '610827', '610800', '陕西省', '榆林市', '米脂县', '733', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6526', '610828', '610800', '陕西省', '榆林市', '佳县', '732', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6527', '610829', '610800', '陕西省', '榆林市', '吴堡县', '731', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6528', '610830', '610800', '陕西省', '榆林市', '清涧县', '730', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6529', '610831', '610800', '陕西省', '榆林市', '子洲县', '729', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6530', '610881', '610800', '陕西省', '榆林市', '神木市', '728', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6531', '610900', '610000', '陕西省', '安康市', '', '727', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6532', '610902', '610900', '陕西省', '安康市', '汉滨区', '726', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6533', '610921', '610900', '陕西省', '安康市', '汉阴县', '725', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6534', '610922', '610900', '陕西省', '安康市', '石泉县', '724', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6535', '610923', '610900', '陕西省', '安康市', '宁陕县', '723', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6536', '610924', '610900', '陕西省', '安康市', '紫阳县', '722', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6537', '610925', '610900', '陕西省', '安康市', '岚皋县', '721', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6538', '610926', '610900', '陕西省', '安康市', '平利县', '720', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6539', '610927', '610900', '陕西省', '安康市', '镇坪县', '719', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6540', '610928', '610900', '陕西省', '安康市', '旬阳县', '718', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6541', '610929', '610900', '陕西省', '安康市', '白河县', '717', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6542', '611000', '610000', '陕西省', '商洛市', '', '716', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6543', '611002', '611000', '陕西省', '商洛市', '商州区', '715', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6544', '611021', '611000', '陕西省', '商洛市', '洛南县', '714', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6545', '611022', '611000', '陕西省', '商洛市', '丹凤县', '713', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6546', '611023', '611000', '陕西省', '商洛市', '商南县', '712', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6547', '611024', '611000', '陕西省', '商洛市', '山阳县', '711', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6548', '611025', '611000', '陕西省', '商洛市', '镇安县', '710', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6549', '611026', '611000', '陕西省', '商洛市', '柞水县', '709', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6550', '620000', '-1', '甘肃省', '', '', '708', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6551', '620100', '620000', '甘肃省', '兰州市', '', '707', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6552', '620102', '620100', '甘肃省', '兰州市', '城关区', '706', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6553', '620103', '620100', '甘肃省', '兰州市', '七里河区', '705', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6554', '620104', '620100', '甘肃省', '兰州市', '西固区', '704', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6555', '620105', '620100', '甘肃省', '兰州市', '安宁区', '703', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6556', '620111', '620100', '甘肃省', '兰州市', '红古区', '702', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6557', '620121', '620100', '甘肃省', '兰州市', '永登县', '701', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6558', '620122', '620100', '甘肃省', '兰州市', '皋兰县', '700', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6559', '620123', '620100', '甘肃省', '兰州市', '榆中县', '699', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6560', '620200', '620000', '甘肃省', '嘉峪关市', '', '698', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6561', '620201', '620200', '甘肃省', '嘉峪关市', '嘉峪关市', '697', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6562', '620300', '620000', '甘肃省', '金昌市', '', '696', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6563', '620302', '620300', '甘肃省', '金昌市', '金川区', '695', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6564', '620321', '620300', '甘肃省', '金昌市', '永昌县', '694', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6565', '620400', '620000', '甘肃省', '白银市', '', '693', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6566', '620402', '620400', '甘肃省', '白银市', '白银区', '692', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6567', '620403', '620400', '甘肃省', '白银市', '平川区', '691', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6568', '620421', '620400', '甘肃省', '白银市', '靖远县', '690', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6569', '620422', '620400', '甘肃省', '白银市', '会宁县', '689', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6570', '620423', '620400', '甘肃省', '白银市', '景泰县', '688', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6571', '620500', '620000', '甘肃省', '天水市', '', '687', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6572', '620502', '620500', '甘肃省', '天水市', '秦州区', '686', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6573', '620503', '620500', '甘肃省', '天水市', '麦积区', '685', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6574', '620521', '620500', '甘肃省', '天水市', '清水县', '684', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6575', '620522', '620500', '甘肃省', '天水市', '秦安县', '683', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6576', '620523', '620500', '甘肃省', '天水市', '甘谷县', '682', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6577', '620524', '620500', '甘肃省', '天水市', '武山县', '681', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6578', '620525', '620500', '甘肃省', '天水市', '张家川回族自治县', '680', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6579', '620600', '620000', '甘肃省', '武威市', '', '679', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6580', '620602', '620600', '甘肃省', '武威市', '凉州区', '678', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6581', '620621', '620600', '甘肃省', '武威市', '民勤县', '677', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6582', '620622', '620600', '甘肃省', '武威市', '古浪县', '676', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6583', '620623', '620600', '甘肃省', '武威市', '天祝藏族自治县', '675', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6584', '620700', '620000', '甘肃省', '张掖市', '', '674', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6585', '620702', '620700', '甘肃省', '张掖市', '甘州区', '673', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6586', '620721', '620700', '甘肃省', '张掖市', '肃南裕固族自治县', '672', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6587', '620722', '620700', '甘肃省', '张掖市', '民乐县', '671', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6588', '620723', '620700', '甘肃省', '张掖市', '临泽县', '670', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6589', '620724', '620700', '甘肃省', '张掖市', '高台县', '669', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6590', '620725', '620700', '甘肃省', '张掖市', '山丹县', '668', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6591', '620800', '620000', '甘肃省', '平凉市', '', '667', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6592', '620802', '620800', '甘肃省', '平凉市', '崆峒区', '666', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6593', '620821', '620800', '甘肃省', '平凉市', '泾川县', '665', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6594', '620822', '620800', '甘肃省', '平凉市', '灵台县', '664', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6595', '620823', '620800', '甘肃省', '平凉市', '崇信县', '663', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6596', '620824', '620800', '甘肃省', '平凉市', '华亭县', '662', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6597', '620825', '620800', '甘肃省', '平凉市', '庄浪县', '661', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6598', '620826', '620800', '甘肃省', '平凉市', '静宁县', '660', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6599', '620900', '620000', '甘肃省', '酒泉市', '', '659', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6600', '620902', '620900', '甘肃省', '酒泉市', '肃州区', '658', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6601', '620921', '620900', '甘肃省', '酒泉市', '金塔县', '657', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6602', '620922', '620900', '甘肃省', '酒泉市', '瓜州县', '656', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6603', '620923', '620900', '甘肃省', '酒泉市', '肃北蒙古族自治县', '655', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6604', '620924', '620900', '甘肃省', '酒泉市', '阿克塞哈萨克族自治县', '654', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6605', '620981', '620900', '甘肃省', '酒泉市', '玉门市', '653', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6606', '620982', '620900', '甘肃省', '酒泉市', '敦煌市', '652', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6607', '621000', '620000', '甘肃省', '庆阳市', '', '651', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6608', '621002', '621000', '甘肃省', '庆阳市', '西峰区', '650', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6609', '621021', '621000', '甘肃省', '庆阳市', '庆城县', '649', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6610', '621022', '621000', '甘肃省', '庆阳市', '环县', '648', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6611', '621023', '621000', '甘肃省', '庆阳市', '华池县', '647', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6612', '621024', '621000', '甘肃省', '庆阳市', '合水县', '646', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6613', '621025', '621000', '甘肃省', '庆阳市', '正宁县', '645', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6614', '621026', '621000', '甘肃省', '庆阳市', '宁县', '644', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6615', '621027', '621000', '甘肃省', '庆阳市', '镇原县', '643', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6616', '621100', '620000', '甘肃省', '定西市', '', '642', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6617', '621102', '621100', '甘肃省', '定西市', '安定区', '641', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6618', '621121', '621100', '甘肃省', '定西市', '通渭县', '640', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6619', '621122', '621100', '甘肃省', '定西市', '陇西县', '639', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6620', '621123', '621100', '甘肃省', '定西市', '渭源县', '638', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6621', '621124', '621100', '甘肃省', '定西市', '临洮县', '637', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6622', '621125', '621100', '甘肃省', '定西市', '漳县', '636', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6623', '621126', '621100', '甘肃省', '定西市', '岷县', '635', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6624', '621200', '620000', '甘肃省', '陇南市', '', '634', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6625', '621202', '621200', '甘肃省', '陇南市', '武都区', '633', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6626', '621221', '621200', '甘肃省', '陇南市', '成县', '632', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6627', '621222', '621200', '甘肃省', '陇南市', '文县', '631', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6628', '621223', '621200', '甘肃省', '陇南市', '宕昌县', '630', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6629', '621224', '621200', '甘肃省', '陇南市', '康县', '629', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6630', '621225', '621200', '甘肃省', '陇南市', '西和县', '628', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6631', '621226', '621200', '甘肃省', '陇南市', '礼县', '627', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6632', '621227', '621200', '甘肃省', '陇南市', '徽县', '626', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6633', '621228', '621200', '甘肃省', '陇南市', '两当县', '625', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6634', '622900', '620000', '甘肃省', '临夏回族自治州', '', '624', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6635', '622901', '622900', '甘肃省', '临夏回族自治州', '临夏市', '623', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6636', '622921', '622900', '甘肃省', '临夏回族自治州', '临夏县', '622', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6637', '622922', '622900', '甘肃省', '临夏回族自治州', '康乐县', '621', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6638', '622923', '622900', '甘肃省', '临夏回族自治州', '永靖县', '620', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6639', '622924', '622900', '甘肃省', '临夏回族自治州', '广河县', '619', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6640', '622925', '622900', '甘肃省', '临夏回族自治州', '和政县', '618', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6641', '622926', '622900', '甘肃省', '临夏回族自治州', '东乡族自治县', '617', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6642', '622927', '622900', '甘肃省', '临夏回族自治州', '积石山保安族东乡族撒拉族自治县', '616', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6643', '623000', '620000', '甘肃省', '甘南藏族自治州', '', '615', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6644', '623001', '623000', '甘肃省', '甘南藏族自治州', '合作市', '614', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6645', '623021', '623000', '甘肃省', '甘南藏族自治州', '临潭县', '613', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6646', '623022', '623000', '甘肃省', '甘南藏族自治州', '卓尼县', '612', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6647', '623023', '623000', '甘肃省', '甘南藏族自治州', '舟曲县', '611', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6648', '623024', '623000', '甘肃省', '甘南藏族自治州', '迭部县', '610', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6649', '623025', '623000', '甘肃省', '甘南藏族自治州', '玛曲县', '609', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6650', '623026', '623000', '甘肃省', '甘南藏族自治州', '碌曲县', '608', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6651', '623027', '623000', '甘肃省', '甘南藏族自治州', '夏河县', '607', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6652', '630000', '-1', '青海省', '', '', '606', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6653', '630100', '630000', '青海省', '西宁市', '', '605', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6654', '630102', '630100', '青海省', '西宁市', '城东区', '604', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6655', '630103', '630100', '青海省', '西宁市', '城中区', '603', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6656', '630104', '630100', '青海省', '西宁市', '城西区', '602', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6657', '630105', '630100', '青海省', '西宁市', '城北区', '601', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6658', '630121', '630100', '青海省', '西宁市', '大通回族土族自治县', '600', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6659', '630122', '630100', '青海省', '西宁市', '湟中县', '599', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6660', '630123', '630100', '青海省', '西宁市', '湟源县', '598', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6661', '630200', '630000', '青海省', '海东市', '', '597', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6662', '630202', '630200', '青海省', '海东市', '乐都区', '596', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6663', '630203', '630200', '青海省', '海东市', '平安区', '595', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6664', '630222', '630200', '青海省', '海东市', '民和回族土族自治县', '594', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6665', '630223', '630200', '青海省', '海东市', '互助土族自治县', '593', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6666', '630224', '630200', '青海省', '海东市', '化隆回族自治县', '592', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6667', '630225', '630200', '青海省', '海东市', '循化撒拉族自治县', '591', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6668', '632200', '630000', '青海省', '海北藏族自治州', '', '590', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6669', '632221', '632200', '青海省', '海北藏族自治州', '门源回族自治县', '589', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6670', '632222', '632200', '青海省', '海北藏族自治州', '祁连县', '588', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6671', '632223', '632200', '青海省', '海北藏族自治州', '海晏县', '587', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6672', '632224', '632200', '青海省', '海北藏族自治州', '刚察县', '586', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6673', '632300', '630000', '青海省', '黄南藏族自治州', '', '585', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6674', '632321', '632300', '青海省', '黄南藏族自治州', '同仁县', '584', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6675', '632322', '632300', '青海省', '黄南藏族自治州', '尖扎县', '583', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6676', '632323', '632300', '青海省', '黄南藏族自治州', '泽库县', '582', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6677', '632324', '632300', '青海省', '黄南藏族自治州', '河南蒙古族自治县', '581', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6678', '632500', '630000', '青海省', '海南藏族自治州', '', '580', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6679', '632521', '632500', '青海省', '海南藏族自治州', '共和县', '579', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6680', '632522', '632500', '青海省', '海南藏族自治州', '同德县', '578', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6681', '632523', '632500', '青海省', '海南藏族自治州', '贵德县', '577', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6682', '632524', '632500', '青海省', '海南藏族自治州', '兴海县', '576', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6683', '632525', '632500', '青海省', '海南藏族自治州', '贵南县', '575', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6684', '632600', '630000', '青海省', '果洛藏族自治州', '', '574', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6685', '632621', '632600', '青海省', '果洛藏族自治州', '玛沁县', '573', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6686', '632622', '632600', '青海省', '果洛藏族自治州', '班玛县', '572', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6687', '632623', '632600', '青海省', '果洛藏族自治州', '甘德县', '571', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6688', '632624', '632600', '青海省', '果洛藏族自治州', '达日县', '570', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6689', '632625', '632600', '青海省', '果洛藏族自治州', '久治县', '569', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6690', '632626', '632600', '青海省', '果洛藏族自治州', '玛多县', '568', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6691', '632700', '630000', '青海省', '玉树藏族自治州', '', '567', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6692', '632701', '632700', '青海省', '玉树藏族自治州', '玉树市', '566', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6693', '632722', '632700', '青海省', '玉树藏族自治州', '杂多县', '565', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6694', '632723', '632700', '青海省', '玉树藏族自治州', '称多县', '564', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6695', '632724', '632700', '青海省', '玉树藏族自治州', '治多县', '563', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6696', '632725', '632700', '青海省', '玉树藏族自治州', '囊谦县', '562', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6697', '632726', '632700', '青海省', '玉树藏族自治州', '曲麻莱县', '561', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6698', '632800', '630000', '青海省', '海西蒙古族藏族自治州', '', '560', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6699', '632801', '632800', '青海省', '海西蒙古族藏族自治州', '格尔木市', '559', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6700', '632802', '632800', '青海省', '海西蒙古族藏族自治州', '德令哈市', '558', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6701', '632821', '632800', '青海省', '海西蒙古族藏族自治州', '乌兰县', '557', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6702', '632822', '632800', '青海省', '海西蒙古族藏族自治州', '都兰县', '556', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6703', '632823', '632800', '青海省', '海西蒙古族藏族自治州', '天峻县', '555', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6704', '632824', '632800', '青海省', '海西蒙古族藏族自治州', '冷湖行政区', '554', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6705', '632825', '632800', '青海省', '海西蒙古族藏族自治州', '大柴旦行政区', '553', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6706', '632826', '632800', '青海省', '海西蒙古族藏族自治州', '茫崖行政区', '552', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6707', '640000', '-1', '宁夏回族自治区', '', '', '551', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6708', '640100', '640000', '宁夏回族自治区', '银川市', '', '550', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6709', '640104', '640100', '宁夏回族自治区', '银川市', '兴庆区', '549', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6710', '640105', '640100', '宁夏回族自治区', '银川市', '西夏区', '548', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6711', '640106', '640100', '宁夏回族自治区', '银川市', '金凤区', '547', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6712', '640121', '640100', '宁夏回族自治区', '银川市', '永宁县', '546', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6713', '640122', '640100', '宁夏回族自治区', '银川市', '贺兰县', '545', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6714', '640181', '640100', '宁夏回族自治区', '银川市', '灵武市', '544', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6715', '640200', '640000', '宁夏回族自治区', '石嘴山市', '', '543', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6716', '640202', '640200', '宁夏回族自治区', '石嘴山市', '大武口区', '542', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6717', '640205', '640200', '宁夏回族自治区', '石嘴山市', '惠农区', '541', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6718', '640221', '640200', '宁夏回族自治区', '石嘴山市', '平罗县', '540', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6719', '640300', '640000', '宁夏回族自治区', '吴忠市', '', '539', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6720', '640302', '640300', '宁夏回族自治区', '吴忠市', '利通区', '538', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6721', '640303', '640300', '宁夏回族自治区', '吴忠市', '红寺堡区', '537', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6722', '640323', '640300', '宁夏回族自治区', '吴忠市', '盐池县', '536', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6723', '640324', '640300', '宁夏回族自治区', '吴忠市', '同心县', '535', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6724', '640381', '640300', '宁夏回族自治区', '吴忠市', '青铜峡市', '534', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6725', '640400', '640000', '宁夏回族自治区', '固原市', '', '533', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6726', '640402', '640400', '宁夏回族自治区', '固原市', '原州区', '532', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6727', '640422', '640400', '宁夏回族自治区', '固原市', '西吉县', '531', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6728', '640423', '640400', '宁夏回族自治区', '固原市', '隆德县', '530', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6729', '640424', '640400', '宁夏回族自治区', '固原市', '泾源县', '529', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6730', '640425', '640400', '宁夏回族自治区', '固原市', '彭阳县', '528', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6731', '640500', '640000', '宁夏回族自治区', '中卫市', '', '527', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6732', '640502', '640500', '宁夏回族自治区', '中卫市', '沙坡头区', '526', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6733', '640521', '640500', '宁夏回族自治区', '中卫市', '中宁县', '525', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6734', '640522', '640500', '宁夏回族自治区', '中卫市', '海原县', '524', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6735', '650000', '-1', '新疆维吾尔自治区', '', '', '523', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6736', '650100', '650000', '新疆维吾尔自治区', '乌鲁木齐市', '', '522', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6737', '650102', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '天山区', '521', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6738', '650103', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '沙依巴克区', '520', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6739', '650104', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '新市区', '519', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6740', '650105', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '水磨沟区', '518', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6741', '650106', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '头屯河区', '517', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6742', '650107', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '达坂城区', '516', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6743', '650109', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '米东区', '515', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6744', '650121', '650100', '新疆维吾尔自治区', '乌鲁木齐市', '乌鲁木齐县', '514', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6745', '650200', '650000', '新疆维吾尔自治区', '克拉玛依市', '', '513', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6746', '650202', '650200', '新疆维吾尔自治区', '克拉玛依市', '独山子区', '512', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6747', '650203', '650200', '新疆维吾尔自治区', '克拉玛依市', '克拉玛依区', '511', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6748', '650204', '650200', '新疆维吾尔自治区', '克拉玛依市', '白碱滩区', '510', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6749', '650205', '650200', '新疆维吾尔自治区', '克拉玛依市', '乌尔禾区', '509', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6750', '650400', '650000', '新疆维吾尔自治区', '吐鲁番市', '', '508', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6751', '650402', '650400', '新疆维吾尔自治区', '吐鲁番市', '高昌区', '507', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6752', '650421', '650400', '新疆维吾尔自治区', '吐鲁番市', '鄯善县', '506', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6753', '650422', '650400', '新疆维吾尔自治区', '吐鲁番市', '托克逊县', '505', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6754', '650500', '650000', '新疆维吾尔自治区', '哈密市', '', '504', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6755', '650502', '650500', '新疆维吾尔自治区', '哈密市', '伊州区', '503', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6756', '650521', '650500', '新疆维吾尔自治区', '哈密市', '巴里坤哈萨克自治县', '502', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6757', '650522', '650500', '新疆维吾尔自治区', '哈密市', '伊吾县', '501', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6758', '652300', '650000', '新疆维吾尔自治区', '昌吉回族自治州', '', '500', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6759', '652301', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '昌吉市', '499', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6760', '652302', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '阜康市', '498', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6761', '652323', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '呼图壁县', '497', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6762', '652324', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '玛纳斯县', '496', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6763', '652325', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '奇台县', '495', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6764', '652327', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '吉木萨尔县', '494', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6765', '652328', '652300', '新疆维吾尔自治区', '昌吉回族自治州', '木垒哈萨克自治县', '493', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6766', '652700', '650000', '新疆维吾尔自治区', '博尔塔拉蒙古自治州', '', '492', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6767', '652701', '652700', '新疆维吾尔自治区', '博尔塔拉蒙古自治州', '博乐市', '491', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6768', '652702', '652700', '新疆维吾尔自治区', '博尔塔拉蒙古自治州', '阿拉山口市', '490', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6769', '652722', '652700', '新疆维吾尔自治区', '博尔塔拉蒙古自治州', '精河县', '489', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6770', '652723', '652700', '新疆维吾尔自治区', '博尔塔拉蒙古自治州', '温泉县', '488', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6771', '652800', '650000', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '', '487', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6772', '652801', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '库尔勒市', '486', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6773', '652822', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '轮台县', '485', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6774', '652823', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '尉犁县', '484', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6775', '652824', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '若羌县', '483', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6776', '652825', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '且末县', '482', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6777', '652826', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '焉耆回族自治县', '481', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6778', '652827', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '和静县', '480', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6779', '652828', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '和硕县', '479', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6780', '652829', '652800', '新疆维吾尔自治区', '巴音郭楞蒙古自治州', '博湖县', '478', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6781', '652900', '650000', '新疆维吾尔自治区', '阿克苏地区', '', '477', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6782', '652901', '652900', '新疆维吾尔自治区', '阿克苏地区', '阿克苏市', '476', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6783', '652922', '652900', '新疆维吾尔自治区', '阿克苏地区', '温宿县', '475', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6784', '652923', '652900', '新疆维吾尔自治区', '阿克苏地区', '库车县', '474', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6785', '652924', '652900', '新疆维吾尔自治区', '阿克苏地区', '沙雅县', '473', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6786', '652925', '652900', '新疆维吾尔自治区', '阿克苏地区', '新和县', '472', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6787', '652926', '652900', '新疆维吾尔自治区', '阿克苏地区', '拜城县', '471', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6788', '652927', '652900', '新疆维吾尔自治区', '阿克苏地区', '乌什县', '470', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6789', '652928', '652900', '新疆维吾尔自治区', '阿克苏地区', '阿瓦提县', '469', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6790', '652929', '652900', '新疆维吾尔自治区', '阿克苏地区', '柯坪县', '468', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6791', '653000', '650000', '新疆维吾尔自治区', '克孜勒苏柯尔克孜自治州', '', '467', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6792', '653001', '653000', '新疆维吾尔自治区', '克孜勒苏柯尔克孜自治州', '阿图什市', '466', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6793', '653022', '653000', '新疆维吾尔自治区', '克孜勒苏柯尔克孜自治州', '阿克陶县', '465', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6794', '653023', '653000', '新疆维吾尔自治区', '克孜勒苏柯尔克孜自治州', '阿合奇县', '464', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6795', '653024', '653000', '新疆维吾尔自治区', '克孜勒苏柯尔克孜自治州', '乌恰县', '463', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6796', '653100', '650000', '新疆维吾尔自治区', '喀什地区', '', '462', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6797', '653101', '653100', '新疆维吾尔自治区', '喀什地区', '喀什市', '461', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6798', '653121', '653100', '新疆维吾尔自治区', '喀什地区', '疏附县', '460', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6799', '653122', '653100', '新疆维吾尔自治区', '喀什地区', '疏勒县', '459', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6800', '653123', '653100', '新疆维吾尔自治区', '喀什地区', '英吉沙县', '458', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6801', '653124', '653100', '新疆维吾尔自治区', '喀什地区', '泽普县', '457', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6802', '653125', '653100', '新疆维吾尔自治区', '喀什地区', '莎车县', '456', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6803', '653126', '653100', '新疆维吾尔自治区', '喀什地区', '叶城县', '455', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6804', '653127', '653100', '新疆维吾尔自治区', '喀什地区', '麦盖提县', '454', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6805', '653128', '653100', '新疆维吾尔自治区', '喀什地区', '岳普湖县', '453', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6806', '653129', '653100', '新疆维吾尔自治区', '喀什地区', '伽师县', '452', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6807', '653130', '653100', '新疆维吾尔自治区', '喀什地区', '巴楚县', '451', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6808', '653131', '653100', '新疆维吾尔自治区', '喀什地区', '塔什库尔干塔吉克自治县', '450', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6809', '653200', '650000', '新疆维吾尔自治区', '和田地区', '', '449', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6810', '653201', '653200', '新疆维吾尔自治区', '和田地区', '和田市', '448', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6811', '653221', '653200', '新疆维吾尔自治区', '和田地区', '和田县', '447', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6812', '653222', '653200', '新疆维吾尔自治区', '和田地区', '墨玉县', '446', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6813', '653223', '653200', '新疆维吾尔自治区', '和田地区', '皮山县', '445', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6814', '653224', '653200', '新疆维吾尔自治区', '和田地区', '洛浦县', '444', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6815', '653225', '653200', '新疆维吾尔自治区', '和田地区', '策勒县', '443', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6816', '653226', '653200', '新疆维吾尔自治区', '和田地区', '于田县', '442', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6817', '653227', '653200', '新疆维吾尔自治区', '和田地区', '民丰县', '441', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6818', '654000', '650000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '', '440', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6819', '654002', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '伊宁市', '439', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6820', '654003', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '奎屯市', '438', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6821', '654004', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '霍尔果斯市', '437', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6822', '654021', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '伊宁县', '436', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6823', '654022', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '察布查尔锡伯自治县', '435', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6824', '654023', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '霍城县', '434', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6825', '654024', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '巩留县', '433', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6826', '654025', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '新源县', '432', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6827', '654026', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '昭苏县', '431', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6828', '654027', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '特克斯县', '430', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6829', '654028', '654000', '新疆维吾尔自治区', '伊犁哈萨克自治州', '尼勒克县', '429', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6830', '654200', '650000', '新疆维吾尔自治区', '塔城地区', '', '428', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6831', '654201', '654200', '新疆维吾尔自治区', '塔城地区', '塔城市', '427', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6832', '654202', '654200', '新疆维吾尔自治区', '塔城地区', '乌苏市', '426', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6833', '654221', '654200', '新疆维吾尔自治区', '塔城地区', '额敏县', '425', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6834', '654223', '654200', '新疆维吾尔自治区', '塔城地区', '沙湾县', '424', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6835', '654224', '654200', '新疆维吾尔自治区', '塔城地区', '托里县', '423', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6836', '654225', '654200', '新疆维吾尔自治区', '塔城地区', '裕民县', '422', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6837', '654226', '654200', '新疆维吾尔自治区', '塔城地区', '和布克赛尔蒙古自治县', '421', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6838', '654300', '650000', '新疆维吾尔自治区', '阿勒泰地区', '', '420', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6839', '654301', '654300', '新疆维吾尔自治区', '阿勒泰地区', '阿勒泰市', '419', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6840', '654321', '654300', '新疆维吾尔自治区', '阿勒泰地区', '布尔津县', '418', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6841', '654322', '654300', '新疆维吾尔自治区', '阿勒泰地区', '富蕴县', '417', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6842', '654323', '654300', '新疆维吾尔自治区', '阿勒泰地区', '福海县', '416', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6843', '654324', '654300', '新疆维吾尔自治区', '阿勒泰地区', '哈巴河县', '415', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6844', '654325', '654300', '新疆维吾尔自治区', '阿勒泰地区', '青河县', '414', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6845', '654326', '654300', '新疆维吾尔自治区', '阿勒泰地区', '吉木乃县', '413', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6846', '659001', 'null', '新疆维吾尔自治区', '石河子市', '石河子市', '412', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6847', '659002', 'null', '新疆维吾尔自治区', '阿拉尔市', '阿拉尔市', '411', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6848', '659003', 'null', '新疆维吾尔自治区', '图木舒克市', '图木舒克市', '410', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6849', '659004', 'null', '新疆维吾尔自治区', '五家渠市', '五家渠市', '409', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6850', '659005', 'null', '新疆维吾尔自治区', '北屯市', '北屯市', '408', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6851', '659006', 'null', '新疆维吾尔自治区', '铁门关市', '铁门关市', '407', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6852', '659007', 'null', '新疆维吾尔自治区', '双河市', '双河市', '406', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6853', '659008', 'null', '新疆维吾尔自治区', '可克达拉市', '可克达拉市', '405', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6854', '659009', 'null', '新疆维吾尔自治区', '昆玉市', '昆玉市', '404', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6855', '710000', '-1', '台湾省', '', '', '403', '0', '1', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6856', '710100', '710000', '台湾省', '台北市', '', '402', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6857', '710101', '710100', '台湾省', '台北市', '中正区', '401', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6858', '710102', '710100', '台湾省', '台北市', '大同区', '400', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6859', '710103', '710100', '台湾省', '台北市', '中山区', '399', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6860', '710104', '710100', '台湾省', '台北市', '松山区', '398', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6861', '710105', '710100', '台湾省', '台北市', '大安区', '397', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6862', '710106', '710100', '台湾省', '台北市', '万华区', '396', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6863', '710107', '710100', '台湾省', '台北市', '信义区', '395', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6864', '710108', '710100', '台湾省', '台北市', '士林区', '394', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6865', '710109', '710100', '台湾省', '台北市', '北投区', '393', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6866', '710110', '710100', '台湾省', '台北市', '内湖区', '392', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6867', '710111', '710100', '台湾省', '台北市', '南港区', '391', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6868', '710112', '710100', '台湾省', '台北市', '文山区', '390', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6869', '710200', '710000', '台湾省', '高雄市', '', '389', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6870', '710201', '710200', '台湾省', '高雄市', '新兴区', '388', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6871', '710202', '710200', '台湾省', '高雄市', '前金区', '387', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6872', '710203', '710200', '台湾省', '高雄市', '苓雅区', '386', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6873', '710204', '710200', '台湾省', '高雄市', '盐埕区', '385', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6874', '710205', '710200', '台湾省', '高雄市', '鼓山区', '384', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6875', '710206', '710200', '台湾省', '高雄市', '旗津区', '383', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6876', '710207', '710200', '台湾省', '高雄市', '前镇区', '382', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6877', '710208', '710200', '台湾省', '高雄市', '三民区', '381', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6878', '710209', '710200', '台湾省', '高雄市', '左营区', '380', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6879', '710210', '710200', '台湾省', '高雄市', '楠梓区', '379', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6880', '710211', '710200', '台湾省', '高雄市', '小港区', '378', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6881', '710242', '710200', '台湾省', '高雄市', '仁武区', '377', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6882', '710243', '710200', '台湾省', '高雄市', '大社区', '376', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6883', '710244', '710200', '台湾省', '高雄市', '冈山区', '375', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6884', '710245', '710200', '台湾省', '高雄市', '路竹区', '374', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6885', '710246', '710200', '台湾省', '高雄市', '阿莲区', '373', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6886', '710247', '710200', '台湾省', '高雄市', '田寮区', '372', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6887', '710248', '710200', '台湾省', '高雄市', '燕巢区', '371', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6888', '710249', '710200', '台湾省', '高雄市', '桥头区', '370', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6889', '710250', '710200', '台湾省', '高雄市', '梓官区', '369', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6890', '710251', '710200', '台湾省', '高雄市', '弥陀区', '368', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6891', '710252', '710200', '台湾省', '高雄市', '永安区', '367', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6892', '710253', '710200', '台湾省', '高雄市', '湖内区', '366', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6893', '710254', '710200', '台湾省', '高雄市', '凤山区', '365', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6894', '710255', '710200', '台湾省', '高雄市', '大寮区', '364', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6895', '710256', '710200', '台湾省', '高雄市', '林园区', '363', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6896', '710257', '710200', '台湾省', '高雄市', '鸟松区', '362', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6897', '710258', '710200', '台湾省', '高雄市', '大树区', '361', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6898', '710259', '710200', '台湾省', '高雄市', '旗山区', '360', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6899', '710260', '710200', '台湾省', '高雄市', '美浓区', '359', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6900', '710261', '710200', '台湾省', '高雄市', '六龟区', '358', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6901', '710262', '710200', '台湾省', '高雄市', '内门区', '357', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6902', '710263', '710200', '台湾省', '高雄市', '杉林区', '356', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6903', '710264', '710200', '台湾省', '高雄市', '甲仙区', '355', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6904', '710265', '710200', '台湾省', '高雄市', '桃源区', '354', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6905', '710266', '710200', '台湾省', '高雄市', '那玛夏区', '353', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6906', '710267', '710200', '台湾省', '高雄市', '茂林区', '352', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6907', '710268', '710200', '台湾省', '高雄市', '茄萣区', '351', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6908', '710300', '710000', '台湾省', '台南市', '', '350', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6909', '710301', '710300', '台湾省', '台南市', '中西区', '349', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6910', '710302', '710300', '台湾省', '台南市', '东区', '348', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6911', '710303', '710300', '台湾省', '台南市', '南区', '347', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6912', '710304', '710300', '台湾省', '台南市', '北区', '346', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6913', '710305', '710300', '台湾省', '台南市', '安平区', '345', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6914', '710306', '710300', '台湾省', '台南市', '安南区', '344', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6915', '710339', '710300', '台湾省', '台南市', '永康区', '343', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6916', '710340', '710300', '台湾省', '台南市', '归仁区', '342', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6917', '710341', '710300', '台湾省', '台南市', '新化区', '341', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6918', '710342', '710300', '台湾省', '台南市', '左镇区', '340', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6919', '710343', '710300', '台湾省', '台南市', '玉井区', '339', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6920', '710344', '710300', '台湾省', '台南市', '楠西区', '338', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6921', '710345', '710300', '台湾省', '台南市', '南化区', '337', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6922', '710346', '710300', '台湾省', '台南市', '仁德区', '336', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6923', '710347', '710300', '台湾省', '台南市', '关庙区', '335', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6924', '710348', '710300', '台湾省', '台南市', '龙崎区', '334', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6925', '710349', '710300', '台湾省', '台南市', '官田区', '333', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6926', '710350', '710300', '台湾省', '台南市', '麻豆区', '332', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6927', '710351', '710300', '台湾省', '台南市', '佳里区', '331', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6928', '710352', '710300', '台湾省', '台南市', '西港区', '330', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6929', '710353', '710300', '台湾省', '台南市', '七股区', '329', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6930', '710354', '710300', '台湾省', '台南市', '将军区', '328', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6931', '710355', '710300', '台湾省', '台南市', '学甲区', '327', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6932', '710356', '710300', '台湾省', '台南市', '北门区', '326', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6933', '710357', '710300', '台湾省', '台南市', '新营区', '325', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6934', '710358', '710300', '台湾省', '台南市', '后壁区', '324', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6935', '710359', '710300', '台湾省', '台南市', '白河区', '323', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6936', '710360', '710300', '台湾省', '台南市', '东山区', '322', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6937', '710361', '710300', '台湾省', '台南市', '六甲区', '321', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6938', '710362', '710300', '台湾省', '台南市', '下营区', '320', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6939', '710363', '710300', '台湾省', '台南市', '柳营区', '319', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6940', '710364', '710300', '台湾省', '台南市', '盐水区', '318', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6941', '710365', '710300', '台湾省', '台南市', '善化区', '317', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6942', '710366', '710300', '台湾省', '台南市', '大内区', '316', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6943', '710367', '710300', '台湾省', '台南市', '山上区', '315', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6944', '710368', '710300', '台湾省', '台南市', '新市区', '314', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6945', '710369', '710300', '台湾省', '台南市', '安定区', '313', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6946', '710400', '710000', '台湾省', '台中市', '', '312', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6947', '710401', '710400', '台湾省', '台中市', '中区', '311', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6948', '710402', '710400', '台湾省', '台中市', '东区', '310', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6949', '710403', '710400', '台湾省', '台中市', '南区', '309', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6950', '710404', '710400', '台湾省', '台中市', '西区', '308', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6951', '710405', '710400', '台湾省', '台中市', '北区', '307', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6952', '710406', '710400', '台湾省', '台中市', '北屯区', '306', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6953', '710407', '710400', '台湾省', '台中市', '西屯区', '305', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6954', '710408', '710400', '台湾省', '台中市', '南屯区', '304', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6955', '710431', '710400', '台湾省', '台中市', '太平区', '303', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6956', '710432', '710400', '台湾省', '台中市', '大里区', '302', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6957', '710433', '710400', '台湾省', '台中市', '雾峰区', '301', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6958', '710434', '710400', '台湾省', '台中市', '乌日区', '300', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6959', '710435', '710400', '台湾省', '台中市', '丰原区', '299', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6960', '710436', '710400', '台湾省', '台中市', '后里区', '298', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6961', '710437', '710400', '台湾省', '台中市', '石冈区', '297', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6962', '710438', '710400', '台湾省', '台中市', '东势区', '296', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6963', '710439', '710400', '台湾省', '台中市', '和平区', '295', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6964', '710440', '710400', '台湾省', '台中市', '新社区', '294', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6965', '710441', '710400', '台湾省', '台中市', '潭子区', '293', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6966', '710442', '710400', '台湾省', '台中市', '大雅区', '292', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6967', '710443', '710400', '台湾省', '台中市', '神冈区', '291', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6968', '710444', '710400', '台湾省', '台中市', '大肚区', '290', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6969', '710445', '710400', '台湾省', '台中市', '沙鹿区', '289', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6970', '710446', '710400', '台湾省', '台中市', '龙井区', '288', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6971', '710447', '710400', '台湾省', '台中市', '梧栖区', '287', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6972', '710448', '710400', '台湾省', '台中市', '清水区', '286', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6973', '710449', '710400', '台湾省', '台中市', '大甲区', '285', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6974', '710450', '710400', '台湾省', '台中市', '外埔区', '284', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6975', '710451', '710400', '台湾省', '台中市', '大安区', '283', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6976', '710600', '710000', '台湾省', '南投县', '', '282', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6977', '710614', '710600', '台湾省', '南投县', '南投市', '281', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6978', '710615', '710600', '台湾省', '南投县', '中寮乡', '280', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6979', '710616', '710600', '台湾省', '南投县', '草屯镇', '279', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6980', '710617', '710600', '台湾省', '南投县', '国姓乡', '278', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6981', '710618', '710600', '台湾省', '南投县', '埔里镇', '277', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6982', '710619', '710600', '台湾省', '南投县', '仁爱乡', '276', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6983', '710620', '710600', '台湾省', '南投县', '名间乡', '275', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6984', '710621', '710600', '台湾省', '南投县', '集集镇', '274', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6985', '710622', '710600', '台湾省', '南投县', '水里乡', '273', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6986', '710623', '710600', '台湾省', '南投县', '鱼池乡', '272', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6987', '710624', '710600', '台湾省', '南投县', '信义乡', '271', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6988', '710625', '710600', '台湾省', '南投县', '竹山镇', '270', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6989', '710626', '710600', '台湾省', '南投县', '鹿谷乡', '269', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6990', '710700', '710000', '台湾省', '基隆市', '', '268', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6991', '710701', '710700', '台湾省', '基隆市', '仁爱区', '267', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6992', '710702', '710700', '台湾省', '基隆市', '信义区', '266', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6993', '710703', '710700', '台湾省', '基隆市', '中正区', '265', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6994', '710704', '710700', '台湾省', '基隆市', '中山区', '264', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6995', '710705', '710700', '台湾省', '基隆市', '安乐区', '263', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6996', '710706', '710700', '台湾省', '基隆市', '暖暖区', '262', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6997', '710707', '710700', '台湾省', '基隆市', '七堵区', '261', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6998', '710800', '710000', '台湾省', '新竹市', '', '260', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('6999', '710801', '710800', '台湾省', '新竹市', '东区', '259', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7000', '710802', '710800', '台湾省', '新竹市', '北区', '258', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7001', '710803', '710800', '台湾省', '新竹市', '香山区', '257', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7002', '710900', '710000', '台湾省', '嘉义市', '', '256', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7003', '710901', '710900', '台湾省', '嘉义市', '东区', '255', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7004', '710902', '710900', '台湾省', '嘉义市', '西区', '254', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7005', '711100', '710000', '台湾省', '新北市', '', '253', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7006', '711130', '711100', '台湾省', '新北市', '万里区', '252', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7007', '711131', '711100', '台湾省', '新北市', '金山区', '251', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7008', '711132', '711100', '台湾省', '新北市', '板桥区', '250', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7009', '711133', '711100', '台湾省', '新北市', '汐止区', '249', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7010', '711134', '711100', '台湾省', '新北市', '深坑区', '248', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7011', '711135', '711100', '台湾省', '新北市', '石碇区', '247', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7012', '711136', '711100', '台湾省', '新北市', '瑞芳区', '246', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7013', '711137', '711100', '台湾省', '新北市', '平溪区', '245', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7014', '711138', '711100', '台湾省', '新北市', '双溪区', '244', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7015', '711139', '711100', '台湾省', '新北市', '贡寮区', '243', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7016', '711140', '711100', '台湾省', '新北市', '新店区', '242', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7017', '711141', '711100', '台湾省', '新北市', '坪林区', '241', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7018', '711142', '711100', '台湾省', '新北市', '乌来区', '240', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7019', '711143', '711100', '台湾省', '新北市', '永和区', '239', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7020', '711144', '711100', '台湾省', '新北市', '中和区', '238', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7021', '711145', '711100', '台湾省', '新北市', '土城区', '237', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7022', '711146', '711100', '台湾省', '新北市', '三峡区', '236', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7023', '711147', '711100', '台湾省', '新北市', '树林区', '235', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7024', '711148', '711100', '台湾省', '新北市', '莺歌区', '234', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7025', '711149', '711100', '台湾省', '新北市', '三重区', '233', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7026', '711150', '711100', '台湾省', '新北市', '新庄区', '232', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7027', '711151', '711100', '台湾省', '新北市', '泰山区', '231', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7028', '711152', '711100', '台湾省', '新北市', '林口区', '230', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7029', '711153', '711100', '台湾省', '新北市', '芦洲区', '229', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7030', '711154', '711100', '台湾省', '新北市', '五股区', '228', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7031', '711155', '711100', '台湾省', '新北市', '八里区', '227', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7032', '711156', '711100', '台湾省', '新北市', '淡水区', '226', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7033', '711157', '711100', '台湾省', '新北市', '三芝区', '225', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7034', '711158', '711100', '台湾省', '新北市', '石门区', '224', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7035', '711200', '710000', '台湾省', '宜兰县', '', '223', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7036', '711214', '711200', '台湾省', '宜兰县', '宜兰市', '222', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7037', '711215', '711200', '台湾省', '宜兰县', '头城镇', '221', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7038', '711216', '711200', '台湾省', '宜兰县', '礁溪乡', '220', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7039', '711217', '711200', '台湾省', '宜兰县', '壮围乡', '219', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7040', '711218', '711200', '台湾省', '宜兰县', '员山乡', '218', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7041', '711219', '711200', '台湾省', '宜兰县', '罗东镇', '217', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7042', '711220', '711200', '台湾省', '宜兰县', '三星乡', '216', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7043', '711221', '711200', '台湾省', '宜兰县', '大同乡', '215', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7044', '711222', '711200', '台湾省', '宜兰县', '五结乡', '214', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7045', '711223', '711200', '台湾省', '宜兰县', '冬山乡', '213', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7046', '711224', '711200', '台湾省', '宜兰县', '苏澳镇', '212', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7047', '711225', '711200', '台湾省', '宜兰县', '南澳乡', '211', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7048', '711300', '710000', '台湾省', '新竹县', '', '210', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7049', '711314', '711300', '台湾省', '新竹县', '竹北市', '209', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7050', '711315', '711300', '台湾省', '新竹县', '湖口乡', '208', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7051', '711316', '711300', '台湾省', '新竹县', '新丰乡', '207', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7052', '711317', '711300', '台湾省', '新竹县', '新埔镇', '206', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7053', '711318', '711300', '台湾省', '新竹县', '关西镇', '205', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7054', '711319', '711300', '台湾省', '新竹县', '芎林乡', '204', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7055', '711320', '711300', '台湾省', '新竹县', '宝山乡', '203', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7056', '711321', '711300', '台湾省', '新竹县', '竹东镇', '202', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7057', '711322', '711300', '台湾省', '新竹县', '五峰乡', '201', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7058', '711323', '711300', '台湾省', '新竹县', '横山乡', '200', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7059', '711324', '711300', '台湾省', '新竹县', '尖石乡', '199', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7060', '711325', '711300', '台湾省', '新竹县', '北埔乡', '198', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7061', '711326', '711300', '台湾省', '新竹县', '峨眉乡', '197', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7062', '711400', '710000', '台湾省', '桃园市', '', '196', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7063', '711414', '711400', '台湾省', '桃园市', '中坜区', '195', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7064', '711415', '711400', '台湾省', '桃园市', '平镇区', '194', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7065', '711416', '711400', '台湾省', '桃园市', '龙潭区', '193', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7066', '711417', '711400', '台湾省', '桃园市', '杨梅区', '192', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7067', '711418', '711400', '台湾省', '桃园市', '新屋区', '191', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7068', '711419', '711400', '台湾省', '桃园市', '观音区', '190', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7069', '711420', '711400', '台湾省', '桃园市', '桃园区', '189', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7070', '711421', '711400', '台湾省', '桃园市', '龟山区', '188', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7071', '711422', '711400', '台湾省', '桃园市', '八德区', '187', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7072', '711423', '711400', '台湾省', '桃园市', '大溪区', '186', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7073', '711424', '711400', '台湾省', '桃园市', '复兴区', '185', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7074', '711425', '711400', '台湾省', '桃园市', '大园区', '184', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7075', '711426', '711400', '台湾省', '桃园市', '芦竹区', '183', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7076', '711500', '710000', '台湾省', '苗栗县', '', '182', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7077', '711519', '711500', '台湾省', '苗栗县', '竹南镇', '181', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7078', '711520', '711500', '台湾省', '苗栗县', '头份市', '180', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7079', '711521', '711500', '台湾省', '苗栗县', '三湾乡', '179', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7080', '711522', '711500', '台湾省', '苗栗县', '南庄乡', '178', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7081', '711523', '711500', '台湾省', '苗栗县', '狮潭乡', '177', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7082', '711524', '711500', '台湾省', '苗栗县', '后龙镇', '176', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7083', '711525', '711500', '台湾省', '苗栗县', '通霄镇', '175', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7084', '711526', '711500', '台湾省', '苗栗县', '苑里镇', '174', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7085', '711527', '711500', '台湾省', '苗栗县', '苗栗市', '173', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7086', '711528', '711500', '台湾省', '苗栗县', '造桥乡', '172', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7087', '711529', '711500', '台湾省', '苗栗县', '头屋乡', '171', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7088', '711530', '711500', '台湾省', '苗栗县', '公馆乡', '170', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7089', '711531', '711500', '台湾省', '苗栗县', '大湖乡', '169', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7090', '711532', '711500', '台湾省', '苗栗县', '泰安乡', '168', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7091', '711533', '711500', '台湾省', '苗栗县', '铜锣乡', '167', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7092', '711534', '711500', '台湾省', '苗栗县', '三义乡', '166', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7093', '711535', '711500', '台湾省', '苗栗县', '西湖乡', '165', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7094', '711536', '711500', '台湾省', '苗栗县', '卓兰镇', '164', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7095', '711700', '710000', '台湾省', '彰化县', '', '163', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7096', '711727', '711700', '台湾省', '彰化县', '彰化市', '162', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7097', '711728', '711700', '台湾省', '彰化县', '芬园乡', '161', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7098', '711729', '711700', '台湾省', '彰化县', '花坛乡', '160', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7099', '711730', '711700', '台湾省', '彰化县', '秀水乡', '159', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7100', '711731', '711700', '台湾省', '彰化县', '鹿港镇', '158', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7101', '711732', '711700', '台湾省', '彰化县', '福兴乡', '157', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7102', '711733', '711700', '台湾省', '彰化县', '线西乡', '156', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7103', '711734', '711700', '台湾省', '彰化县', '和美镇', '155', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7104', '711735', '711700', '台湾省', '彰化县', '伸港乡', '154', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7105', '711736', '711700', '台湾省', '彰化县', '员林市', '153', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7106', '711737', '711700', '台湾省', '彰化县', '社头乡', '152', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7107', '711738', '711700', '台湾省', '彰化县', '永靖乡', '151', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7108', '711739', '711700', '台湾省', '彰化县', '埔心乡', '150', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7109', '711740', '711700', '台湾省', '彰化县', '溪湖镇', '149', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7110', '711741', '711700', '台湾省', '彰化县', '大村乡', '148', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7111', '711742', '711700', '台湾省', '彰化县', '埔盐乡', '147', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7112', '711743', '711700', '台湾省', '彰化县', '田中镇', '146', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7113', '711744', '711700', '台湾省', '彰化县', '北斗镇', '145', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7114', '711745', '711700', '台湾省', '彰化县', '田尾乡', '144', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7115', '711746', '711700', '台湾省', '彰化县', '埤头乡', '143', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7116', '711747', '711700', '台湾省', '彰化县', '溪州乡', '142', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7117', '711748', '711700', '台湾省', '彰化县', '竹塘乡', '141', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7118', '711749', '711700', '台湾省', '彰化县', '二林镇', '140', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7119', '711750', '711700', '台湾省', '彰化县', '大城乡', '139', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7120', '711751', '711700', '台湾省', '彰化县', '芳苑乡', '138', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7121', '711752', '711700', '台湾省', '彰化县', '二水乡', '137', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7122', '711900', '710000', '台湾省', '嘉义县', '', '136', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7123', '711919', '711900', '台湾省', '嘉义县', '番路乡', '135', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7124', '711920', '711900', '台湾省', '嘉义县', '梅山乡', '134', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7125', '711921', '711900', '台湾省', '嘉义县', '竹崎乡', '133', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7126', '711922', '711900', '台湾省', '嘉义县', '阿里山乡', '132', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7127', '711923', '711900', '台湾省', '嘉义县', '中埔乡', '131', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7128', '711924', '711900', '台湾省', '嘉义县', '大埔乡', '130', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7129', '711925', '711900', '台湾省', '嘉义县', '水上乡', '129', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7130', '711926', '711900', '台湾省', '嘉义县', '鹿草乡', '128', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7131', '711927', '711900', '台湾省', '嘉义县', '太保市', '127', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7132', '711928', '711900', '台湾省', '嘉义县', '朴子市', '126', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7133', '711929', '711900', '台湾省', '嘉义县', '东石乡', '125', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7134', '711930', '711900', '台湾省', '嘉义县', '六脚乡', '124', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7135', '711931', '711900', '台湾省', '嘉义县', '新港乡', '123', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7136', '711932', '711900', '台湾省', '嘉义县', '民雄乡', '122', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7137', '711933', '711900', '台湾省', '嘉义县', '大林镇', '121', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7138', '711934', '711900', '台湾省', '嘉义县', '溪口乡', '120', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7139', '711935', '711900', '台湾省', '嘉义县', '义竹乡', '119', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7140', '711936', '711900', '台湾省', '嘉义县', '布袋镇', '118', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7141', '712100', '710000', '台湾省', '云林县', '', '117', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7142', '712121', '712100', '台湾省', '云林县', '斗南镇', '116', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7143', '712122', '712100', '台湾省', '云林县', '大埤乡', '115', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7144', '712123', '712100', '台湾省', '云林县', '虎尾镇', '114', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7145', '712124', '712100', '台湾省', '云林县', '土库镇', '113', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7146', '712125', '712100', '台湾省', '云林县', '褒忠乡', '112', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7147', '712126', '712100', '台湾省', '云林县', '东势乡', '111', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7148', '712127', '712100', '台湾省', '云林县', '台西乡', '110', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7149', '712128', '712100', '台湾省', '云林县', '仑背乡', '109', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7150', '712129', '712100', '台湾省', '云林县', '麦寮乡', '108', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7151', '712130', '712100', '台湾省', '云林县', '斗六市', '107', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7152', '712131', '712100', '台湾省', '云林县', '林内乡', '106', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7153', '712132', '712100', '台湾省', '云林县', '古坑乡', '105', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7154', '712133', '712100', '台湾省', '云林县', '莿桐乡', '104', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7155', '712134', '712100', '台湾省', '云林县', '西螺镇', '103', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7156', '712135', '712100', '台湾省', '云林县', '二仑乡', '102', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7157', '712136', '712100', '台湾省', '云林县', '北港镇', '101', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7158', '712137', '712100', '台湾省', '云林县', '水林乡', '100', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7159', '712138', '712100', '台湾省', '云林县', '口湖乡', '99', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7160', '712139', '712100', '台湾省', '云林县', '四湖乡', '98', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7161', '712140', '712100', '台湾省', '云林县', '元长乡', '97', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7162', '712400', '710000', '台湾省', '屏东县', '', '96', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7163', '712434', '712400', '台湾省', '屏东县', '屏东市', '95', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7164', '712435', '712400', '台湾省', '屏东县', '三地门乡', '94', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7165', '712436', '712400', '台湾省', '屏东县', '雾台乡', '93', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7166', '712437', '712400', '台湾省', '屏东县', '玛家乡', '92', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7167', '712438', '712400', '台湾省', '屏东县', '九如乡', '91', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7168', '712439', '712400', '台湾省', '屏东县', '里港乡', '90', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7169', '712440', '712400', '台湾省', '屏东县', '高树乡', '89', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7170', '712441', '712400', '台湾省', '屏东县', '盐埔乡', '88', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7171', '712442', '712400', '台湾省', '屏东县', '长治乡', '87', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7172', '712443', '712400', '台湾省', '屏东县', '麟洛乡', '86', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7173', '712444', '712400', '台湾省', '屏东县', '竹田乡', '85', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7174', '712445', '712400', '台湾省', '屏东县', '内埔乡', '84', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7175', '712446', '712400', '台湾省', '屏东县', '万丹乡', '83', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7176', '712447', '712400', '台湾省', '屏东县', '潮州镇', '82', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7177', '712448', '712400', '台湾省', '屏东县', '泰武乡', '81', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7178', '712449', '712400', '台湾省', '屏东县', '来义乡', '80', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7179', '712450', '712400', '台湾省', '屏东县', '万峦乡', '79', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7180', '712451', '712400', '台湾省', '屏东县', '崁顶乡', '78', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7181', '712452', '712400', '台湾省', '屏东县', '新埤乡', '77', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7182', '712453', '712400', '台湾省', '屏东县', '南州乡', '76', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7183', '712454', '712400', '台湾省', '屏东县', '林边乡', '75', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7184', '712455', '712400', '台湾省', '屏东县', '东港镇', '74', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7185', '712456', '712400', '台湾省', '屏东县', '琉球乡', '73', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7186', '712457', '712400', '台湾省', '屏东县', '佳冬乡', '72', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7187', '712458', '712400', '台湾省', '屏东县', '新园乡', '71', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7188', '712459', '712400', '台湾省', '屏东县', '枋寮乡', '70', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7189', '712460', '712400', '台湾省', '屏东县', '枋山乡', '69', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7190', '712461', '712400', '台湾省', '屏东县', '春日乡', '68', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7191', '712462', '712400', '台湾省', '屏东县', '狮子乡', '67', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7192', '712463', '712400', '台湾省', '屏东县', '车城乡', '66', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7193', '712464', '712400', '台湾省', '屏东县', '牡丹乡', '65', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7194', '712465', '712400', '台湾省', '屏东县', '恒春镇', '64', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7195', '712466', '712400', '台湾省', '屏东县', '满州乡', '63', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7196', '712500', '710000', '台湾省', '台东县', '', '62', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7197', '712517', '712500', '台湾省', '台东县', '台东市', '61', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7198', '712518', '712500', '台湾省', '台东县', '绿岛乡', '60', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7199', '712519', '712500', '台湾省', '台东县', '兰屿乡', '59', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7200', '712520', '712500', '台湾省', '台东县', '延平乡', '58', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7201', '712521', '712500', '台湾省', '台东县', '卑南乡', '57', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7202', '712522', '712500', '台湾省', '台东县', '鹿野乡', '56', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7203', '712523', '712500', '台湾省', '台东县', '关山镇', '55', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7204', '712524', '712500', '台湾省', '台东县', '海端乡', '54', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7205', '712525', '712500', '台湾省', '台东县', '池上乡', '53', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7206', '712526', '712500', '台湾省', '台东县', '东河乡', '52', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7207', '712527', '712500', '台湾省', '台东县', '成功镇', '51', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7208', '712528', '712500', '台湾省', '台东县', '长滨乡', '50', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7209', '712529', '712500', '台湾省', '台东县', '金峰乡', '49', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7210', '712530', '712500', '台湾省', '台东县', '大武乡', '48', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7211', '712531', '712500', '台湾省', '台东县', '达仁乡', '47', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7212', '712532', '712500', '台湾省', '台东县', '太麻里乡', '46', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7213', '712600', '710000', '台湾省', '花莲县', '', '45', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7214', '712615', '712600', '台湾省', '花莲县', '花莲市', '44', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7215', '712616', '712600', '台湾省', '花莲县', '新城乡', '43', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7216', '712618', '712600', '台湾省', '花莲县', '秀林乡', '42', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7217', '712619', '712600', '台湾省', '花莲县', '吉安乡', '41', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7218', '712620', '712600', '台湾省', '花莲县', '寿丰乡', '40', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7219', '712621', '712600', '台湾省', '花莲县', '凤林镇', '39', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7220', '712622', '712600', '台湾省', '花莲县', '光复乡', '38', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7221', '712623', '712600', '台湾省', '花莲县', '丰滨乡', '37', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7222', '712624', '712600', '台湾省', '花莲县', '瑞穗乡', '36', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7223', '712625', '712600', '台湾省', '花莲县', '万荣乡', '35', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7224', '712626', '712600', '台湾省', '花莲县', '玉里镇', '34', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7225', '712627', '712600', '台湾省', '花莲县', '卓溪乡', '33', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7226', '712628', '712600', '台湾省', '花莲县', '富里乡', '32', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7227', '712700', '710000', '台湾省', '澎湖县', '', '31', '0', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7228', '712707', '712700', '台湾省', '澎湖县', '马公市', '30', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7229', '712708', '712700', '台湾省', '澎湖县', '西屿乡', '29', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7230', '712709', '712700', '台湾省', '澎湖县', '望安乡', '28', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7231', '712710', '712700', '台湾省', '澎湖县', '七美乡', '27', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7232', '712711', '712700', '台湾省', '澎湖县', '白沙乡', '26', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7233', '712712', '712700', '台湾省', '澎湖县', '湖西乡', '25', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7234', '810000', '-1', '香港特别行政区', '香港特别行政区', '', '24', '1', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7235', '810101', '810000', '香港特别行政区', '香港特别行政区', '中西区', '23', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7236', '810102', '810000', '香港特别行政区', '香港特别行政区', '东区', '22', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7237', '810103', '810000', '香港特别行政区', '香港特别行政区', '九龙城区', '21', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7238', '810104', '810000', '香港特别行政区', '香港特别行政区', '观塘区', '20', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7239', '810105', '810000', '香港特别行政区', '香港特别行政区', '南区', '19', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7240', '810106', '810000', '香港特别行政区', '香港特别行政区', '深水埗区', '18', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7241', '810107', '810000', '香港特别行政区', '香港特别行政区', '湾仔区', '17', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7242', '810108', '810000', '香港特别行政区', '香港特别行政区', '黄大仙区', '16', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7243', '810109', '810000', '香港特别行政区', '香港特别行政区', '油尖旺区', '15', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7244', '810110', '810000', '香港特别行政区', '香港特别行政区', '离岛区', '14', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7245', '810111', '810000', '香港特别行政区', '香港特别行政区', '葵青区', '13', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7246', '810112', '810000', '香港特别行政区', '香港特别行政区', '北区', '12', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7247', '810113', '810000', '香港特别行政区', '香港特别行政区', '西贡区', '11', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7248', '810114', '810000', '香港特别行政区', '香港特别行政区', '沙田区', '10', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7249', '810115', '810000', '香港特别行政区', '香港特别行政区', '屯门区', '9', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7250', '810116', '810000', '香港特别行政区', '香港特别行政区', '大埔区', '8', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7251', '810117', '810000', '香港特别行政区', '香港特别行政区', '荃湾区', '7', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7252', '810118', '810000', '香港特别行政区', '香港特别行政区', '元朗区', '6', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7253', '820000', '-1', '澳门特别行政区', '澳门特别行政区', '', '5', '1', '2', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7254', '820101', '820000', '澳门特别行政区', '澳门特别行政区', '澳门半岛', '4', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7255', '820102', '820000', '澳门特别行政区', '澳门特别行政区', '凼仔', '3', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7256', '820103', '820000', '澳门特别行政区', '澳门特别行政区', '路凼城', '2', '0', '3', null, '10001001', null, null, '-1', null, '0');
INSERT INTO `t_area` VALUES ('7257', '820104', '820000', '澳门特别行政区', '澳门特别行政区', '路环', '1', '0', '3', null, '10001001', null, null, '-1', null, '0');

-- ----------------------------
-- Table structure for t_attribute_key
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute_key`;
CREATE TABLE `t_attribute_key` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '所属类别',
  `attribute_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '属性名',
  `attribute_sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品属性名表';

-- ----------------------------
-- Records of t_attribute_key
-- ----------------------------
INSERT INTO `t_attribute_key` VALUES ('1', '4', '内存', '0', null, '0', '2021-01-04 15:06:37.000000', '0', null, null);
INSERT INTO `t_attribute_key` VALUES ('2', '4', '颜色', '1', null, '0', '2021-01-04 15:06:49.000000', '0', null, null);
INSERT INTO `t_attribute_key` VALUES ('3', '5', '内存', '0', null, '0', '2021-01-04 15:07:13.000000', '0', null, null);
INSERT INTO `t_attribute_key` VALUES ('4', '5', '颜色', '1', null, '0', '2021-01-04 15:07:26.000000', '0', null, null);

-- ----------------------------
-- Table structure for t_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute_value`;
CREATE TABLE `t_attribute_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `attribute_key_id` bigint(20) NOT NULL COMMENT '所属属性KEY',
  `attribute_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '属性名',
  `attribute_sort` bigint(20) DEFAULT NULL COMMENT '排序',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品属性值表';

-- ----------------------------
-- Records of t_attribute_value
-- ----------------------------
INSERT INTO `t_attribute_value` VALUES ('1', '1', '8G', '0', null, '0', '2021-01-04 15:08:35.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('2', '1', '12G', '1', null, '0', '2021-01-04 15:08:43.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('4', '3', '8G+128G', '1', null, '0', '2021-01-04 15:09:13.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('5', '3', '8G+256G', '2', null, '0', '2021-01-04 15:09:22.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('6', '2', '蓝色', '0', null, '0', '2021-01-04 15:09:40.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('7', '2', '黑色', '1', null, '0', '2021-01-04 15:09:52.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('8', '4', '亮黑色', '0', null, '0', '2021-01-04 15:10:08.000000', '0', null, null);
INSERT INTO `t_attribute_value` VALUES ('9', '4', '8号色', '1', null, '0', '2021-01-04 15:10:24.000000', '0', null, null);

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `img_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片路径',
  `click_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '点击路径',
  `position` smallint(6) DEFAULT NULL COMMENT '类型 0:首页顶部 1:栏目左侧',
  `banner_sort` bigint(20) unsigned DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='轮播图表';

-- ----------------------------
-- Records of t_banner
-- ----------------------------
INSERT INTO `t_banner` VALUES ('825024447403528204', '测试轮播图', 'https://img.alicdn.com/imgextra/i1/6000000008048/O1CN01oIVdRc29K2D0FObyv_!!6000000008048-0-octopus.jpg', null, null, null, null, '2021-03-26 15:12:36', null, '10001001', null, null, '0');

-- ----------------------------
-- Table structure for t_banner_area
-- ----------------------------
DROP TABLE IF EXISTS `t_banner_area`;
CREATE TABLE `t_banner_area` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `banner_id` bigint(20) NOT NULL COMMENT '轮播图主键',
  `area_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区编码',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='轮播图地区表';

-- ----------------------------
-- Records of t_banner_area
-- ----------------------------

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) NOT NULL COMMENT '父类别ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类别名称',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '-1' COMMENT '类别图标',
  `type` smallint(6) DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `category_sort` bigint(20) unsigned DEFAULT '0' COMMENT '排序',
  `show_status` tinyint(4) DEFAULT '1' COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '点击类别跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品类别表';

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES ('324', '-1', '家用电器', '/static/images/nav9.png', '1', '0', '1', 'www.baidu.com', '类别备注', '2021-03-29 16:46:12.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('325', '324', '电视', '-1', '1', '0', '1', 'www.163com', '类别备注', '2021-03-29 16:46:51.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('326', '325', '超薄电视', '-1', '1', '0', '1', 'www.taobao.com', '类别备注', '2021-03-29 16:47:12.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('327', '-1', '手机;运营商;数码', '/static/images/nav9.png', '1', '0', '1', 'www.taobao.com;www.jd.com;www.163.com', null, '2021-03-29 17:01:52.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('328', '327', '手机通讯', '-1', '1', '0', '1', 'www.taobao.com', null, '2021-03-29 17:03:24.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('329', '328', '手机', '-1', '1', '0', '1', 'www.taobao.com', null, '2021-03-29 17:05:04.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('330', '-1', '电脑;办公', '/static/images/nav9.png', '1', '0', '1', 'www.163.com;www.163.com', null, '2021-03-30 14:11:04.000000', null, null, null, '0');
INSERT INTO `t_category` VALUES ('331', '330', '电脑整机', '-1', null, '0', '1', null, null, null, null, null, null, '0');
INSERT INTO `t_category` VALUES ('332', '331', '笔记本', '-1', null, '0', '1', null, null, null, null, null, null, '0');

-- ----------------------------
-- Table structure for t_category_area
-- ----------------------------
DROP TABLE IF EXISTS `t_category_area`;
CREATE TABLE `t_category_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '类别主键',
  `area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区编码',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='类别地区表';

-- ----------------------------
-- Records of t_category_area
-- ----------------------------
INSERT INTO `t_category_area` VALUES ('22', '324', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('23', '325', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('24', '326', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('25', '327', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('26', '328', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('27', '329', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('28', '330', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('29', '331', '110000', null, null, null, null, '0');
INSERT INTO `t_category_area` VALUES ('30', '332', '110000', null, null, null, null, '0');

-- ----------------------------
-- Table structure for t_category_img
-- ----------------------------
DROP TABLE IF EXISTS `t_category_img`;
CREATE TABLE `t_category_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '类别ID',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片地址',
  `type` smallint(6) DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `category_sort` bigint(20) unsigned DEFAULT '0' COMMENT '排序',
  `show_status` tinyint(4) DEFAULT '1' COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '点击跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='类别广告图片表';

-- ----------------------------
-- Records of t_category_img
-- ----------------------------
INSERT INTO `t_category_img` VALUES ('330', '324', 'https://file06.16sucai.com/2016/0329/d5165ca7147d15d44cc17424c28c49e4.jpg', '1', '0', '1', null, null, null, null, null, null, '0');
INSERT INTO `t_category_img` VALUES ('331', '324', 'https://p0.ssl.qhimgs1.com/t01b8ad8aa5018be314.jpg', '1', '0', '1', null, null, null, null, null, null, '0');

-- ----------------------------
-- Table structure for t_column
-- ----------------------------
DROP TABLE IF EXISTS `t_column`;
CREATE TABLE `t_column` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `type` smallint(6) DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `show_status` tinyint(4) DEFAULT NULL COMMENT '显示状态 0隐藏 1显示',
  `position` smallint(6) DEFAULT NULL COMMENT '栏目位置 1首页',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='栏目表';

-- ----------------------------
-- Records of t_column
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_area
-- ----------------------------
DROP TABLE IF EXISTS `t_column_area`;
CREATE TABLE `t_column_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column_id` bigint(20) NOT NULL COMMENT '栏目主键',
  `area_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区编码',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='栏目地区表';

-- ----------------------------
-- Records of t_column_area
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_column_banner`;
CREATE TABLE `t_column_banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `banner_id` bigint(20) NOT NULL COMMENT '轮播图主键',
  `column_id` bigint(20) NOT NULL COMMENT '栏目主键',
  `position` smallint(6) DEFAULT NULL COMMENT '位置 1:栏目左侧',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='栏目轮播图关联表';

-- ----------------------------
-- Records of t_column_banner
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_img
-- ----------------------------
DROP TABLE IF EXISTS `t_column_img`;
CREATE TABLE `t_column_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column_id` bigint(20) NOT NULL COMMENT '栏目ID',
  `src` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片地址',
  `type` smallint(6) DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `img_sort` bigint(20) unsigned DEFAULT '0' COMMENT '排序',
  `show_status` tinyint(4) DEFAULT '1' COMMENT '显示状态 0隐藏 1显示',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '点击跳转路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='栏目图片表';

-- ----------------------------
-- Records of t_column_img
-- ----------------------------

-- ----------------------------
-- Table structure for t_column_sku_category
-- ----------------------------
DROP TABLE IF EXISTS `t_column_sku_category`;
CREATE TABLE `t_column_sku_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '类别名称',
  `href` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '点击类别跳转连接',
  `column_id` bigint(20) NOT NULL COMMENT '栏目主键',
  `position` smallint(6) DEFAULT NULL COMMENT '位置 1:栏目顶部 2:栏目左侧',
  `category_sort` bigint(20) DEFAULT NULL COMMENT '类别排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='栏目推荐商品类别表';

-- ----------------------------
-- Records of t_column_sku_category
-- ----------------------------

-- ----------------------------
-- Table structure for t_event_process
-- ----------------------------
DROP TABLE IF EXISTS `t_event_process`;
CREATE TABLE `t_event_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息状态 0:待发送 1:已发送',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '消息内容',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务ID',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事务ID,UUID',
  `table_name` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务表名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14073 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='收消息记录表';

-- ----------------------------
-- Records of t_event_process
-- ----------------------------

-- ----------------------------
-- Table structure for t_event_publish
-- ----------------------------
DROP TABLE IF EXISTS `t_event_publish`;
CREATE TABLE `t_event_publish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息状态 0:待发送 1:已发送',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '消息内容',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '消息类型',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务ID',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '事务ID,UUID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13080 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='发消息表';

-- ----------------------------
-- Records of t_event_publish
-- ----------------------------

-- ----------------------------
-- Table structure for t_news
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标题',
  `content_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '内容路径,如/content/news/new1.html',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '作者',
  `show_date` datetime DEFAULT NULL COMMENT '开始显示时间',
  `news_sort` bigint(20) unsigned DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_admin_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='新闻表';

-- ----------------------------
-- Records of t_news
-- ----------------------------

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '付款金额',
  `total_amount` decimal(10,2) NOT NULL COMMENT '商品最终金额(折扣算完)',
  `pay_status` smallint(6) NOT NULL COMMENT '支付状态 0未支付 1已支付 3线下支付已到账 4取消支付',
  `trade_status` smallint(6) NOT NULL COMMENT '交易状态 0进行中 1已完成 2已取消交易 3已结算',
  `pay_method` smallint(6) DEFAULT NULL COMMENT '支付方式 1线上支付 2线下支付',
  `pay_type` smallint(6) NOT NULL COMMENT '交易类型 0微信 1支付宝',
  `outer_trade_no` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '交易订单号(微信支付宝交易流水号)',
  `best_date` datetime DEFAULT NULL COMMENT '最佳送货时间',
  `pay_date` datetime DEFAULT NULL COMMENT '订单支付时间',
  `src_type` smallint(6) DEFAULT NULL COMMENT '用户购买渠道 1:pc  2:app',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单备注 买家填写',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单信息表';

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('16093', '22011615175364235000000000059818', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 03:49:26.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16094', '22011615175368686000000000082625', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 03:49:29.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16095', '22011615175426513000000000097192', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 03:50:28.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16096', '22011615179502383000000000072065', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 04:58:23.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16097', '22011615179504853000000000015988', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-08 04:58:25.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16098', '22011615559556940000000000048979', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:32:47.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16099', '22011615559644795000000000082736', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:34:05.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16100', '22011615559653361000000000051477', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:34:14.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16101', '22011615559656692000000000046201', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 14:34:17.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16102', '22011615565862391000000000000455', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 16:17:50.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16103', '22011615567626417000000000058849', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 16:47:14.000000', '10001001', '1', '0');
INSERT INTO `t_order` VALUES ('16104', '22011615567700628000000000081238', '1', '8998.00', '0.00', '8998.00', '0', '0', '1', '-1', null, null, null, null, null, '2021-03-12 16:48:21.000000', '10001001', '1', '0');

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `order_id` bigint(20) NOT NULL COMMENT '订单主表ID',
  `sku_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品SKUID',
  `delivery_status` smallint(6) NOT NULL COMMENT '配送状态 0未收货 1送货中 2已收货',
  `seller_status` smallint(6) NOT NULL COMMENT '卖家备货状态 0备货中 1备货完成 2缺货',
  `buyer_status` smallint(6) NOT NULL COMMENT '买家状态 0待收货 1已收货 2换货 3退货',
  `product_num` bigint(20) NOT NULL COMMENT '购买商品数量',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `order_item_amount` decimal(10,2) NOT NULL COMMENT '订单单项总金额',
  `delivery_money` decimal(10,2) NOT NULL COMMENT '配送费用',
  `delivery_receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `delivery_finish_time` datetime DEFAULT NULL COMMENT '配送人员完成时间',
  `seller_finish_time` datetime DEFAULT NULL COMMENT '卖家完成时间',
  `buyer_finish_time` datetime DEFAULT NULL COMMENT '买家完成时间',
  `src_type` smallint(6) DEFAULT NULL COMMENT '用户购买渠道 1:pc  2:app',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '订单备注 买家填写',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '所属应用',
  `create_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17510 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单项信息表';

-- ----------------------------
-- Records of t_order_item
-- ----------------------------
INSERT INTO `t_order_item` VALUES ('17486', '1', '22011615175364235000000000059818', '16093', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:26.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17487', '1', '22011615175364235000000000059818', '16093', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:26.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17488', '1', '22011615175368686000000000082625', '16094', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:29.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17489', '1', '22011615175368686000000000082625', '16094', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:49:29.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17490', '1', '22011615175426513000000000097192', '16095', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:50:28.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17491', '1', '22011615175426513000000000097192', '16095', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 03:50:28.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17492', '1', '22011615179502383000000000072065', '16096', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:23.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17493', '1', '22011615179502383000000000072065', '16096', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:23.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17494', '1', '22011615179504853000000000015988', '16097', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:25.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17495', '1', '22011615179504853000000000015988', '16097', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-08 04:58:25.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17496', '1', '22011615559556940000000000048979', '16098', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:32:48.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17497', '1', '22011615559556940000000000048979', '16098', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:32:48.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17498', '1', '22011615559644795000000000082736', '16099', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:05.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17499', '1', '22011615559644795000000000082736', '16099', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:05.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17500', '1', '22011615559653361000000000051477', '16100', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17501', '1', '22011615559653361000000000051477', '16100', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17502', '1', '22011615559656692000000000046201', '16101', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:17.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17503', '1', '22011615559656692000000000046201', '16101', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 14:34:17.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17504', '1', '22011615565862391000000000000455', '16102', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:17:51.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17505', '1', '22011615565862391000000000000455', '16102', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:17:51.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17506', '1', '22011615567626417000000000058849', '16103', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:47:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17507', '1', '22011615567626417000000000058849', '16103', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:47:14.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17508', '1', '22011615567700628000000000081238', '16104', '0000163b10f067dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4299.00', '4299.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:48:21.000000', '10001001', '1', '0');
INSERT INTO `t_order_item` VALUES ('17509', '1', '22011615567700628000000000081238', '16104', '0000165c2b8b77dbf11eb85a380a5893fe2d8', '0', '0', '0', '1', '4699.00', '4699.00', '0.00', null, null, null, null, null, null, '2021-03-12 16:48:21.000000', '10001001', '1', '0');

-- ----------------------------
-- Table structure for t_order_region
-- ----------------------------
DROP TABLE IF EXISTS `t_order_region`;
CREATE TABLE `t_order_region` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `region_code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地区编码',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单区域信息表';

-- ----------------------------
-- Records of t_order_region
-- ----------------------------

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SPU的UUID',
  `category_id` bigint(20) DEFAULT NULL COMMENT '所属类别',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '商品所有属性',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品表';

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES ('1', '6d60a39cf4cf4bccb1159fec61ba147b', '4', '小米11', '{\"内存\": [\"8G\",\"12G\"],\"颜色\":[\"蓝色\",\"黑色\"]}', null, '0', '2021-01-04 15:23:39.000000', '0', null, null);
INSERT INTO `t_product` VALUES ('2', '337c8f9148294a488e7aad2cf733632a', '5', '华为 HUAWEI nova 8', '{\"内存\": [\"8G+128G\",\"8G+256G\"],\"颜色\":[\"亮黑色\",\"8号色\"]}', null, '0', '2021-01-04 15:48:06.000000', '0', null, null);

-- ----------------------------
-- Table structure for t_product_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_product_sku`;
CREATE TABLE `t_product_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU主键',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SKU的UUID',
  `product_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SPU(所属商品)',
  `attributes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '这个SKU的商品属性',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  `status` smallint(6) DEFAULT '0' COMMENT '是否上架 0:未上架 1:已上架',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_date` datetime(6) DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品SKU表';

-- ----------------------------
-- Records of t_product_sku
-- ----------------------------
INSERT INTO `t_product_sku` VALUES ('1', '63b10f067dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '4299.00', '1', '8G 蓝色 小米11手机', '10001001', '0', '2021-01-04 15:42:33.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('2', '65c2b8b77dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"12G\"],\"颜色\":[\"蓝色\"]}', '4699.00', '1', '12G 蓝色 小米11手机', '10001001', '0', '2021-01-04 15:43:29.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('3', '66e93ba77dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"黑色\"]}', '4299.00', '1', '8G 黑色 小米11手机', '10001001', '0', '2021-01-04 15:44:06.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('4', '6dbc4fcc7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"12G\"],\"颜色\":[\"黑色\"]}', '4699.00', '1', '12G 黑色 小米11手机', '10001001', '0', '2021-01-04 15:44:33.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('5', '6fb4cbcf7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+128G\"],\"颜色\":[\"亮黑色\"]}', '3299.00', '1', '8G+128G 亮黑色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:51:23.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('6', '7173995a7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+128G\"],\"颜色\":[\"8号色\"]}', '3299.00', '1', '8G+128G 8号色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:52:13.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('7', '7357066e7dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+256G\"],\"颜色\":[\"亮黑色\"]}', '3699.00', '1', '8G+256G 亮黑色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:53:05.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('8', '74b367387dbf11eb85a380a5893fe2d8', '337c8f9148294a488e7aad2cf733632a', '{\"内存\": [\"8G+256G\"],\"颜色\":[\"8号色\"]}', '3699.00', '1', '8G+256G 8号色 华为 NOVA8手机', '10001001', '0', '2021-01-04 15:53:52.000000', '0', null, null);
INSERT INTO `t_product_sku` VALUES ('20', '7823949b7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注', '10001001', '0', '2021-02-01 02:59:12.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('21', '7a010e297dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注', '10001001', '0', '2021-02-01 03:21:45.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('22', '7b5817af7dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '华为手机备注', '10001001', '0', '2021-02-01 03:22:03.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('23', '7c999d147dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注 主库', '10001001', '0', '2021-02-02 08:18:42.000000', '1', null, null);
INSERT INTO `t_product_sku` VALUES ('24', '7e6f27447dbf11eb85a380a5893fe2d8', '6d60a39cf4cf4bccb1159fec61ba147b', '{\"内存\": [\"8G\"],\"颜色\":[\"蓝色\"]}', '999.99', '1', '小米手机备注 主库测试02', '10001001', '0', '2021-02-04 02:45:54.000000', '1', null, null);

-- ----------------------------
-- Table structure for t_product_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_product_sku_stock`;
CREATE TABLE `t_product_sku_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存表主键',
  `product_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SPU(所属商品)',
  `uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sku_uuid` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'SKU(所属商品)',
  `stock_num` bigint(20) DEFAULT '0' COMMENT '库存数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `delete_status` tinyint(4) DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) DEFAULT NULL COMMENT '修改时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='商品SKU库存表';

-- ----------------------------
-- Records of t_product_sku_stock
-- ----------------------------
INSERT INTO `t_product_sku_stock` VALUES ('16', '6d60a39cf4cf4bccb1159fec61ba147b', 'dfb5a0e07dc811eb85a380a5893fe2d8', '63b10f067dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('17', '6d60a39cf4cf4bccb1159fec61ba147b', 'ea042d337dc811eb85a380a5893fe2d8', '65c2b8b77dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('18', '6d60a39cf4cf4bccb1159fec61ba147b', 'eb65be307dc811eb85a380a5893fe2d8', '66e93ba77dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('19', '6d60a39cf4cf4bccb1159fec61ba147b', 'ecec8af97dc811eb85a380a5893fe2d8', '6dbc4fcc7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('20', '337c8f9148294a488e7aad2cf733632a', 'ee5a9f457dc811eb85a380a5893fe2d8', '6fb4cbcf7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('21', '337c8f9148294a488e7aad2cf733632a', '008e9e217dc911eb85a380a5893fe2d8', '7173995a7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('22', '337c8f9148294a488e7aad2cf733632a', '016c090f7dc911eb85a380a5893fe2d8', '7357066e7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('23', '337c8f9148294a488e7aad2cf733632a', '025b006f7dc911eb85a380a5893fe2d8', '74b367387dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('24', '6d60a39cf4cf4bccb1159fec61ba147b', '03bb58e77dc911eb85a380a5893fe2d8', '7823949b7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('25', '6d60a39cf4cf4bccb1159fec61ba147b', '04853b8c7dc911eb85a380a5893fe2d8', '7a010e297dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('26', '6d60a39cf4cf4bccb1159fec61ba147b', '0554c3967dc911eb85a380a5893fe2d8', '7b5817af7dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('27', '6d60a39cf4cf4bccb1159fec61ba147b', '0c12efdc7dc911eb85a380a5893fe2d8', '7c999d147dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);
INSERT INTO `t_product_sku_stock` VALUES ('28', '6d60a39cf4cf4bccb1159fec61ba147b', 'bae4588a7dc811eb85a380a5893fe2d8', '7e6f27447dbf11eb85a380a5893fe2d8', '9999', null, '10001001', '0', null, null, null, null);

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
) ENGINE=InnoDB AUTO_INCREMENT=1027 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of t_sa_admin_app
-- ----------------------------

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
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属角色',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='管理员角色关联表';

-- ----------------------------
-- Records of t_sa_admin_role
-- ----------------------------

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
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '菜单类型 0目录 1菜单 2按钮',
  `pid` bigint(20) NOT NULL COMMENT '上级菜单 -1表示当前是顶级',
  `icon` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `enable_status` tinyint(4) DEFAULT NULL COMMENT '启用状态 0:禁用 1启用',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  `update_date` datetime(6) DEFAULT NULL,
  `update_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='菜单表';

-- ----------------------------
-- Records of t_sa_function
-- ----------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of t_sa_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_sa_role_function
-- ----------------------------
DROP TABLE IF EXISTS `t_sa_role_function`;
CREATE TABLE `t_sa_role_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属角色',
  `function_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属菜单',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `create_admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建用户ID -1初始化',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of t_sa_role_function
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_main_id` bigint(20) NOT NULL COMMENT '用户主表ID',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `enable_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '启用状态 0:禁用 1启用',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户主表';

-- ----------------------------
-- Records of t_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_app
-- ----------------------------
DROP TABLE IF EXISTS `t_user_app`;
CREATE TABLE `t_user_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用 (软关联,直接存储应用名字符串)',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1003 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户应用关联';

-- ----------------------------
-- Records of t_user_app
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_user_detail`;
CREATE TABLE `t_user_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `nick_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '昵称',
  `true_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `head_sculpture` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `id_card` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证',
  `user_main_id` bigint(20) NOT NULL COMMENT '所属用户',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别 0:女 1:男',
  `type` tinyint(4) DEFAULT NULL COMMENT '用户类型 0:买家 1:卖家 2:既是买家又是卖家',
  `create_date` datetime(6) DEFAULT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户详细信息';

-- ----------------------------
-- Records of t_user_detail
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_email
-- ----------------------------
DROP TABLE IF EXISTS `t_user_email`;
CREATE TABLE `t_user_email` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱(可用于登录)',
  `user_main_id` bigint(20) NOT NULL COMMENT '用户主表ID',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户邮箱关联子表';

-- ----------------------------
-- Records of t_user_email
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_login_record
-- ----------------------------
DROP TABLE IF EXISTS `t_user_login_record`;
CREATE TABLE `t_user_login_record` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录';

-- ----------------------------
-- Records of t_user_login_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_mobile_phone
-- ----------------------------
DROP TABLE IF EXISTS `t_user_mobile_phone`;
CREATE TABLE `t_user_mobile_phone` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `mobile_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号(注册时必填)',
  `user_main_id` bigint(20) NOT NULL COMMENT '用户主表ID',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户手机号关联子表';

-- ----------------------------
-- Records of t_user_mobile_phone
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_shipping_address
-- ----------------------------
DROP TABLE IF EXISTS `t_user_shipping_address`;
CREATE TABLE `t_user_shipping_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收货地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `app_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属应用',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户收货地址';

-- ----------------------------
-- Records of t_user_shipping_address
-- ----------------------------
INSERT INTO `t_user_shipping_address` VALUES ('1', '1', 'A1的收货地址', '2021-01-04 16:13:56', null, '0', '0');
INSERT INTO `t_user_shipping_address` VALUES ('2', '2', 'A2的收货地址', '2021-01-04 16:13:56', null, '0', '0');
INSERT INTO `t_user_shipping_address` VALUES ('3', '3', 'A3的收货地址', '2021-01-04 16:13:56', null, '0', '0');
INSERT INTO `t_user_shipping_address` VALUES ('4', '4', 'A4的收货地址', '2021-01-04 16:13:56', null, '0', '0');
INSERT INTO `t_user_shipping_address` VALUES ('5', '5', 'A5的收货地址', '2021-01-04 16:13:56', null, '0', '0');

-- ----------------------------
-- Table structure for t_user_username
-- ----------------------------
DROP TABLE IF EXISTS `t_user_username`;
CREATE TABLE `t_user_username` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `user_main_id` bigint(20) NOT NULL COMMENT '用户主表ID',
  `create_date` datetime(6) NOT NULL COMMENT '创建时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户与用户名关联子表';

-- ----------------------------
-- Records of t_user_username
-- ----------------------------
