package com.tangxianglin.blog.mapper;

import com.tangxianglin.blog.vo.AdminVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    AdminVO login(@Param("adminName") String adminName, @Param("adminPassword") String adminPassword);

    AdminVO loginByEmail(@Param("email") String email);//验证码验证后，通过邮箱查找到用户信息从而登录
}
