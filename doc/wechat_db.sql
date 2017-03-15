/*
SQLyog Ultimate v8.4 
MySQL - 5.6.24-log : Database - wechat_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wechat_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wechat_db`;

/*Table structure for table `auto_reply_info` */

DROP TABLE IF EXISTS `auto_reply_info`;

CREATE TABLE `auto_reply_info` (
  `auto_reply_id` int(100) NOT NULL AUTO_INCREMENT,
  `auto_reply_key_word` varchar(100) NOT NULL DEFAULT '',
  `MsgType` varchar(100) NOT NULL DEFAULT '',
  `Content` text NOT NULL,
  `Title` varchar(100) NOT NULL DEFAULT '',
  `Description` varchar(100) NOT NULL DEFAULT '',
  `PicUrl` varchar(100) NOT NULL DEFAULT '',
  `Url` varchar(100) NOT NULL DEFAULT '',
  `auto_reply_type` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`auto_reply_id`)
) ENGINE=MyISAM AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

/*Data for the table `auto_reply_info` */

/*Table structure for table `customer_info` */

DROP TABLE IF EXISTS `customer_info`;

CREATE TABLE `customer_info` (
  `customer_id` int(100) NOT NULL AUTO_INCREMENT,
  `customer_openid` varchar(100) NOT NULL DEFAULT '',
  `customer_subscribe` varchar(100) NOT NULL DEFAULT '',
  `customer_nickname` varchar(100) NOT NULL DEFAULT '',
  `customer_sex` varchar(100) NOT NULL DEFAULT '',
  `customer_language` varchar(100) NOT NULL DEFAULT '',
  `customer_city` varchar(100) NOT NULL DEFAULT '',
  `customer_province` varchar(100) NOT NULL DEFAULT '',
  `customer_country` varchar(100) NOT NULL DEFAULT '',
  `customer_headimgurl` varchar(1000) NOT NULL DEFAULT '',
  `customer_remark` varchar(100) NOT NULL DEFAULT '',
  `customer_subscribe_time` varchar(100) NOT NULL DEFAULT '',
  `customer_group_id` int(100) NOT NULL DEFAULT '1',
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=426689 DEFAULT CHARSET=utf8;

/*Data for the table `customer_info` */

insert  into `customer_info`(`customer_id`,`customer_openid`,`customer_subscribe`,`customer_nickname`,`customer_sex`,`customer_language`,`customer_city`,`customer_province`,`customer_country`,`customer_headimgurl`,`customer_remark`,`customer_subscribe_time`,`customer_group_id`) values (426685,'','','','','','','','','','','2016-02-24 13:29:24',1),(426686,'','','','','','','','','','','2016-02-24 13:35:02',1),(426687,'','','','','','','','','','','2016-02-24 13:36:41',1),(426688,'','','','','','','','','','','2016-02-24 13:39:53',1);

/*Table structure for table `data` */

DROP TABLE IF EXISTS `data`;

CREATE TABLE `data` (
  `date` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `data` */

/*Table structure for table `group_info` */

DROP TABLE IF EXISTS `group_info`;

CREATE TABLE `group_info` (
  `group_id` int(100) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) NOT NULL,
  `group_count` varchar(100) NOT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `group_info` */

/*Table structure for table `media_info` */

DROP TABLE IF EXISTS `media_info`;

CREATE TABLE `media_info` (
  `media_id` int(100) NOT NULL AUTO_INCREMENT,
  `media_media_id` varchar(1000) NOT NULL DEFAULT '',
  `media_name` varchar(100) NOT NULL DEFAULT '',
  `media_data` longblob NOT NULL,
  `media_update_time` varchar(100) NOT NULL DEFAULT '',
  `media_type` varchar(100) NOT NULL DEFAULT '',
  `media_group` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `media_info` */

/*Table structure for table `message_info` */

DROP TABLE IF EXISTS `message_info`;

CREATE TABLE `message_info` (
  `message_id` int(100) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `message_msg_id` varchar(100) DEFAULT NULL,
  `message_text` text,
  `message_type` varchar(10) DEFAULT NULL,
  `message_image_url` varchar(1000) DEFAULT NULL,
  `message_image` longblob,
  `message_audio` longblob,
  `message_video` longblob,
  `message_short_video` longblob,
  `message_thumb_media` longblob,
  `message_media_id` varchar(100) DEFAULT NULL,
  `message_thumb_media_id` varchar(100) DEFAULT NULL,
  `message_format` varchar(100) DEFAULT NULL,
  `message_location_x` varchar(100) DEFAULT NULL,
  `message_location_y` varchar(100) DEFAULT NULL,
  `message_scale` varchar(100) DEFAULT NULL,
  `message_label` varchar(100) DEFAULT NULL,
  `message_title` varchar(100) DEFAULT NULL,
  `message_description` varchar(100) DEFAULT NULL,
  `message_url` varchar(1000) DEFAULT NULL,
  `message_revice_time` varchar(50) DEFAULT NULL,
  `message_create_time` varchar(50) DEFAULT NULL,
  `message_status` varchar(50) NOT NULL DEFAULT '0' COMMENT '0：未读；1：已读',
  `message_to_user_name` varchar(50) DEFAULT NULL,
  `message_from_user_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28825 DEFAULT CHARSET=utf8;

/*Data for the table `message_info` */

insert  into `message_info`(`message_id`,`message_msg_id`,`message_text`,`message_type`,`message_image_url`,`message_image`,`message_audio`,`message_video`,`message_short_video`,`message_thumb_media`,`message_media_id`,`message_thumb_media_id`,`message_format`,`message_location_x`,`message_location_y`,`message_scale`,`message_label`,`message_title`,`message_description`,`message_url`,`message_revice_time`,`message_create_time`,`message_status`,`message_to_user_name`,`message_from_user_name`) values (28810,'6254725749325264879','youhaha','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:30:19','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28811,'6254726037088073724','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:31:25','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28812,'6254726994865780814','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:35:08','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28813,'6254727033520486481','kfg','voice',NULL,NULL,'{\"errcode\":48001,\"errmsg\":\"api unauthorized hint: [007ldA0121e297]\"}',NULL,NULL,NULL,'6Ad6Dx545n2mIWKVc5Me-K7lWTFj8fpqjSZKr3UqnVoPq-QSmRau9Lk0_t9SEZm6',NULL,'amr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:35:17','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28814,'6254728279061002413','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:40:07','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28815,'6254728605478516930','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-03-10 09:32:35','2016-02-24 13:41:23','1','8037','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28816,'6254728661313091782','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:41:36','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28817,'6254728781572176080','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:42:04','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28818,'6254729146644396266','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:43:29','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28819,'6254729146644396266','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:44:20','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28820,'6254727033520486481','kfg','voice',NULL,NULL,'{\"errcode\":48001,\"errmsg\":\"api unauthorized hint: [8fDzsA0703e298]\"}',NULL,NULL,NULL,'6Ad6Dx545n2mIWKVc5Me-K7lWTFj8fpqjSZKr3UqnVoPq-QSmRau9Lk0_t9SEZm6',NULL,'amr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:44:59','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28821,'6254727033520486481','kfg','voice',NULL,NULL,'{\"errcode\":48001,\"errmsg\":\"api unauthorized hint: [p5SzwA0810e297]\"}',NULL,NULL,NULL,'6Ad6Dx545n2mIWKVc5Me-K7lWTFj8fpqjSZKr3UqnVoPq-QSmRau9Lk0_t9SEZm6',NULL,'amr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:46:47','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28822,'6254727033520486481','kfg','voice',NULL,NULL,'{\"errcode\":48001,\"errmsg\":\"api unauthorized hint: [g0glCA0821e292]\"}',NULL,NULL,NULL,'6Ad6Dx545n2mIWKVc5Me-K7lWTFj8fpqjSZKr3UqnVoPq-QSmRau9Lk0_t9SEZm6',NULL,'amr',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:46:57','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28823,'6254733321352608164','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 13:59:42','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA'),(28824,'6254737453111147130','kfg','text',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2016-02-24 14:15:43','0','gh_b3869567a1b7','o73mFv-riWPU9Zixk_2_R6OSquuA');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `birthday` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '出身日期',
  `area` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '出身地',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`birthday`,`area`) values (1,'小红','2017-02-01 00:00:00.000000','莆田'),(7,'34','1998-11-05 04:12:12.000000','dddd'),(9,'34','1998-11-05 04:12:12.000000','dddd'),(10,'7','1992-12-12 23:12:12.000000','asdf'),(11,'小青','1991-09-10 09:08:11.000000','香港'),(12,'笑笑','1989-12-10 10:01:05.000000','sadf'),(13,'笑笑','1989-12-10 10:01:05.000000','sadf');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
