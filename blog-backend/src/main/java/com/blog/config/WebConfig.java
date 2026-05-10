package com.blog.config;

import com.blog.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 注册拦截器 + 跨域配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")           // 拦截所有 /api/ 开头的请求
                .excludePathPatterns(                  // 但这些路径不需要登录：
                        "/api/auth/login",             //   登录接口
                        "/api/auth/register"            //   注册接口
                );
        // 注意：GET /api/articles 放行，但 POST/PUT /api/articles 在 Controller 里单独验
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // Vue 开发服务器的地址
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
