package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 实体类 = 一张表的 Java 映射
 * @Data = Lombok 自动生成 getter/setter/toString，不用手写
 * @TableName = 指定对应哪张数据库表
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)  // 主键，数据库自增
    private Long id;
    private String username;
    private String password;      // BCrypt 加密存储，不是明文
    private String nickname;
    private String role;          // USER 或 ADMIN
    private Integer status;       // 0待审核 1正常 2禁用

    @TableField(fill = FieldFill.INSERT)  // 插入时自动填充
    private LocalDateTime createTime;
}
