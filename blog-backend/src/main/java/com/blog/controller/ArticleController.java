package com.blog.controller;

import com.blog.dto.Result;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 文章列表 —— GET /api/articles（公开，不需要登录）
     */
    @GetMapping
    public Result<List<Article>> list() {
        return Result.ok(articleService.listPublished());
    }

    /**
     * 文章详情 —— GET /api/articles/{id}（公开）
     * @PathVariable = 从 URL 路径里取 {id}
     */
    @GetMapping("/{id}")
    public Result<Article> detail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return Result.fail(404, "文章不存在");
        }
        return Result.ok(article);
    }

    /**
     * 发表文章 —— POST /api/articles（需登录）
     * HttpServletRequest 里存着 JwtInterceptor 放入的 userId
     */
    @PostMapping
    public Result<?> create(@RequestBody Article article, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.fail(401, "请先登录");
        }
        // 普通用户只能让自己文章在副位置，管理员(博主)可以设 is_top=1
        String role = (String) request.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            article.setIsTop(0);
        }
        article.setAuthorId(userId);
        article.setStatus(1);  // 直接发布
        articleService.create(article);
        return Result.ok("发布成功");
    }

    /**
     * 更新文章 —— PUT /api/articles/{id}（需登录，且是作者本人或管理员）
     */
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id,
                            @RequestBody Article article,
                            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        if (userId == null) {
            return Result.fail(401, "请先登录");
        }
        try {
            articleService.update(id, article, userId, role);
            return Result.ok("更新成功");
        } catch (RuntimeException e) {
            return Result.fail(403, e.getMessage());
        }
    }

    /**
     * 删除文章 —— DELETE /api/articles/{id}（需登录，且是作者本人或管理员）
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        if (userId == null) {
            return Result.fail(401, "请先登录");
        }
        try {
            articleService.delete(id, userId, role);
            return Result.ok("删除成功");
        } catch (RuntimeException e) {
            return Result.fail(403, e.getMessage());
        }
    }
}
