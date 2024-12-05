package com.tms.sportlight.security.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import java.net.URL;
import java.security.Signature;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PublicKeyProvider {

    private static final String KAKAO_PUBLIC_KEY_URL = "https://kauth.kakao.com/.well-known/jwks.json";
    private static final String NAVER_PUBLIC_KEY_URL = "https://openapi.naver.com/oauth2.0/jwks";
    private static final String GOOGLE_PUBLIC_KEY_URL = "https://www.googleapis.com/oauth2/v3/certs";
    private final ObjectMapper objectMapper;


    /**
     * 카카오 공개 키 가져오기
     *
     * @return 카카오 공개 키 목록
     */
    public OidcPublicKeyList getKakaoKeys() {
        return fetchKeys(KAKAO_PUBLIC_KEY_URL);
    }

    /**
     * 네이버 공개 키 가져오기
     *
     * @return 네이버 공개 키 목록
     */
    public OidcPublicKeyList getNaverKeys() {
        return fetchKeys(NAVER_PUBLIC_KEY_URL);
    }

    /**
     * 구글 공개 키 가져오기
     *
     * @return 구글 공개 키 목록
     */
    public OidcPublicKeyList getGoogleKeys() { // Google 관련 메서드 추가
        return fetchKeys(GOOGLE_PUBLIC_KEY_URL);
    }


    /**
     * 공개 키 목록 가져오기
     *
     * @param url 공개 키 URL
     * @return 공개 키 목록
     */
    private OidcPublicKeyList fetchKeys(String url) {
        try {
            return objectMapper.readValue(new URL(url), OidcPublicKeyList.class);
        } catch (Exception e) {
            throw new RuntimeException("공개키 불러오기 실패", e);
        }
    }

    /**
     * 공개 키 생성
     *
     * @param idToken ID 토큰
     * @param publicKeys 공개 키 목록
     * @return 생성된 공개 키
     */
    public PublicKey generatePublicKey(String idToken, OidcPublicKeyList publicKeys) {
        Map<String, String> headers = parseHeaders(idToken);
        String kid = headers.get("kid");
        String alg = headers.get("alg");

        OidcPublicKey key = publicKeys.getKeys().stream()
            .filter(k -> k.getKid().equals(kid) && k.getAlg().equals(alg))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No matching public key found"));

        return createPublicKey(key);
    }

    /**
     * RSA 공개 키 생성
     *
     * @param key 공개 키 데이터
     * @return RSA 공개 키
     */
    private PublicKey createPublicKey(OidcPublicKey key) {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());
            RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("공개키 생성 실패", e);
        }
    }

    /**
     * ID 토큰 헤더 파싱
     *
     * @param token ID 토큰
     * @return 파싱된 헤더 정보
     */
    private Map<String, String> parseHeaders(String token) {
        String header = token.split("\\.")[0];
        byte[] decoded = Base64.getUrlDecoder().decode(header);
        try {
            return objectMapper.readValue(decoded, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("JWT 헤더 파싱 실패", e);
        }
    }

    /**
     * RSA 서명 검증
     *
     * @param idToken JWT 토큰
     * @param publicKey 공개 키
     * @return 검증 성공 여부
     */
    public boolean verifyIdToken(String idToken, PublicKey publicKey) {
        try {
            String[] tokenParts = idToken.split("\\.");
            if (tokenParts.length != 3) {
                throw new BizException(ErrorCode.INVALID_JWT_FORMAT);
            }

            String headerPayload = tokenParts[0] + "." + tokenParts[1];
            byte[] signature = Base64.getUrlDecoder().decode(tokenParts[2]);

            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(headerPayload.getBytes());

            return verifier.verify(signature);
        } catch (IllegalArgumentException e) {
            throw new BizException(ErrorCode.INVALID_JWT_FORMAT);
        } catch (Exception e) {
            throw new BizException(ErrorCode.NOT_VALID_ID_TOKEN);
        }
    }
}
