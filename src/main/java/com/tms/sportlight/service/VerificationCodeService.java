package com.tms.sportlight.service;

import com.tms.sportlight.security.util.VerificationCodeGenerator;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int VERIFICATION_CODE_TTL = 180;
    private static final int LOGIN_ID_TTL = 300;

    public String generateAndSaveCode(String loginId) {
        String code = VerificationCodeGenerator.generateCode();
        redisTemplate.opsForValue().set(getVerificationCodeKey(loginId), code, VERIFICATION_CODE_TTL, TimeUnit.SECONDS);
        return code;
    }

    public boolean verifyCode(String loginId, String inputCode) {
        String key = getVerificationCodeKey(loginId);
        String storedCode = redisTemplate.opsForValue().get(key);
        return storedCode != null && storedCode.equals(inputCode);
    }

    public void disableCode(String loginId) {
        redisTemplate.delete(getVerificationCodeKey(loginId));
    }

    public void saveLoginId(String loginId) {
        redisTemplate.opsForValue().set(getLoginIdKey(), loginId, LOGIN_ID_TTL, TimeUnit.SECONDS);
    }

    public String getLoginId() {
        return redisTemplate.opsForValue().get(getLoginIdKey());
    }

    public void deleteLoginId() {
        redisTemplate.delete(getLoginIdKey());
    }

    private String getVerificationCodeKey(String loginId) {
        return "verification:" + loginId;
    }

    private String getLoginIdKey() {
        return "authenticatedLoginId";
    }
}

