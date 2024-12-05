package com.tms.sportlight.security.oauth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class OidcTokenUtil {

    /**
     * OIDC ID 토큰을 파싱하여 Claims 를 추출
     *
     * @param idToken OIDC 아이디 토큰
     * @param publicKey 프로바이더의 공개키
     * @return 파싱된 Claims
     */
    public Claims parseIdToken(String idToken, PublicKey publicKey) {
        return Jwts.parser()
            .verifyWith(publicKey)
            .build()
            .parseSignedClaims(idToken)
            .getPayload();
    }
}
