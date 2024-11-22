package com.tms.sportlight.service;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.PasswordFindRequestDTO;
import com.tms.sportlight.dto.PasswordResetDTO;
import com.tms.sportlight.dto.PasswordResetVerifyDTO;
import com.tms.sportlight.dto.VerificationCodeDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VerificationCodeService verificationCodeService;

    /**
     * 사용자 이름과 전화번호로 로그인 ID(이메일) 찾기
     */
    public List<String> findLoginIds(String userName, String userPhone) {
        List<String> loginIds = userRepository.findAllLoginIds(userName, userPhone);
        if (loginIds.isEmpty()) {
            throw new BizException(ErrorCode.NOT_FOUND_USER);
        }
        return loginIds;
    }

    /**
     * 인증번호 생성 및 이메일 발송
     */
    public void sendVerificationCode(PasswordFindRequestDTO request) {
        Optional<User> userOpt = userRepository.findByLoginId(request.getLoginId());
        if (userOpt.isEmpty() || !isValidUser(userOpt.get(), request)) {
            throw new BizException(ErrorCode.INVALID_USER_INFO);
        }

        String code = verificationCodeService.generateAndSaveCode(request.getLoginId());

        try {
            String subject = "비밀번호 재설정 인증번호";
            String content = String.format("""
                <p>안녕하세요,</p>
                <p>요청하신 비밀번호 재설정 인증번호는 다음과 같습니다:</p>
                <h2>%s</h2>
                <p>본 인증번호는 3분간 유효합니다.</p>
                """, code);
            emailService.sendEmail(request.getLoginId(), subject, content);
        } catch (MessagingException e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 인증번호 검증 및 로그인 ID 저장
     */
    public void verifyAndStoreLoginId(VerificationCodeDTO verificationCodeDTO) {
        boolean isValid = verificationCodeService.verifyCode(
            verificationCodeDTO.getLoginId(),
            verificationCodeDTO.getCode()
        );

        if (!isValid) {
            throw new BizException(ErrorCode.INVALID_CODE);
        }

        verificationCodeService.disableCode(verificationCodeDTO.getLoginId());
        verificationCodeService.saveLoginId(verificationCodeDTO.getLoginId());
    }

    /**
     * 비밀번호 재설정
     */
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        String authenticatedLoginId = verificationCodeService.getLoginId();
        if (authenticatedLoginId == null) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        Optional<User> userOpt = userRepository.findByLoginId(authenticatedLoginId);
        if (userOpt.isEmpty()) {
            throw new BizException(ErrorCode.NOT_FOUND_USER);
        }

        User user = userOpt.get();

        if (bCryptPasswordEncoder.matches(passwordResetDTO.getNewPwd(), user.getLoginPwd())) {
            throw new BizException(ErrorCode.DUPLICATE_PASSWORD);
        }

        user.updatePassword(bCryptPasswordEncoder.encode(passwordResetDTO.getNewPwd()));
        user.userModTime();
        userRepository.save(user);

        verificationCodeService.deleteLoginId();
    }

    private boolean isValidUser(User user, PasswordFindRequestDTO request) {
        return user.getUserName().equals(request.getUserName())
            && user.getUserPhone().equals(request.getUserPhone());
    }

}
