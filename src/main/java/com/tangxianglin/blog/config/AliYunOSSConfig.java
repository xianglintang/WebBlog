package com.tangxianglin.blog.config;


import com.tangxianglin.blog.properties.OSSProperties;
import com.tangxianglin.blog.utils.AliYunOSSUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

@Configuration
public class AliYunOSSConfig {


    //创建AliYunOSSUtil对象交给容器来管理



    @Bean
    public AliYunOSSUtil createAliYunOSSUtil(OSSProperties ossProperties){

        AliYunOSSUtil aliYunOSSUtil = new AliYunOSSUtil(ossProperties.getEndpoint(),ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret(),ossProperties.getBucketName());
        return aliYunOSSUtil;
    }

}