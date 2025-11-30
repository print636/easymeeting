-- 创建 meeting_chat_message 主表
CREATE TABLE IF NOT EXISTS `meeting_chat_message` (
  `message_id` BIGINT(16) NOT NULL COMMENT '消息ID',
  `meeting_id` VARCHAR(10) NOT NULL COMMENT '会议ID',
  `message_type` TINYINT(1) DEFAULT NULL COMMENT '消息类型',
  `message_content` VARCHAR(500) DEFAULT NULL COMMENT '消息内容',
  `send_user_id` VARCHAR(12) DEFAULT NULL COMMENT '发送用户ID',
  `send_user_nick_name` VARCHAR(20) DEFAULT NULL COMMENT '发送用户昵称',
  `send_time` BIGINT(20) DEFAULT NULL COMMENT '发送时间',
  `receive_type` TINYINT(1) DEFAULT NULL COMMENT '接收类型',
  `receive_user_id` VARCHAR(12) DEFAULT NULL COMMENT '接收用户ID',
  `file_size` BIGINT(20) DEFAULT NULL COMMENT '文件大小',
  `file_name` VARCHAR(200) DEFAULT NULL COMMENT '文件名',
  `file_type` TINYINT(1) DEFAULT NULL COMMENT '文件类型',
  `file_suffix` VARCHAR(10) DEFAULT NULL COMMENT '文件后缀',
  `status` TINYINT(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议聊天消息表';

-- 创建 32 张分表
CREATE TABLE IF NOT EXISTS `meeting_chat_message_01` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_02` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_03` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_04` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_05` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_06` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_07` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_08` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_09` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_10` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_11` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_12` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_13` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_14` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_15` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_16` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_17` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_18` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_19` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_20` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_21` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_22` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_23` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_24` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_25` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_26` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_27` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_28` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_29` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_30` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_31` LIKE `meeting_chat_message`;
CREATE TABLE IF NOT EXISTS `meeting_chat_message_32` LIKE `meeting_chat_message`;

