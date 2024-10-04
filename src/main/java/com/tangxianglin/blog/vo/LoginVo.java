package com.tangxianglin.blog.vo;

import lombok.Data;

@Data
public class LoginVo {
    private String userName;
    private String passWord;
    private String nonceStr;
    private String value;
    private Boolean remember;

}
