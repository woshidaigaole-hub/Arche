package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("artwork")
public class Artwork {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private LocalDate createDate;  // 画作的实际创作日期
    private String imagePath;      // 图片存储路径，如 /uploads/xxx.jpg

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;  // 上传时间，自动填充
}
