-- 在 MySQL 中先执行：CREATE DATABASE blog_db CHARACTER SET utf8mb4;
-- 然后执行本文件建表

CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `username`    VARCHAR(32)  NOT NULL UNIQUE COMMENT '用户名',
    `password`    VARCHAR(128) NOT NULL COMMENT 'BCrypt加密后的密码',
    `nickname`    VARCHAR(32)  DEFAULT NULL COMMENT '昵称',
    `role`        VARCHAR(16)  NOT NULL DEFAULT 'USER' COMMENT 'USER=普通用户, ADMIN=管理员',
    `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0=待审核, 1=正常, 2=禁用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `article` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(128) NOT NULL COMMENT '标题',
    `summary`     VARCHAR(256) DEFAULT '' COMMENT '摘要',
    `content`     LONGTEXT     NOT NULL COMMENT '正文(Markdown)',
    `author_id`   BIGINT       NOT NULL COMMENT '作者ID',
    `is_top`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0=副位置, 1=主位置(你的文章)',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '0=草稿, 1=已发布',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_author` (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 默认管理员（密码是 123456 的 BCrypt 加密值）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`) VALUES
('admin', '$2b$10$dAIN4lybP5o0xoG08ehd7u.Q2S3VY2DN/xb9MIvB/muM5uCM110Fe', '博主', 'ADMIN', 1);
