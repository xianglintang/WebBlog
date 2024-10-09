package com.tangxianglin.blog.mapper;

import com.tangxianglin.blog.pojo.Article;
import com.tangxianglin.blog.query.ArticleQuery;
import com.tangxianglin.blog.vo.AdminVO;
import com.tangxianglin.blog.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //搜索，这个可能要分页，也可能不要分页
    List<ArticleVO> Search(ArticleQuery articleQuery);
    //那几个类型的tab-pane，这个不用分页
    List<ArticleVO> SearchBycategoryId(@Param("categoryId") long categoryId);

    ArticleVO SearchById(@Param("articleId") long articleId);

    void AddNewArticle(Article article);

    void UpdateArticle(Article article);
}
