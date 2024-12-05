package com.tms.sportlight.security.oauth.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OidcProviderFactory {

    private final Map<String, OidcProvider> providers;

    public OidcProviderFactory(
        GoogleOidcProvider googleProvider,
        KakaoOidcProvider kakaoProvider,
        NaverOidcProvider naverProvider) {

        this.providers = Map.of(
            "google", googleProvider,
            "kakao", kakaoProvider,
            "naver", naverProvider
        );
    }

    public OidcProvider getProvider(String providerName) {
        OidcProvider provider = providers.get(providerName.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported provider: " + providerName);
        }
        return provider;
    }
}
