package com.tangxianglin.blog.controller;

import com.tangxianglin.blog.utils.AliYunOSSUtil;
import com.tangxianglin.blog.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/file")
public class FileController {

    @Resource
    AliYunOSSUtil aliYunOSSUtil;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();  //11.png

        String subfix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-","")+subfix;//随机名

        String result = aliYunOSSUtil.upload(file.getBytes(),newFileName);

        return Result.ok("上传成功",result);
    }
}
