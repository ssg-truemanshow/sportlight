package com.tms.sportlight.security.oauth.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverOidcProvider implements OidcProvider {

    @Override
    public String getProviderId(String idToken, OidcIdToken oidcIdToken) {
        Map<String, Object> claims = oidcIdToken.getClaims();
        Map<String, Object> response = (Map<String, Object>) claims.get("response");
        return response != null ? (String) response.get("id") : null;
    }

    @Override
    public String extractEmail(Map<String, Object> claims) {
        Map<String, Object> response = (Map<String, Object>) claims.get("response");
        return response != null ? (String) response.get("email") : null;
    }

    @Override
    public String extractName(Map<String, Object> claims) {
        Map<String, Object> response = (Map<String, Object>) claims.get("response");
        return response != null ? (String) response.get("name") : null;
    }

    @Override
    public String getProviderName() {
        return "NAVER";
    }
}