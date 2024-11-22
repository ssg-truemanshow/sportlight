package com.tms.sportlight.service;

import com.tms.sportlight.security.util.VerificationCodeGenerator;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private static final int VERIFICATION_CODE_TTL = 180;
    private static final int LOGIN_ID_TTL = 300;

    public VerificationCodeService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String generateAndSaveCode(String loginId) {
        String code = VerificationCodeGenerator.generateCode();
        stringRedisTemplate.opsForValue().set(getVerificationCodeKey(loginId), code, VERIFICATION_CODE_TTL, TimeUnit.SECONDS);
        return code;
    }

    public boolean verifyCode(String loginId, String inputCode) {
        String key = getVerificationCodeKey(loginId);
        String storedCode = stringRedisTemplate.opsForValue().get(key);
        return storedCode != null && storedCode.equals(inputCode);
    }

    public void disableCode(String loginId) {
        stringRedisTemplate.delete(getVerificationCodeKey(loginId));
    }

    public void saveLoginId(String loginId) {
        stringRedisTemplate.opsForValue().set(getLoginIdKey(), loginId, LOGIN_ID_TTL, TimeUnit.SECONDS);
    }

    public String getLoginId() {
        return stringRedisTemplate.opsForValue().get(getLoginIdKey());
    }

    public void deleteLoginId() {
        stringRedisTemplate.delete(getLoginIdKey());
    }

    private String getVerificationCodeKey(String loginId) {
        return "verification:" + loginId;
    }

    private String getLoginIdKey() {
        return "authenticatedLoginId";
    }
}

