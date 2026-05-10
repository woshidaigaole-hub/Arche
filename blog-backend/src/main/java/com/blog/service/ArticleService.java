package com.blog.service;

import com.blog.entity.Article;
import com.blog.entity.User;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    public ArticleService(ArticleMapper articleMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
    }

    /**
     * 文章列表 —— is_top=1 的（博主文章）排前面，再按时间倒序
     */
    public List<Article> listPublished() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1)        // 只查已发布的
               .orderByDesc(Article::getIsTop)   // is_top=1 优先
               .orderByDesc(Article::getCreateTime);  // 再按时间倒序
        List<Article> articles = articleMapper.selectList(wrapper);

        // 填充作者名（联表查用户名，避免 N+1 可优化，学习阶段先这样）
        for (Article article : articles) {
            User author = userMapper.selectById(article.getAuthorId());
            if (author != null) {
                article.setAuthorName(author.getNickname() != null ? author.getNickname() : author.getUsername());
            }
        }
        return articles;
    }

    /**
     * 文章详情
     */
    public Article getById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            User author = userMapper.selectById(article.getAuthorId());
            if (author != null) {
                article.setAuthorName(author.getNickname() != null ? author.getNickname() : author.getUsername());
            }
        }
        return article;
    }

    /**
     * 发表文章
     */
    public void create(Article article) {
        articleMapper.insert(article);
    }

    /**
     * 更新文章 —— 校验是不是作者本人或管理员
     */
    public void update(Long articleId, Article updated, Long currentUserId, String currentRole) {
        Article existing = articleMapper.selectById(articleId);
        if (existing == null) {
            throw new RuntimeException("文章不存在");
        }
        // 只有作者本人或管理员可以修改
        if (!existing.getAuthorId().equals(currentUserId) && !"ADMIN".equals(currentRole)) {
            throw new RuntimeException("无权修改此文章");
        }
        updated.setId(articleId);
        articleMapper.updateById(updated);
    }
}
