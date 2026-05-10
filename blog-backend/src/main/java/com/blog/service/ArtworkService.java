package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.entity.Artwork;
import com.blog.mapper.ArtworkMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ArtworkService {

    private final ArtworkMapper artworkMapper;

    @Value("${app.upload-dir:./uploads/}")
    private String uploadDir;

    public ArtworkService(ArtworkMapper artworkMapper) {
        this.artworkMapper = artworkMapper;
    }

    // 上传画作：保存图片到本地 → 写入数据库
    public Artwork upload(MultipartFile file, String title, LocalDate createDate) throws IOException {
        // 确保 uploads 目录存在
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 用 UUID 重命名，避免文件名冲突
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + extension;

        // 写入磁盘
        Path dest = uploadPath.resolve(newFileName);
        file.transferTo(dest);

        // 存数据库记录
        Artwork artwork = new Artwork();
        artwork.setTitle(title);
        artwork.setCreateDate(createDate);
        artwork.setImagePath("/uploads/" + newFileName);
        artworkMapper.insert(artwork);

        return artwork;
    }

    // 按创作日期倒序排列
    public List<Artwork> list() {
        LambdaQueryWrapper<Artwork> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Artwork::getCreateDate);
        return artworkMapper.selectList(wrapper);
    }
}
