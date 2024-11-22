package com.tms.sportlight.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.dto.JoinDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

        boolean isLoginIdExist = userRepository.existsByLoginId(joinDTO.getLoginId());
        if (isLoginIdExist) {
            throw new BizException(ErrorCode.DUPLICATE_USERNAME);
        }

        boolean isNicknameExist = userRepository.existsByUserNickname(joinDTO.getUserNickname());
        if (isNicknameExist) {
            throw new BizException(ErrorCode.DUPLICATE_NICKNAME);
        }

        String loginId = joinDTO.getLoginId();
        String loginPwd = joinDTO.getLoginPwd();
        String userNickname = joinDTO.getUserNickname();
        String userName = joinDTO.getUserName();
        String userGender = joinDTO.getUserGender();
        String userBirth = joinDTO.getUserBirth();
        String userPhone = joinDTO.getUserPhone();
        Boolean termsAgreement = joinDTO.getTermsAgreement();
        Boolean marketingAgreement = joinDTO.getMarketingAgreement();
        Boolean personalAgreement = joinDTO.getPersonalAgreement();

        User data = User.builder()
            .loginId(loginId)
            .loginPwd(bCryptPasswordEncoder.encode(loginPwd))
            .roles(new ArrayList<>(List.of(UserRole.USER)))
            .userNickname(userNickname)
            .userName(userName)
            .userGender(userGender)
            .userBirth(userBirth)
            .userPhone(userPhone)
            .termsAgreement(termsAgreement)
            .marketingAgreement(marketingAgreement)
            .personalAgreement(personalAgreement)
            .regDate(LocalDateTime.now())
            .isDeleted(false)
            .build();

        userRepository.save(data);

    }

}
