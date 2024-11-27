package com.tms.sportlight.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey secretKey;

    private RedisTemplate<String, String> stringRedisTemplate;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate =stringRedisTemplate;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
            SIG.HS256.key().build()
                .getAlgorithm());
    }

    public String getUsername(String token) {
        return getClaims(token).get("loginId", String.class);
    }


    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    public Boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public String createJwt(String loginId, List<String> roles, Long expiredMS) {
        return Jwts.builder()
            .claim("loginId", loginId)
            .claim("roles", roles)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMS))
            .signWith(secretKey)
            .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String createRefreshToken(String loginId, Long expiredMS) {
        return Jwts.builder()
            .claim("loginId", loginId)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMS))
            .signWith(secretKey)
            .compact();
    }

    public void storeRefreshToken(String loginId, String refreshToken, Long expiration) {
        String hashedToken = hashToken(refreshToken);
        stringRedisTemplate.opsForValue().set(loginId, hashedToken, expiration, TimeUnit.MILLISECONDS);

    }


    public boolean validateRefreshToken(String loginId, String refreshToken) {
        String storedHashedToken = stringRedisTemplate.opsForValue().get(loginId);
        if (storedHashedToken == null) {
            return false;
        }

        String currentHashedToken = hashToken(refreshToken);

        return storedHashedToken.equals(currentHashedToken);
    }


    public void deleteRefreshToken(String loginId) {
        stringRedisTemplate.delete(loginId);
    }



    public String hashToken(String token) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(token);
    }

}
