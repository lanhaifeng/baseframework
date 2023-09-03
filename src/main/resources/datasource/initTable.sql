-- create database
create database baseframework default character set utf8 collate utf8_general_ci;
-- create user
create user 'admin'@'%' identified by 'admin';
-- grant privileges
grant select,insert,update,delete,create on baseframework.* to admin;
FLUSH PRIVILEGES;
-- grant all privileges
grant ALL on baseframework.* to admin;

FLUSH PRIVILEGES;

-- cmd backup database
-- backup databases
-- mysqldump -uroot --databases baseframework > ackup.sql

-- restore databases
-- mysql -uroot -proot soc < socbackup.sql

-- backup tables
-- mysqldump -uroot -proot soc base_user>base_user.sql

-- restore tables
-- mysqldump -uroot -proot soc base_user<base_user.sql

-- create table base_user;
CREATE TABLE base_user(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);

-- create table base_user_role;
CREATE TABLE base_user_role(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `role_id` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);

-- create table base_role;
CREATE TABLE base_role(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL,
  `desc` varchar(100) DEFAULT NULL,
  `mark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- create table base_menu;
CREATE TABLE base_menu(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(500) NOT NULL,
  `seq` int(10) NOT NULL,
  `parent_id` int(10),
  `name` varchar(500) NOT NULL,
  `icon` varchar(500),
  `order` varchar(500) NOT NULL DEFAULT 0,
  `is_leaf` tinyint  NOT NULL DEFAULT 0,
  `mark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- create table base_role_menu;
CREATE TABLE base_role_menu(
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(10) NOT NULL,
  `menu_id` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE oper_log(
    `ID` INT(12) NOT NULL AUTO_INCREMENT COMMENT '日志编号',
    `LOG_MODULE` VARCHAR(50)  COMMENT '操作模块',
    `USER_ID` INT(11) NOT NULL  COMMENT '用户编号',
    `LOG_ACTION` VARCHAR(50)   COMMENT  '操作行为',
    `LOG_RESULT` VARCHAR(20) DEFAULT NULL COMMENT '请求结果',
    `LOG_TIME` TIMESTAMP  COMMENT  '请求时间',
    PRIMARY KEY (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8;