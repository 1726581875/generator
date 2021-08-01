DROP DATABASE IF EXISTS `fool_code`;
-- 创建数据库
CREATE DATABASE `fool_code`;
-- 进入数据库
use `fool_code`;

-- 创建数据源表
DROP TABLE IF EXISTS `fool_data_source`;
CREATE TABLE `fool_data_source`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`         varchar(20)     NOT NULL COMMENT '数据源名称',
    `user_id`   bigint             NOT NULL COMMENT '所属用户id',
	`host_name` varchar(200)  NOT NULL COMMENT '主机名或者ip地址',
	`port`  int  NOT NULL COMMENT '端口',
    `source_url`  varchar(200) COMMENT '数据源url',
    `user_name` varchar(100) NOT NULL COMMENT '数据库连接用户名',
    `password` varchar(400) NOT NULL COMMENT '数据库连接密码',
    `status`       tinyint         NOT NULL DEFAULT 1 COMMENT '状态|1正常、2已删除',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_url` (`source_url`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='数据源表';

insert into `fool_data_source` (`name`,`user_id`,`host_name`,`port`,`source_url`,`user_name`,`password`)
values ('测试数据源1',1,'127.0.0.1',3306,'testurl1','admin','admin'),
       ('测试数据源2',1,'127.0.0.1',3306,'testurl2','admin','admin'),
       ('测试数据源3',1,'127.0.0.1',3306,'testurl3','admin','admin'),
       ('测试数据源4',1,'127.0.0.1',3306,'testurl4','admin','admin'),
       ('测试数据源5',1,'127.0.0.1',3306,'testurl5','admin','admin');

-- 创建数据库表
DROP TABLE IF EXISTS `fool_database`;
CREATE TABLE `fool_database`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`         varchar(200)     NOT NULL COMMENT '数据库名',
    `source_id`   bigint             NOT NULL COMMENT '所属数据源id',
    `status`       tinyint         NOT NULL DEFAULT 1 COMMENT '状态|1正常、2已删除',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='数据库表';

insert into `fool_database` (`name`,`source_id`)
values
('fool_code',1),
('mooc',1),
('test',1),
('fool_big',2);

-- 创建表描述表
DROP TABLE IF EXISTS `fool_table`;
CREATE TABLE `fool_table`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`         varchar(200)     NOT NULL COMMENT '表名',
    `database_id`   bigint           NOT NULL COMMENT '所属数据库id',
    `table_comment`  varchar(200) COMMENT '表备注',
    `status`       tinyint         NOT NULL DEFAULT 1 COMMENT '状态|1正常、2已删除',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_database_id_and_name` (`database_id`,`name`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='表描述表';
  
-- 创建字段表
DROP TABLE IF EXISTS `fool_field`;
CREATE TABLE `fool_field`
(
    `id`           bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`         varchar(200)     NOT NULL COMMENT '字段名',
	`key_type` varchar(20)     DEFAULT NULL COMMENT '键类型|pri主键、uk唯一键',
    `table_id`   bigint             NOT NULL COMMENT '所属数据库表id',
    `field_comment`  varchar(200) NOT NULL COMMENT '字段备注',
	`field_type` varchar(20) NOT NULL COMMENT '字段类型',
	`field_length` int NOT NULL COMMENT '字段长度',
    `status`       tinyint         NOT NULL DEFAULT 1 COMMENT '状态|1正常、2已删除',
    `create_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_table_id_and_name` (`table_id`,`name`)
) ENGINE = INNODB
  DEFAULT charset = utf8mb4 COMMENT ='字段表';
  
