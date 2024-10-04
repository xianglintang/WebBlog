package com.tangxianglin.blog.service.impl;

import com.tangxianglin.blog.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

//发邮件单独封装成类
@Service
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    @Override //留言
    public void sendMessageMail(String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(fromEmail);//收件人和发件人相同
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        System.out.println(content);
        System.out.println("留言已发送");
    }

    @Async
    @Override //验证码
    public void sendCodeMail(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        System.out.println(content);
        System.out.println("验证码已发送");
    }

}
