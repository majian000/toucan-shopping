/*
 Navicat Premium Data Transfer

 Source Server         : rds
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : rm-2ze33642t0ju9a897oo.mysql.rds.aliyuncs.com:3306
 Source Schema         : toucan_shopping_column

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 15/04/2021 17:47:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_column
-- ----------------------------
DROP TABLE IF EXISTS `t_column`;
CREATE TABLE `t_column`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `type` smallint(6) NULL DEFAULT NULL COMMENT '类型 1:pc端 2:移动端',
  `column_sort` bigint(20) NULL DEFAULT NULL COMMENT '栏目排序',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '栏目内容',
  `show_status` tinyint(4) NULL DEFAULT NULL COMMENT '显示状态 0隐藏 1显示',
  `position` smallint(6) NULL DEFAULT NULL COMMENT '栏目位置 1首页',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column
-- ----------------------------
INSERT INTO `t_column` VALUES (1, '生鲜栏目', 1, 1, '<!--Begin 进口 生鲜 Begin-->\r\n    <div class=\"i_t mar_10\">\r\n        <span class=\"floor_num\">1F</span>\r\n        <span class=\"fl\">进口 <b>·</b> 生鲜</span>\r\n        <span class=\"i_mores fr\"><a href=\"#\">进口咖啡</a>&nbsp; &nbsp; &nbsp; <a href=\"#\">进口酒</a>&nbsp; &nbsp; &nbsp; <a href=\"#\">进口母婴</a>&nbsp; &nbsp; &nbsp; <a href=\"#\">新鲜蔬菜</a>&nbsp; &nbsp; &nbsp; <a href=\"#\">新鲜水果</a></span>\r\n    </div>\r\n    <div class=\"content\">\r\n        <div class=\"fresh_left\">\r\n            <div class=\"fre_ban\">\r\n                <div id=\"imgPlay1\">\r\n                    <ul class=\"imgs\" id=\"actor1\">\r\n                        <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_r.jpg\" width=\"211\" height=\"286\" /></a></li>\r\n                        <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_r.jpg\" width=\"211\" height=\"286\" /></a></li>\r\n                        <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_r.jpg\" width=\"211\" height=\"286\" /></a></li>\r\n                    </ul>\r\n                    <div class=\"prevf\">上一张</div>\r\n                    <div class=\"nextf\">下一张</div>\r\n                </div>\r\n            </div>\r\n            <div class=\"fresh_txt\">\r\n                <div class=\"fresh_txt_c\">\r\n                    <a href=\"#\">进口水果</a><a href=\"#\">奇异果</a><a href=\"#\">西柚</a><a href=\"#\">海鲜水产</a><a href=\"#\">品质牛肉</a><a href=\"#\">奶粉</a><a href=\"#\">鲜活禽蛋</a><a href=\"#\">进口酒</a><a href=\"#\">进口奶粉</a><a href=\"#\">鲜活禽蛋</a>\r\n                </div>\r\n            </div>\r\n        </div>\r\n        <div class=\"fresh_mid\">\r\n            <ul>\r\n                <li>\r\n                    <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\r\n                    <div class=\"price\">\r\n                        <font>￥<span>198.00</span></font> &nbsp; 26R\r\n                    </div>\r\n                    <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_1.jpg\" width=\"185\" height=\"155\" /></a></div>\r\n                </li>\r\n                <li>\r\n                    <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\r\n                    <div class=\"price\">\r\n                        <font>￥<span>198.00</span></font> &nbsp; 26R\r\n                    </div>\r\n                    <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_2.jpg\" width=\"185\" height=\"155\" /></a></div>\r\n                </li>\r\n                <li>\r\n                    <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\r\n                    <div class=\"price\">\r\n                        <font>￥<span>198.00</span></font> &nbsp; 26R\r\n                    </div>\r\n                    <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_3.jpg\" width=\"185\" height=\"155\" /></a></div>\r\n                </li>\r\n                <li>\r\n                    <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\r\n                    <div class=\"price\">\r\n                        <font>￥<span>198.00</span></font> &nbsp; 26R\r\n                    </div>\r\n                    <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_4.jpg\" width=\"185\" height=\"155\" /></a></div>\r\n                </li>\r\n                <li>\r\n                    <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\r\n                    <div class=\"price\">\r\n                        <font>￥<span>198.00</span></font> &nbsp; 26R\r\n                    </div>\r\n                    <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_5.jpg\" width=\"185\" height=\"155\" /></a></div>\r\n                </li>\r\n                <li>\r\n                    <div class=\"name\"><a href=\"#\">新鲜美味  进口美食</a></div>\r\n                    <div class=\"price\">\r\n                        <font>￥<span>198.00</span></font> &nbsp; 26R\r\n                    </div>\r\n                    <div class=\"img\"><a href=\"#\"><img src=\"${basePath}/static/images/fre_6.jpg\" width=\"185\" height=\"155\" /></a></div>\r\n                </li>\r\n            </ul>\r\n        </div>\r\n        <div class=\"fresh_right\">\r\n            <ul>\r\n                <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_b1.jpg\" width=\"260\" height=\"220\" /></a></li>\r\n                <li><a href=\"#\"><img src=\"${basePath}/static/images/fre_b2.jpg\" width=\"260\" height=\"220\" /></a></li>\r\n            </ul>\r\n        </div>\r\n    </div>\r\n    <!--End 进口 生鲜 End-->', 1, 1, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_column_area
-- ----------------------------
DROP TABLE IF EXISTS `t_column_area`;
CREATE TABLE `t_column_area`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column_id` bigint(20) NOT NULL COMMENT '栏目主键',
  `area_code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区编码',
  `create_date` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(6) NULL DEFAULT NULL COMMENT '修改时间',
  `create_admin_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `update_admin_id` bigint(20) NULL DEFAULT NULL COMMENT '修改人ID',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '栏目地区表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_column_area
-- ----------------------------
INSERT INTO `t_column_area` VALUES (1, 1, '110000', '2021-04-15 17:41:29.000000', NULL, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
