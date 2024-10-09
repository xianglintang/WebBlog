package com.tangxianglin.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleVO {
    private String fileName;
    private int articleId;
    private String title;
    private String categoryName;
    private String introduce;
    private String content;
    private String fileNoHtml;
    private int adminId;
    private int categoryId;
    private Date createdTime;
    private Date updatedTime;
    //目录和内容
    private String htmltoc;       // 目录的 HTML
    private String htmlcontent;   // 文章的 HTML
}
