package com.tms.sportlight.security.oauth.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String socialId;
    private String email;
    private String nickname;
    private String name;
    private String provider;

    public static OAuthAttributes of(String provider,
        String userNameAttributeName,
        Map<String, Object> attributes) {
        if ("kakao".equals(provider)) {
            return ofKakao(userNameAttributeName, attributes);
        } else if ("naver".equals(provider)) {
            return ofNaver(userNameAttributeName, attributes);
        } else if ("google".equals(provider)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 프로마이더입니다. : " + provider);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
        Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount =
            (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile =
            (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
            .socialId(String.valueOf(attributes.get("id")))
            .nickname((String) profile.get("nickname"))
            .provider("kakao")
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
        Map<String, Object> attributes) {
        Map<String, Object> response =
            (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
            .socialId((String) response.get("id"))
            .email((String) response.get("email"))
            .nickname((String) response.get("nickname"))
            .name((String) response.get("name"))
            .provider("naver")
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .socialId((String) attributes.get("sub"))
            .email((String) attributes.get("email"))
            .name((String) attributes.get("name"))
            .provider("google")
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }
}
