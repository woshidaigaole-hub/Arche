package com.blog.service;

import com.blog.config.JwtUtil;
import com.blog.dto.LoginDto;
import com.blog.dto.RegisterDto;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Service = 业务逻辑层（大脑）
 * 所有"判断"都在这里，Controller 只负责接请求和调 Service
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 注册 —— 注意：注册后 status=0（待审核），需要管理员在后台通过
     */
    public void register(RegisterDto dto) {
        // 查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));  // BCrypt 加密，不存明文
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setRole("USER");
        user.setStatus(1);  // 直接激活
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    /**
     * 登录 —— 返回 JWT token
     */
    public Map<String, Object> login(LoginDto dto) {
        // 1. 查用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(wrapper);

        // 2. 验密码（BCrypt 的 matches 方法比明文对比安全）
        if (user == null || !encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 3. 检查账号状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号待审核，请等待管理员通过");
        }
        if (user.getStatus() == 2) {
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 生成 token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 5. 返回 token + 用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("role", user.getRole());
        return result;
    }
}
