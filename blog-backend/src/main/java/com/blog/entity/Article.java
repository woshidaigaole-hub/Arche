package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String summary;
    private String content;       // Markdown 原文，前端用 marked.js 渲染
    private Long authorId;
    private Integer isTop;        // 1=主位置(博主文章置顶), 0=副位置
    private Integer status;       // 0=草稿, 1=已发布

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 以下字段不在数据库里，是查询时关联填充的
    @TableField(exist = false)
    private String authorName;    // 作者名（联表查出来展示用）
}
