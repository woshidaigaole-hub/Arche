package com.blog.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) = 专门接收前端传来的请求体
 * 和 Entity 的区别：Entity 映射数据库表，DTO 只用于传输，字段可以完全不同
 */
@Data
public class LoginDto {
    private String username;
    private String password;
}
