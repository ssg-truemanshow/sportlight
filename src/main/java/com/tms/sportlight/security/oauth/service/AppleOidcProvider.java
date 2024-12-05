package com.tms.sportlight.security.oauth.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOidcProvider implements OidcProvider{

    @Override
    public String getProviderId(String idToken, OidcIdToken oidcIdToken) {
        return oidcIdToken.getSubject();
    }

    @Override
    public String extractEmail(Map<String, Object> claims) {
        return (String) claims.get("email");
    }

    @Override
    public String extractName(Map<String, Object> claims) {
        return (String) claims.get("nickname");
    }

    @Override
    public String getProviderName() {
        return "KAKAO";
    }

}
