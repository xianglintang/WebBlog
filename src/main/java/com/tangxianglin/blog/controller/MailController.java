package com.tangxianglin.blog.controller;

import com.tangxianglin.blog.dto.AdminDTO;
import com.tangxianglin.blog.dto.MessageDTO;
import com.tangxianglin.blog.mapper.MailMapper;

import com.tangxianglin.blog.pojo.MailMessage;
import com.tangxianglin.blog.service.MailService;
import com.tangxianglin.blog.service.VerificationCodeService;
import com.tangxianglin.blog.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@Api(tags = "留言邮件接口")
@RestController
@RequestMapping("/api/mail")
public class MailController {
    @Resource
    private MailService mailService;
    @Resource
    private MailMapper mailMapper;
    @Resource
    VerificationCodeService verificationCodeService;//验证码生成和放入redis

    @ApiOperation("发送邮件")
    @PostMapping("/send")
    public Result<String> sendMessage(@RequestBody MessageDTO messageDTO) {
        //先存入数据库，后面再发送
        MailMessage mailMessage = new MailMessage();
        mailMessage.setName(messageDTO.getName());
        mailMessage.setContact(messageDTO.getContact());
        mailMessage.setMessage(messageDTO.getMessage());
        mailMapper.InsertMailMessage(mailMessage);
        System.out.println("留言已保存数据库");
        //发送邮件
        String content = "姓名: " + messageDTO.getName() + "\n联系方式: " + messageDTO.getContact() + "\n留言: " + messageDTO.getMessage();
        mailService.sendMessageMail("新的留言", content);
        return Result.ok("留言已发送");
    }

    @ApiOperation("邮箱验证码发送")
    @PostMapping("/sendcode")
    public Result<String> sendcode(AdminDTO adminDTO){
        String email = adminDTO.getEmail();
        String identifier = adminDTO.getIdentifier();
        System.out.println("identifier：" + identifier);
        // 生成验证码
        String verificationCode = verificationCodeService.generateVerificationCode();
        System.out.println("验证码：" + verificationCode);
        // 将验证码加密后存入 Redis
        verificationCodeService.saveVerificationCode(identifier, verificationCode);

        // 发送验证码
        String content = "您的验证码为：" + verificationCode+"\n验证码有效期5分钟，请勿泄露。";
        mailService.sendCodeMail(email, "验证码", content);
        return Result.ok("验证码已发送");
    }
    @ApiOperation("验证码验证")
    @PostMapping("/verifycode")
    public Result<String> verifycode(AdminDTO adminDTO){
        String inputCode = adminDTO.getEmailCode();
        String identifier = adminDTO.getIdentifier();
        boolean result = verificationCodeService.verifyCode(inputCode, identifier);
        if (result) {
            return Result.ok("验证通过");
        } else {
            return Result.failure("验证码错误或已经过期");
        }
    }
}
