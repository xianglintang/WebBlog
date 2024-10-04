package com.tangxianglin.blog.dto;

import lombok.Data;

@Data
public class AdminDTO {
    private String adminName;
    private String adminPassword;
    private String email;//邮箱验证码不在这里，而在redis
    private String identifier;//用户标识符
    private String emailCode;//前端传来的邮件验证码
}
