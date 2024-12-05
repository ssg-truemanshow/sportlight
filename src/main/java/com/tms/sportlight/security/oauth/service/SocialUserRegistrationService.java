package com.tms.sportlight.security.oauth.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialUserRegistrationService {

    private final UserRepository userRepository;

    public User createNewUser(String socialId, String email, String name, String provider) {
        if (email != null && !email.startsWith("temp_") &&
            userRepository.existsByLoginId(email)) {
            throw new BizException(ErrorCode.DUPLICATE_USERNAME, "이미 사용 중인 이메일입니다.");
        }

        String defaultEmail = String.format("temp_%s_%s@temp.com",
            provider.toLowerCase(),
            socialId);

        String baseNickname = name != null ? name : "User";
        String nickname = generateUniqueNickname(baseNickname);

        User user = User.builder()
            .socialId(socialId)
            .loginId(defaultEmail)
            .roles(List.of(UserRole.USER))
            .userName(name)
            .userNickname(nickname)
            .regDate(LocalDateTime.now())
            .isDeleted(false)
            .joinMethod(provider)
            .requiresAdditionalInfo(true)
            .termsAgreement(false)
            .marketingAgreement(false)
            .personalAgreement(false)
            .build();

        return userRepository.save(user);
    }

    private String generateUniqueNickname(String baseEmail) {
        String baseNickname = baseEmail.split("@")[0];
        String nickname = baseNickname;
        int suffix = 1;

        while (userRepository.existsByUserNickname(nickname)) {
            nickname = baseNickname + "_" + suffix++;
        }

        return nickname;
    }
}
