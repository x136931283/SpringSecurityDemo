/*
 Navicat Premium Data Transfer

 Source Server         : spring_security
 Source Server Type    : MySQL
 Source Server Version : 50650
 Source Host           : 106.53.126.165:3306
 Source Schema         : spring_security

 Target Server Type    : MySQL
 Target Server Version : 50650
 File Encoding         : 65001

 Date: 16/06/2022 19:01:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` char(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆账号',
  `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码-MD5加密,默认密码123456',
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '用户类型：1-系统用户,2-TMC，3-配送员;4-运输公司负责人;5-行查员;6-机场人员;8-代理商;10-子公司;11-贵宾厅核销账号;12-地铁转运人员;13-物流负责人;14-不正常行李账号',
  `authority` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标记，测试用，应使用RBAC权限模型',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户姓名',
  `sex` int(1) NULL DEFAULT 1 COMMENT '用户性别（1-男 2-女 3-未知）',
  `mob_phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `head_img` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像路径',
  `role_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '关联角色ID',
  `status` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '帐号状态：1-正常； 0-停用；',
  `is_disable` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否禁用：1-正常：0-禁用',
  `c_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$mNKxfvLLAohP5MloVCzqjeFNKllLSrlGoDfsPobHOomvLE3Qqjzb6', '1', 'ROLE_admin,ROLE_normal,sys:user:list', '小田', 1, '', '', '', '', '1', NULL, '2022-06-15 17:10:08');
INSERT INTO `sys_user` VALUES (13, 'txy123456', '$2a$10$3ERVxoHbC7ffFDbF0Aqc0OUEkQBcovdpDZh50Lactgl8dE/GAVqqm', '1', 'admin,resource:get,resource:remove1,resource:set', '小田技术博客', 1, '', '', '', '', '1', NULL, '2022-06-16 16:24:32');

SET FOREIGN_KEY_CHECKS = 1;
