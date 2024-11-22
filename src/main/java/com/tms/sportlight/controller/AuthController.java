
package com.tms.sportlight.controller;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.LoginIdFindRequestDTO;
import com.tms.sportlight.dto.PasswordFindRequestDTO;
import com.tms.sportlight.dto.PasswordResetDTO;
import com.tms.sportlight.dto.PasswordResetVerifyDTO;
import com.tms.sportlight.dto.VerificationCodeDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.service.AuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;


    /**
     * 이름과 휴대전화 번호로 아이디(이메일) 찾기
     *
     * @param request 사용자의 요청
     * @return 조회된 사용자의 이메일(로그인 ID)
     */
    @PostMapping("/find-login-id")
    public DataResponse<List<String>> findLoginIds(@RequestBody LoginIdFindRequestDTO request) {
        List<String> loginIds = authService.findLoginIds(request.getUserName(),
            request.getUserPhone());
        return DataResponse.of(loginIds);
    }

    /**
     * 로그인 아이디, 이름, 전화번호를 검증한 후 비밀번호 변경 링크 발송
     *
     * @param request 비밀번호 재설정 요청 정보를 담은 DTO
     * @return 인증 코드 전송 성공 시 메시지와 함께 200 OK 응답 반환, 사용자 정보가 일치하지 않을 경우 400 Bad Request 응답 반환
     */
    /*@PostMapping("/password-reset/request")
    public DataResponse<String> requestPasswordResetLink(
        @RequestBody PasswordFindRequestDTO request) {
        boolean isSent = authService.sendPasswordResetLinkIfValid(
            request.getLoginId(), request.getUserName(), request.getUserPhone()
        );

        if (isSent) {
            return DataResponse.of("비밀번호 재설정 링크가 이메일로 발송되었습니다.");
        } else {
            throw new BizException(ErrorCode.INVALID_USER_INFO);
        }
    }*/


    /*@PostMapping("/password-reset/confirm")
    public DataResponse<String> confirmPasswordReset(
        @RequestBody PasswordResetDTO passwordResetDTO) {
        boolean isTokenValid = authService.verifyToken(passwordResetDTO.getToken());

        if (isTokenValid) {
            authService.updatePassword(passwordResetDTO.getToken(),
                passwordResetDTO.getNewPwd());
            return DataResponse.of("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            throw new BizException(ErrorCode.EXPIRED_TOKEN);
        }
    }*/


    @PostMapping("/password-reset/request")
    public DataResponse<String> requestVerificationCode(@RequestBody PasswordFindRequestDTO request) {
        authService.sendVerificationCode(request);
        return DataResponse.of("인증번호가 이메일로 발송되었습니다.");
    }

    @PostMapping("/verify-code")
    public DataResponse<String> verifyCode(@RequestBody VerificationCodeDTO verificationCodeDTO) {
        authService.verifyAndStoreLoginId(verificationCodeDTO);
        return DataResponse.of("인증번호 검증이 완료되었습니다.");
    }

    @PostMapping("/password-reset/confirm")
    public DataResponse<String> confirmPasswordReset(@RequestBody PasswordResetDTO passwordResetDTO) {
        authService.resetPassword(passwordResetDTO);
        return DataResponse.of("비밀번호가 성공적으로 변경되었습니다.");
    }
}

