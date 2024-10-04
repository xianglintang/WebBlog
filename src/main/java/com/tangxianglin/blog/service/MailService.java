package com.tangxianglin.blog.service;

//该接口是为了留言自动发邮件，已经邮箱验证登录
public interface MailService {
    public void sendMessageMail(String subject, String content);

    public void sendCodeMail(String toEmail, String subject, String content);
}
