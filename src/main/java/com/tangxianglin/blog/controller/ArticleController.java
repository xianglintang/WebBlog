package com.tangxianglin.blog.controller;


import com.tangxianglin.blog.dto.ArticleDTO;
import com.tangxianglin.blog.pojo.Article;
import com.tangxianglin.blog.properties.JWTProperties;
import com.tangxianglin.blog.query.ArticleQuery;
import com.tangxianglin.blog.service.ArticleService;
import com.tangxianglin.blog.utils.Result;
import com.tangxianglin.blog.vo.ArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    JWTProperties jwtProperties;

    String FinalFilePath = "D:/aaa/";

    @Resource
    ArticleService articleService;

    //如果为空，也是可以的，就输出全部文章即可；如果不为空，那么就模糊查询，如果没有，那么就显示没有。
    @ApiOperation("查找全部")//模糊和全部查询并存，由前端带参数，网址有参数，则相应mapper.xml里面激活where标签中的模糊查询
    @GetMapping()
    public Result<List<ArticleVO>> Search(ArticleQuery articleQuery){
        List<ArticleVO> articleVO = articleService.Search(articleQuery);
        return Result.ok("查找成功",articleVO);
    }

    @ApiOperation("根据id查找")
    @GetMapping("/{articleId}")
    public Result<String> SearchById(@PathVariable long articleId){
        String articleHTML = articleService.SearchById(articleId);
        return Result.ok("根据id查找成功",articleHTML);
    }
    @ApiOperation("类型查找")
    @GetMapping("/category/{categoryId}")
    public Result<List<ArticleVO>> SearchByCategory_Id(@PathVariable long categoryId){
        List<ArticleVO> articleVO = articleService.SearchBycategoryId(categoryId);
        return Result.ok("根据类型查找成功",articleVO);
    }
    @ApiOperation("文章上传")
    @PostMapping("/uploadArticle")
    //@Async  // 异步处理文件上传,尽量别异步，不然临时文件会被删除，导致bug
    public Result<String> UploadArticle(ArticleDTO articleDTO) throws IOException {
        System.out.println("articleDTO:"+articleDTO);
        // 1. 获取当前时间
        Date now = new Date();
        System.out.println("LocalDateTimenowtime:"+now);
        // 2. 定义文件存储路径，保存文件
        String filePath = FinalFilePath + articleDTO.getFile().getOriginalFilename();
        File dest = new File(filePath);
        System.out.println("filePath:"+filePath);
        System.out.println("dest是否存在:"+dest.exists());
        if(!dest.exists()){//一般都不存在，所以创建
            dest.mkdirs();
        }
        articleDTO.getFile().transferTo(dest);

        // 3. 将创建时间和更新时间保存到数据库（假设有Article实体类）
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setIntroduce(articleDTO.getIntroduce());
        article.setAdminId(articleDTO.getAdminId());
        article.setCategoryId(articleDTO.getCategoryId());
        article.setContent(filePath);
        article.setCreatedTime(now);;
        article.setUpdatedTime(now);

        // 保存到数据库（假设有articleService来保存文章）
        articleService.AddNewArticle(article);

        return Result.ok("上传成功");
    }
}
