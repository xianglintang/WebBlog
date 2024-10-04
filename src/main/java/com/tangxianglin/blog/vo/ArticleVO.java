package com.tangxianglin.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleVO {
    private int articleId;
    private String title;
    private String categoryName;
    private String introduce;
    private String content;
    private int adminId;
    private int categoryId;
    private Date createdTime;
    private Date updatedTime;
}
