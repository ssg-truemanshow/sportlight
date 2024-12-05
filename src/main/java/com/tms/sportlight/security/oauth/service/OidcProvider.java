package com.tms.sportlight.security.oauth.service;

import java.util.Map;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

public interface OidcProvider {

    /**
     * OIDC ID Token에서 제공자별 고유 ID를 추출
     *
     * @param idToken 원본 ID 토큰 문자열
     * @param oidcIdToken 파싱된 OIDC ID 토큰
     * @return 제공자 ID
     */
    String getProviderId(String idToken, OidcIdToken oidcIdToken);

    /**
     * 제공자별 이메일 정보 추출
     *
     * @param claims ID 토큰의 클레임
     * @return 이메일 주소 (없을 경우 null)
     */
    String extractEmail(Map<String, Object> claims);

    /**
     * 제공자별 이름 정보 추출
     *
     * @param claims ID 토큰의 클레임
     * @return 사용자 이름 (없을 경우 null)
     */
    String extractName(Map<String, Object> claims);

    /**
     * 프로바이더 이름 반환
     *
     * @return 프로바이더 이름 (예: "KAKAO", "NAVER", "GOOGLE")
     */
    String getProviderName();

}
