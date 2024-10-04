package com.tangxianglin.blog.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public interface VerificationCodeService {
    // 生成 6 位验证码
    public String generateVerificationCode();

    // 将验证码存入 Redis
    public void saveVerificationCode(String email, String code);

    // 从 Redis 中获取验证码
    //public String getVerificationCode(String Identifier);这个尽量不公开，实现类私有

    public boolean verifyCode(String inputCode, String identifier);
    // 删除验证码
    public void deleteVerificationCode(String email);

    // 生成 Redis 的键名
    //String getRedisKey(String email);
}
