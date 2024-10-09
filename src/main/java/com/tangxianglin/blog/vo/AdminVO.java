package com.tangxianglin.blog.vo;

import lombok.Data;

@Data
public class AdminVO {
    private int adminId;
    private String adminName;
    private String adminPassword;
    private String email;//邮箱验证码不在这里，而在redis
    private String identifier;//用户标识符
    //注册用的,默认都不存在
    private boolean adminNameIsExist = false;//账号是否已存在
    private boolean emailIsExist = false;//账号是否已存在


}
