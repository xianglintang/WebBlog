package com.tangxianglin.blog.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private int articleId;
    private String title;
    private String categoryName;
    private String introduce;
    private String content;//存文件名，或者处理过的文件名
    private int adminId;
    private int categoryId;
    private Date createdTime;
    private Date updatedTime;


}
