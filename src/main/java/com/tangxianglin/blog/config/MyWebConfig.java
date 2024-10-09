package com.tangxianglin.blog.config;


import com.tangxianglin.blog.interceptor.AdminLoginInterceptor;
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
public class MyWebConfig extends WebMvcConfigurationSupport {

    @Resource
    AdminLoginInterceptor adminLoginInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns()为拦截哪些路径，"/"就是全部不拦截，excludePathPatterns是不拦截的路径，/admin/**代表admin路径后面下全部
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login","/admin/verifycode","/admin/register","/admin/register/**");
    }

    @Bean
    public FormContentFilter createFormContentFilter(){
        return new FormContentFilter();
    }

    @Bean
    public Docket createDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
            .title("在线写作平台接口文档admin")
            .version("1.0")
            .description("个人博客项目接口文档admin")
            .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端接口")
            .apiInfo(apiInfo)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.tangxianglin.blog.controller"))
            .paths(PathSelectors.any())
            .build();
        return docket;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*
         * addMapping：配置可以被跨域的路径，可以任意配置，可以具体到直接请求路径。
         * allowCredentials：是否开启Cookie
         * allowedMethods：允许的请求方式，如：POST、GET、PUT、DELETE等。
         * allowedOrigins：允许访问的url，可以固定单条或者多条内容
         * allowedHeaders：允许的请求header，可以自定义设置任意请求头信息。
         * maxAge：配置预检请求的有效时间
         */

        registry.addMapping("/**")
                .allowedOrigins("*")
                //.allowCredentials(true)  //cookie
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);  //如果每次请求都加载了token, options
    }
}