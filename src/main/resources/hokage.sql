DROP DATABASE `hokage`;
CREATE DATABASE IF NOT EXISTS `hokage` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE `hokage`;

CREATE TABLE IF NOT EXISTS `hokage_sequence` (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `gmt_create` DATETIME NOT NULL,
 `gmt_modified` DATETIME NOT NULL,
 `name` varchar(128) NOT NULL UNIQUE COMMENT '序列名称，一般对应表名',
 `value` bigint NOT NULL comment 'sequence值',
 PRIMARY KEY (`id`)
)
COMMENT 'hokage用户信息表';

INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_user', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_supervisor_subordinate', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_server', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_server_group', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_subordinate_server', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_supervisor_server', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_task_result', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_security_group', '1000');
INSERT INTO `hokage_sequence` (gmt_create, gmt_modified, name, value) VALUES (now(), now(), 'hokage_task', '1000');

CREATE TABLE IF NOT EXISTS `hokage_user` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `username` varchar(32) NOT NULL,
  `passwd` varchar(128) NOT NULL,
  `role` tinyint NOT NULL COMMENT '用户角色标识: 0: 超级管理员, 1 管理员, 2普通用户',
  `email` varchar(128) NULL,
  `is_subscribed` tinyint ZEROFILL NULL COMMENT '是否订阅, 0: 不订阅, 1: 订阅, 发送消息邮件',
  PRIMARY KEY (`id`)
)
  COMMENT 'hokage用户信息表';

CREATE TABLE IF NOT EXISTS `hokage_supervisor_subordinate` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `supervisor_id` bigint NOT NULL COMMENT '管理员id',
  `subordinate_id` bigint NOT NULL COMMENT '普通用户id',
  PRIMARY KEY (`id`)
)
  COMMENT = '管理员与用户关系映射表';

CREATE TABLE IF NOT EXISTS `hokage_server` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `hostname` varchar(128) NULL COMMENT '服务器主机名',
  `domain` varchar(128) NULL COMMENT '服务器对应域名',
  `ip` varchar(16) NOT NULL COMMENT '服务器ip',
  `ssh_port` varchar(6) NOT NULL COMMENT 'ssh链接端口',
  `account` varchar(32) NOT NULL COMMENT '登录账户',
  `passwd` varchar(32) NOT NULL COMMENT '登录密码',
  `server_group` varchar(128) NULL COMMENT '服务器分组',
  `type`  tinyint NULL COMMENT '服务器类型, 0-内网、1-外网、3-X86、4-GPU',
  `description` varchar(1024) NULL COMMENT '服务器描述',
  PRIMARY KEY (`id`)
)
  COMMENT = '服务器信息表';

CREATE TABLE IF NOT EXISTS `hokage_server_group` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `name` varchar(128) NOT NULL COMMENT '分组名称(只能是字母或者数字)',
  `description` varchar(1024) NULL COMMENT '分组信息描述',
  PRIMARY KEY (`id`)
)
  COMMENT = '服务器分组配置信息表';

CREATE TABLE IF NOT EXISTS `hokage_subordinate_server` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `subordinate_id` bigint NOT NULL COMMENT '普通用户id',
  `server_id` bigint NOT NULL COMMENT '服务器id',
  PRIMARY KEY (`id`)
)
  COMMENT = '普通用户和服务器颖映射表';

CREATE TABLE IF NOT EXISTS `hokage_supervisor_server` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `supervisor_id` bigint NOT NULL COMMENT '管理员id',
  `server_id` bigint NOT NULL COMMENT '服务器id',
  PRIMARY KEY (`id`)
)
  COMMENT = '管理员和服务器颖映射表';

CREATE TABLE IF NOT EXISTS `hokage_task` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `user_id` bigint NOT NULL COMMENT '创建用户',
  `task_name` varchar(128) NOT NULL COMMENT '任务名称',
  `task_type` tinyint ZEROFILL NOT NULL COMMENT '任务类型: 0: 默认为shell',
  `exec_type` tinyint NOT NULL COMMENT '执行类型: 0: 定时, 1: cron周期',
  `exec_time` bigint NOT NULL COMMENT '执行时间',
  `exec_servers` varchar(1024) NOT NULL COMMENT '执行服务器(服务器IP或者服务器分组)',
  `exec_command` text NOT NULL COMMENT '执行命令',
  `description` varchar(1024) NULL COMMENT '任务描述',
  PRIMARY KEY (`id`)
)
  COMMENT 'hokage批量任务表';

CREATE TABLE IF NOT EXISTS `hokage_task_result` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `task_id` bigint NOT NULL COMMENT '任务id',
  `task_status` tinyint NOT NULL COMMENT '任务执行状态: 0: 执行完成, 1: 正在执行, 2: 未知',
  `start_time` bigint NOT NULL COMMENT '任务开始执行时间',
  `end_time` bigint NULL COMMENT '执行结束时间',
  `exit_code` tinyint NULL COMMENT '任务返回状态码',
  `exec_server` varchar(128) NULL COMMENT '执行机器',
  `exec_result` text NULL COMMENT '执行返回内容',
  PRIMARY KEY (`id`)
)
  COMMENT = '批量任务结果表';

CREATE TABLE IF NOT EXISTS `hokage_security_group` (
  `id` bigint NOT NULL,
  `gmt_create` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `servers` varchar(1024) NOT NULL COMMENT '指定服务器(IP或者分组)',
  `auth_strategy` tinyint NOT NULL COMMENT '授权策略: 0-禁止, 1-允许',
  `protocol_type` tinyint NOT NULL COMMENT '协议类型: 0-tcp, 1-udp, 2-icmp, 3-all',
  `port_range` varchar(64) NOT NULL COMMENT '端口范围',
  `auth_object` varchar(1024) NOT NULL COMMENT '授权对象',
  `status` tinyint NOT NULL COMMENT '0-禁用, 1-在线',
  `description` varchar(1024) NULL COMMENT '安全组描述',
  PRIMARY KEY (`id`)
)
  COMMENT 'hokage安全组信息表';
