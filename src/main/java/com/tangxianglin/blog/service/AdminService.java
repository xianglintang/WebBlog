package com.tangxianglin.blog.service;


import com.tangxianglin.blog.pojo.Admin;
import com.tangxianglin.blog.vo.AdminVO;

public interface AdminService {

    AdminVO login(String adminName, String adminPassword);
    AdminVO loginByEmail(String email);
    void registerAdmin(Admin admin);
    AdminVO nameIsExist(String adminName);
    AdminVO emailIsExist(String email);
}
