package com.tangxianglin.blog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tangxianglin.blog.mapper.AdminMapper;
import com.tangxianglin.blog.pojo.Admin;
import com.tangxianglin.blog.service.AdminService;
import com.tangxianglin.blog.utils.PageResult;
import com.tangxianglin.blog.vo.AdminVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Value("${tom.salt}")
    private String salt;

    @Resource
    AdminMapper adminMapper;

    @Override
    public AdminVO login(String adminName, String adminPassword) {
        adminPassword = DigestUtils.md5DigestAsHex((adminPassword+salt).getBytes()); //MD5
        return adminMapper.login(adminName,adminPassword);
    }

    @Override
    public AdminVO loginByEmail(String email) {
        return adminMapper.loginByEmail(email);
    }

    @Override
    public void registerAdmin(Admin admin) {
        admin.setAdminPassword(DigestUtils.md5DigestAsHex((admin.getAdminPassword()+salt).getBytes()));
        adminMapper.registerAdmin(admin);
    }
    @Override
    public AdminVO emailIsExist(String email) {
        return adminMapper.emailIsExist(email);
    }
    @Override
    public AdminVO nameIsExist(String adminName) {
        return adminMapper.nameIsExist(adminName);
    }
}
