package com.tangxianglin.blog.controller;



import com.tangxianglin.blog.dto.AdminDTO;
import com.tangxianglin.blog.exception.ParamException;
import com.tangxianglin.blog.pojo.Admin;
import com.tangxianglin.blog.properties.JWTProperties;
import com.tangxianglin.blog.service.AdminService;
import com.tangxianglin.blog.service.MailService;
import com.tangxianglin.blog.service.VerificationCodeService;
import com.tangxianglin.blog.utils.JWTUtil;
import com.tangxianglin.blog.utils.Result;
import com.tangxianglin.blog.utils.StringUtil;
import com.tangxianglin.blog.vo.AdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "管理员接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    JWTProperties jwtProperties;

    @Resource
    AdminService adminService;

    @Resource
    VerificationCodeService verificationCodeService;

    @ApiOperation("密码登录")
    @PostMapping("/login")
    public Result<List<AdminVO>> login(AdminDTO adminDTO){
        //String adminName, String adminPassword
        String adminName = adminDTO.getAdminName();
        String adminPassword = adminDTO.getAdminPassword();
        System.out.println("adminName:"+adminName+"，adminPassword:"+adminPassword);
        //参数的校验
        if(StringUtil.isEmpty(adminName) || StringUtil.isEmpty(adminPassword)){
            throw new ParamException("账号和密码都不能为空");
        }

        AdminVO adminVO = adminService.login(adminName,adminPassword);

        if(adminVO==null){
            return Result.failure("账号或密码错误");
        }

        //服务器生成token --> 响应给前端----->前端每次发请求都携带token------->服务器要校验

        Map<String,Object> payload = new HashMap<>();
        payload.put(JWTUtil.ADMIN_ID,adminVO.getAdminId());

        String token = JWTUtil.generateToken(payload,jwtProperties.getSecure(),jwtProperties.getExpire());

        Map<String,Object> data = new HashMap<>();
        data.put("token",token);
        data.put("adminInfo",adminVO);

        return Result.ok("成功",data);
    }

    @ApiOperation("邮箱验证")
    @PostMapping("/verifycode")
    public Result<List<AdminVO>> verifycode(AdminDTO adminDTO){//先验证验证码是否对
        //String adminName, String adminPassword
        String Identifier = adminDTO.getIdentifier();
        String verificationCode = adminDTO.getEmailCode();
        String email = adminDTO.getEmail();
        System.out.println("Identifier:"+Identifier);
        System.out.println("verificationCode:"+verificationCode);
        //参数的校验
        if(StringUtil.isEmpty(verificationCode)){
            throw new ParamException("验证码不能为空");//可以Result.failure，但是会错误引导，后端出错没收到，前端以为数据为空但实际不空，
        }
        //验证验证码
        if(!verificationCodeService.verifyCode(verificationCode , Identifier)){
            return Result.failure("验证码错误或者已过期");
        }

        //验证码通过即可开始生成token
        AdminVO adminVO = adminService.loginByEmail(email);

        if(adminVO==null){
            return Result.failure("邮箱不存在");
        }

        //服务器生成token --> 响应给前端----->前端每次发请求都携带token------->服务器要校验

        Map<String,Object> payload = new HashMap<>();
        payload.put(JWTUtil.ADMIN_ID,adminVO.getAdminId());

        String token = JWTUtil.generateToken(payload,jwtProperties.getSecure(),jwtProperties.getExpire());

        Map<String,Object> data = new HashMap<>();
        data.put("token",token);
        data.put("adminInfo",adminVO);

        return Result.ok("成功",data);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result<String> register(AdminDTO adminDTO){
        //检测账号和邮箱是否已存在
        if(adminService.nameIsExist(adminDTO.getAdminName())!=null){
            return Result.failure("账号已存在");
        }
        if(adminService.emailIsExist(adminDTO.getEmail())!=null){
            return Result.failure("邮箱已存在");
        }
        //然后再检验邮箱验证码
        String Identifier = adminDTO.getIdentifier();
        String verificationCode = adminDTO.getEmailCode();
        //参数的校验
        if(StringUtil.isEmpty(verificationCode)){
            throw new ParamException("验证码不能为空");//可以Result.failure，但是会错误引导，后端出错没收到，前端以为数据为空但实际不空，
        }
        //验证验证码
        if(!verificationCodeService.verifyCode(verificationCode , Identifier)){
            return Result.failure("验证码错误或者已过期");
        }

        Admin admin = new Admin();
        admin.setAdminName(adminDTO.getAdminName());
        admin.setAdminPassword(adminDTO.getAdminPassword());
        admin.setEmail(adminDTO.getEmail());
        System.out.println("adminName:"+admin.getAdminName()+"，adminPassword:"+admin.getAdminPassword());
        //参数的校验
        if(StringUtil.isEmpty(admin.getAdminName()) || StringUtil.isEmpty(admin.getAdminPassword()) || StringUtil.isEmpty(admin.getEmail())){
            throw new ParamException("账号和密码和邮箱都不能为空");
        }
        adminService.registerAdmin(admin);
        return Result.ok("注册成功");
    }
    @ApiOperation("账号是否已注册")
    @PostMapping("/register/adminNameIsExist")
    public Result<AdminVO> nameIsExist(String adminName){//@RequestParam默认处理请求参数，导致json无法解析，因为默认请求application/x-www-form-urlencoded
        System.out.println("adminName:"+adminName);
        AdminVO adminVO = adminService.nameIsExist(adminName);
        if(adminVO!=null){
            adminVO.setAdminNameIsExist(true);
            return Result.ok("已注册",adminVO);
        }
        else{
            adminVO = new AdminVO();
            return Result.ok("可注册",adminVO);
        }
    }
    @ApiOperation("邮箱是否已注册")
    @PostMapping("/register/emailIsExist")
    public Result<AdminVO> emailIsExist(String email){
        System.out.println("email:"+email);
        AdminVO adminVO = adminService.emailIsExist(email);
        if(adminVO!=null){
            adminVO.setEmailIsExist(true);
            return Result.ok("已注册",adminVO);
        }
        else{
            adminVO = new AdminVO();
            return Result.ok("可注册",adminVO);
        }
    }
}
