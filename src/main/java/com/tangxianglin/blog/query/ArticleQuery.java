package com.tangxianglin.blog.query;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleQuery {
    private int articleTd;
    private String title;
    private String categoryName;
    private String introduce;
    private String content;
    private int adminId;
    private int categoryId;
    private Date createTime;
    private Date updateTime;
}
