package com.tangxianglin.blog.service.impl;

import com.tangxianglin.blog.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//该类，将邮件验证码放入redis
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final int EXPIRE_TIME = 5; // 验证码有效期为 5 分钟

    // 生成 6 位验证码
    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 生成 100000 到 999999 的随机数
        return String.valueOf(code);
    }

    // 将加密后的验证码保存到 Redis 中
    public void saveVerificationCode(String identifier, String verificationCode) {
        String encryptedCode  = encryptCode(verificationCode);
        redisTemplate.opsForValue().set(identifier, encryptedCode , EXPIRE_TIME, TimeUnit.MINUTES);
    }

    // 从 Redis 通过键 获取加密后的验证码
    private String getVerificationCode(String identifier) {
        return (String) redisTemplate.opsForValue().get(identifier); // 从 Redis 中获取
    }
    // 校验用户输入的验证码是否正确
    public boolean verifyCode(String inputCode, String identifier) {
        String encryptedInputCode = encryptCode(inputCode); // 加密用户输入的验证码
        return encryptedInputCode.equals(getVerificationCode(identifier)); // 对比加密后的验证码是否一致
    }

    // 删除 Redis 中的验证码
    public void deleteVerificationCode(String identifier) {
        redisTemplate.delete(identifier);
    }

    // 生成 Redis 的键名
    private String encryptCode(String code) {
        return DigestUtils.md5DigestAsHex(code.getBytes(StandardCharsets.UTF_8)); // 使用 MD5 对验证码进行加密
    }
}
