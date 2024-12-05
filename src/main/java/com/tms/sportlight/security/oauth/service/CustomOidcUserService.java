package com.tms.sportlight.security.oauth.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.security.oauth.dto.AdditionalSocialUserInfoDTO;
import com.tms.sportlight.util.JWTUtil;
import java.security.PublicKey;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRepository userRepository;
    private final OidcProviderFactory oidcProviderFactory;
    private final PublicKeyProvider publicKeyProvider;
    private final SocialUserRegistrationService socialUserRegistrationService;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            String providerName = userRequest.getClientRegistration().getRegistrationId();
            log.debug("OAuth Provider Name: {}", providerName);

            if (!("kakao".equalsIgnoreCase(providerName) || "google".equalsIgnoreCase(providerName))) {
                throw new BizException(ErrorCode.INVALID_SOCIAL_PROVIDER);
            }

            OidcProvider provider = oidcProviderFactory.getProvider(providerName);
            String idToken = userRequest.getIdToken().getTokenValue();
            OidcIdToken oidcIdToken = userRequest.getIdToken();
            Map<String, Object> claims = oidcIdToken.getClaims();

            validateIdToken(providerName, idToken);

            String socialId = oidcIdToken.getSubject();
            String email = provider.extractEmail(claims);
            String name = provider.extractName(claims);

            User user = processUserRegistration(socialId, email, name, provider.getProviderName());
            return createOidcUser(user, oidcIdToken);

        } catch (BizException e) {
            log.error("소셜 로그인 처리 중 비즈니스 예외 발생", e);
            throw e;
        } catch (Exception e) {
            log.error("소셜 로그인 실패", e);
            throw new OAuth2AuthenticationException(
                new OAuth2Error("authentication_error"),
                e.getMessage()
            );
        }
    }

    private User processUserRegistration(String socialId, String email, String name, String provider) {
        return userRepository.findBySocialIdAndJoinMethod(socialId, provider)
            .orElseGet(() -> socialUserRegistrationService.createNewUser(socialId, email, name, provider));
    }

    private void validateIdToken(String provider, String idToken) {
        OidcPublicKeyList publicKeyList = getPublicKeyList(provider);
        PublicKey publicKey = publicKeyProvider.generatePublicKey(idToken, publicKeyList);

        if (!publicKeyProvider.verifyIdToken(idToken, publicKey)) {
            throw new BizException(ErrorCode.NOT_VALID_ID_TOKEN);
        }
    }

    private String generateTempEmail(String socialId, OidcProvider provider) {
        return String.format("temp_%s_%s@temp.com", provider.getProviderName().toLowerCase(), socialId);
    }

    private DefaultOidcUser createOidcUser(User user, OidcIdToken oidcIdToken) {
        return new DefaultOidcUser(
            user.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> role.name())
                .collect(Collectors.toList()),
            oidcIdToken
        );
    }

    @Transactional
    public void updateAdditionalInfo(Long userId, AdditionalSocialUserInfoDTO additionalInfo) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));

        if (!user.isRequiresAdditionalInfo()) {
            throw new BizException(ErrorCode.INVALID_REQUEST);
        }

        if (!user.getLoginId().equals(additionalInfo.getLoginId()) &&
            userRepository.existsByLoginId(additionalInfo.getLoginId())) {
            throw new BizException(ErrorCode.DUPLICATE_USERNAME);
        }

        validateAdditionalInfo(additionalInfo);

        user.updateSocialUserInfo(
            additionalInfo.getUserName(),
            additionalInfo.getUserPhone(),
            additionalInfo.getUserGender(),
            additionalInfo.getUserBirth(),
            additionalInfo.getLoginId(),
            additionalInfo.getTermsAgreement(),
            additionalInfo.getMarketingAgreement(),
            additionalInfo.getPersonalAgreement()
        );

        user.completeAdditionalInfo();
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !email.matches(emailRegex)) {
            throw new BizException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }
    private void validateAdditionalInfo(AdditionalSocialUserInfoDTO additionalInfo) {
        if (additionalInfo.getUserName() == null || additionalInfo.getUserName().trim().isEmpty()) {
            throw new BizException(ErrorCode.INVALID_USER_INFO, "이름은 필수 입력 항목입니다.");
        }

        if (additionalInfo.getUserPhone() == null || !additionalInfo.getUserPhone().matches("\\d{10,11}")) {
            throw new BizException(ErrorCode.INVALID_USER_INFO, "올바른 전화번호 형식이 아닙니다.");
        }

        if (!additionalInfo.getTermsAgreement()) {
            throw new BizException(ErrorCode.TERMS_AGREEMENT_REQUIRED);
        }
    }

    private void validateUserInfoUpdate(User user, AdditionalSocialUserInfoDTO additionalInfo) {
        if (!user.isRequiresAdditionalInfo()) {
            throw new BizException(ErrorCode.INVALID_REQUEST);
        }
    }

    private OidcPublicKeyList getPublicKeyList(String provider) {
        return switch (provider.toLowerCase()) {
            case "kakao" -> publicKeyProvider.getKakaoKeys();
            case "naver" -> publicKeyProvider.getNaverKeys();
            case "google" -> publicKeyProvider.getGoogleKeys(); // Google 지원 추가
            default -> throw new OAuth2AuthenticationException(
                new OAuth2Error("unsupported_provider"),
                "지원되지 않는 OAuth 제공자입니다: " + provider
            );
        };
    }
}