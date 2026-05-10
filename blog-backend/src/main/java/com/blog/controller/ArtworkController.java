package com.blog.controller;

import com.blog.dto.Result;
import com.blog.entity.Artwork;
import com.blog.service.ArtworkService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    // 获取全部画作（公开访问）
    @GetMapping
    public Result list() {
        return Result.ok(artworkService.list());
    }

    // 上传画作（需登录）
    @PostMapping
    public Result upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("title") String title,
                         @RequestParam("createDate") String createDate,
                         @RequestAttribute(value = "userId", required = false) Long userId) {
        if (userId == null) {
            return Result.fail(401, "请先登录");
        }
        if (file.isEmpty()) {
            return Result.fail(400, "请选择图片");
        }

        try {
            Artwork artwork = artworkService.upload(file, title, LocalDate.parse(createDate));
            return Result.ok(artwork);
        } catch (IOException e) {
            return Result.fail(500, "图片保存失败: " + e.getMessage());
        }
    }
}
