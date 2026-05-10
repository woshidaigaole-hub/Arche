package com.blog.dto;

import lombok.Data;

/**
 * 统一返回格式 —— 前端收到的永远是 { code, message, data } 结构
 * 好处：前端只需写一套解析逻辑，不用每个接口猜格式
 */
@Data
public class Result<T> {
    private int code;       // 200=成功, 其他=各种错误
    private String message; // 提示信息
    private T data;         // 实际数据，泛型 T 可以是 Article、List<User> 等任意类型

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ---------- 静态工厂方法，调用时很简洁 ----------

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
