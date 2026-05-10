package com.blog.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类 —— 核心就两个操作：生成 token（登录时）、解析 token（每次请求时）
 *
 * JWT 结构（三段 Base64 用 . 连起来）：
 *   Header.载荷.签名
 *   载荷里存 userId、username 等不敏感信息
 *   签名用来防篡改 —— 任何人改了载荷，签名就对不上了
 */
@Component
public class   JwtUtil {

    private final SecretKey key;
    private final long expiration;

    /**
     * 构造函数：从 application.yml 读配置
     * @Value = 把 yml 里的值注入到变量
     */
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        // 密钥至少256位(32字节)，不够就报错
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    /**
     * 生成 token —— 登录成功后调用
     * @param userId   用户ID
     * @param username 用户名
     * @return JWT 字符串
     */
    public String generateToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(Map.of("userId", userId, "username", username, "role", role))  // 载荷
                .issuedAt(now)          // 签发时间
                .expiration(expireDate)  // 过期时间
                .signWith(key)           // 用密钥签名
                .compact();              // 压缩成字符串
    }

    /**
     * 解析 token —— 每次请求时调用，验证 token 是否有效
     * @return 载荷里的 claims，解析失败返回 null
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            return null;  // token 无效/过期/被篡改
        }
    }
}
