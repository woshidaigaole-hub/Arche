package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication = 三合一注解：
 *   @Configuration + @EnableAutoConfiguration + @ComponentScan
 *   告诉 Spring Boot："从这个类所在目录开始扫描所有组件"
 *
 * @MapperScan = 告诉 MyBatis-Plus 去哪找 Mapper 接口
 */
@SpringBootApplication
@MapperScan("com.blog.mapper")
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
