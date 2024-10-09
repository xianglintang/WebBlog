package com.tangxianglin.blog.service.impl;

import com.tangxianglin.blog.mapper.AdminMapper;
import com.tangxianglin.blog.mapper.ArticleMapper;
import com.tangxianglin.blog.pojo.Article;
import com.tangxianglin.blog.query.ArticleQuery;
import com.tangxianglin.blog.service.AdminService;
import com.tangxianglin.blog.service.ArticleService;
import com.tangxianglin.blog.vo.AdminVO;
import com.tangxianglin.blog.vo.ArticleVO;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
//相关md转html包
/*import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;*/
//最新md转html
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    ArticleMapper articleMapper;


    @Override
    public List<ArticleVO> Search(ArticleQuery articleQuery) {
        return articleMapper.Search(articleQuery);
    }

    @Override
    public List<ArticleVO> SearchBycategoryId(long categoryId) {
        return articleMapper.SearchBycategoryId(categoryId);
    }

    //以下重点，md文件转html
    //md转字节流
    public String getMarkdownContent(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
    //功能说明:
    //使用Flexmark库，将Markdown文本转换为HTML。
    //创建Parser实例解析Markdown内容，生成抽象语法树（AST）表示文档结构。
    //使用HtmlRenderer实例将AST渲染为HTML字符串。
    //目的: 使Markdown格式的内容可以在网页上以HTML形式显示，支持样式和结构。
    public String parseMarkdownToHtml(String markdownContent) {
        /*
        //旧版，不支持表格
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
        */
        // 启用表格扩展
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));

        // 创建解析器和渲染器，启用扩展
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdownContent);

        return renderer.render(document);
    }
    @Override
    public String SearchById(long articleId) {
        ArticleVO articleVO = articleMapper.SearchById(articleId);
        //通过content里面的路径解析其中md文件
        if (articleVO != null) {
            String markdownContent = null;
            try {
                markdownContent = getMarkdownContent(articleVO.getContent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return parseMarkdownToHtml(markdownContent);
        } else {
            return "Article not found";
        }
    }
    @Override
    public ArticleVO GetMarkdownContentByIdNoHTML(long articleId) {
        ArticleVO articleVO = articleMapper.SearchById(articleId);
        String pathcontent = articleVO.getContent();
        //通过content里面的路径解析其中md文件
        Path path = Paths.get(pathcontent);
        articleVO.setFileName(path.getFileName().toString());//先获取文件名，前端修改的时候用来名字保持不变

        try {
            articleVO.setFileNoHtml(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return articleVO;
    }

    @Override
    public void AddNewArticle(Article article) {
        articleMapper.AddNewArticle(article);
    }
    @Override
    public void UpdateArticle(Article article) {
        articleMapper.UpdateArticle(article);
    }
}
