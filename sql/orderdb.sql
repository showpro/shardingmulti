/*
 Navicat Premium Data Transfer

 Source Server         : localhost3306
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : localhost:3306
 Source Schema         : orderdb

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 25/11/2025 20:26:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for orderinfo
-- ----------------------------
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo`  (
  `orderId` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `orderSn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '编号',
  `orderTime` timestamp(0) NOT NULL DEFAULT '1971-01-01 00:00:01' COMMENT '下单时间',
  `orderStatus` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态:0,未支付，1，已支付，2，已发货，3，已退货,4,已过期',
  `userId` int(12) NOT NULL DEFAULT 0 COMMENT '用户id',
  `price` decimal(10, 0) NOT NULL COMMENT '价格',
  `addressId` int(12) NOT NULL DEFAULT 0 COMMENT '地址',
  PRIMARY KEY (`orderId`) USING BTREE,
  UNIQUE INDEX `orderSn`(`orderSn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderinfo
-- ----------------------------
INSERT INTO `orderinfo` VALUES (1, '20200814171411660', '2020-08-14 09:14:12', 0, 8, 100, 0);

SET FOREIGN_KEY_CHECKS = 1;
