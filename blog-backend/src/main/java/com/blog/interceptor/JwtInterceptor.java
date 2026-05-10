package com.blog.interceptor;

import com.blog.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器 —— 在请求到达 Controller 之前先验 token
 * 相当于门卫：有合法 token 才放行，没有就返回 401
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 放行 OPTIONS 预检请求（浏览器跨域时会先发 OPTIONS 探测）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头拿 token：Authorization: Bearer xxx
        // 无 token 时不拦截，交给 Controller 自己判断权限
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            if (claims != null) {
                request.setAttribute("userId", claims.get("userId", Long.class));
                request.setAttribute("username", claims.get("username", String.class));
                request.setAttribute("role", claims.get("role", String.class));
            }
        }

        return true;
    }
}
