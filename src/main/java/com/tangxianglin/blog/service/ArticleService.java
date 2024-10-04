package com.tangxianglin.blog.service;


import com.tangxianglin.blog.pojo.Article;
import com.tangxianglin.blog.query.ArticleQuery;
import com.tangxianglin.blog.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleService {
    //搜索，这个可能要分页，也可能不要分页,先不分页
    List<ArticleVO> Search(ArticleQuery articleQuery);
    //根据id查找
    String SearchById(long articleId);

    //那几个类型的tab-pane，这个不用分页
    List<ArticleVO> SearchBycategoryId(long categoryId);

    void AddNewArticle(Article article);
}
