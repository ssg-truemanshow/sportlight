package com.tms.sportlight.security.oauth.service;

import com.nimbusds.oauth2.sdk.OAuth2Error;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final SocialUserRegistrationService socialUserRegistrationService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            String providerName = userRequest.getClientRegistration().getRegistrationId();

            if (!"naver".equalsIgnoreCase(providerName)) {
                return oauth2User;
            }

            Map<String, Object> attributes = oauth2User.getAttributes();
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            if (response == null) {
                throw new BizException(ErrorCode.INVALID_SOCIAL_CREDENTIALS);
            }

            String socialId = response.get("id").toString();
            String email = (String) response.get("email");
            String name = (String) response.get("name");

            User user = processUserRegistration(socialId, email, name, "NAVER");

            return new DefaultOAuth2User(
                user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList()),
                response,
                "id"
            );

        } catch (BizException e) {
            log.error("네이버 로그인 처리 중 비즈니스 예외 발생", e);
            throw e;
        } catch (Exception e) {
            log.error("네이버 로그인 실패", e);
            throw new BizException(ErrorCode.LOGIN_ERROR);
        }
    }

    private User processUserRegistration(String socialId, String email, String name, String provider) {
        return userRepository.findBySocialIdAndJoinMethod(socialId, provider)
            .orElseGet(() -> socialUserRegistrationService.createNewUser(socialId, email, name, provider));
    }
}