package com.tangxianglin.blog;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

public class MailTest {

    public static void main(String[] args) {
        //outlook老是出错，无法生成专用密码
        /*JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //mailSender.setHost("smtp.office365.com");//旧版
        mailSender.setHost("smtp-mail.outlook.com");//新版
        mailSender.setPort(587);
        mailSender.setUsername("xianglintang@outlook.com");
        mailSender.setPassword("KVKG9-93XEW-6EZB6-WEWT5-Q3ZKP");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");  // 开启调试模式，查看详细日志（可选）

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("xianglintang@outlook.com");
        message.setTo("xianglintang@outlook.com");
        message.setSubject("Test Mail");
        message.setText("This is a test mail.");*/

        //qq邮箱,校园网不可以，发邮件慢
        // 创建邮件发送器
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 设置 QQ 邮箱 SMTP 服务器的主机地址和端口
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(587);

        // 设置发送方的 QQ 邮箱地址和授权码（注意不是邮箱密码）
        mailSender.setUsername("*******@qq.com");
        mailSender.setPassword("*****");  // 替换为你的授权码

        // 设置邮件的其他属性
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");// STARTTLS
        //props.put("mail.smtp.ssl.enable", "true"); // 启用 SSL
        props.put("mail.smtp.ssl.trust", "smtp.qq.com");
        //props.put("mail.debug", "true");  // 开启调试模式，查看详细日志（可选）
        props.put("mail.smtp.connectiontimeout", "5000");  // 5秒连接超时
        props.put("mail.smtp.timeout", "5000");  // 5秒读超时
        props.put("mail.smtp.writetimeout", "5000");  // 5秒写超时

        // 创建邮件内容
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2280793613@qq.com");         // 发件人
        message.setTo("xianglintang@outlook.com");     // 收件人
        message.setSubject("Test Mail from QQ");       // 邮件主题
        message.setText("This is a test mail from QQ mailbox.");  // 邮件正文
        try {
            // 发送邮件
            mailSender.send(message);
            System.out.println("Mail sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send mail.");
        }
    }
}
