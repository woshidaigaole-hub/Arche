package com.blog.controller;

import com.blog.dto.LoginDto;
import com.blog.dto.RegisterDto;
import com.blog.dto.Result;
import com.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller = 门卫，只做三件事：
 *   1. 接收 HTTP 请求（@PostMapping 等）
 *   2. 调用 Service
 *   3. 用 Result 包装返回
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 注册 —— POST /api/auth/register
     * @RequestBody = 把前端传来的 JSON 自动转成 RegisterDto 对象
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDto dto) {
        try {
            userService.register(dto);
            return Result.ok("注册成功，请等待管理员审核");
        } catch (RuntimeException e) {
            return Result.fail(400, e.getMessage());
        }
    }

    /**
     * 登录 —— POST /api/auth/login
     * 成功后返回 JWT token 和用户信息
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDto dto) {
        try {
            Map<String, Object> data = userService.login(dto);
            return Result.ok(data);
        } catch (RuntimeException e) {
            return Result.fail(401, e.getMessage());
        }
    }
}
