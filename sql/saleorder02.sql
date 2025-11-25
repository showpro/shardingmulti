/*
 Navicat Premium Data Transfer

 Source Server         : localhost3306
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : localhost:3306
 Source Schema         : saleorder02

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 25/11/2025 20:28:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order_3
-- ----------------------------
DROP TABLE IF EXISTS `t_order_3`;
CREATE TABLE `t_order_3`  (
  `orderId` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goodsName` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'name',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'order3' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_3
-- ----------------------------

-- ----------------------------
-- Table structure for t_order_4
-- ----------------------------
DROP TABLE IF EXISTS `t_order_4`;
CREATE TABLE `t_order_4`  (
  `orderId` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goodsName` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'name',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'order4' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_order_4
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
