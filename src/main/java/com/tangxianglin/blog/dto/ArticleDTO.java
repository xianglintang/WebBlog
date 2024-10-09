package com.tangxianglin.blog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class ArticleDTO {
    private int articleId;
    private String title;
    private String introduce;
    private MultipartFile file;//文件数据，到pojo那边前，转移到好的路径，然后转化为路径即可
    private int adminId;
    private int categoryId;
}
