
package com.tms.sportlight.controller;

import com.tms.sportlight.dto.PasswordFindRequestDTO;
import com.tms.sportlight.dto.PasswordResetDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * 비밀번호 재설정 요청을 처리하는 API 엔드포인트 사용자가 입력한 로그인 아이디, 이름, 전화번호를 검증한 후 비밀번호 변경 링크 발송
     *
     * @param request 비밀번호 재설정 요청 정보를 담은 DTO
     * @return 인증 코드 전송 성공 시 메시지와 함께 200 OK 응답 반환, 사용자 정보가 일치하지 않을 경우 400 Bad Request 응답 반환
     */
    @PostMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordResetLink(
        @RequestBody PasswordFindRequestDTO request) {
        boolean isSent = authService.sendPasswordResetLinkIfValid(
            request.getLoginId(), request.getUserName(), request.getUserPhone()
        );

        if (isSent) {
            return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 발송되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("사용자 정보가 일치하지 않습니다.");
        }
    }

    /**
     * 비밀번호 재설정 확인 및 업데이트
     */
    @PostMapping("/password-reset/confirm")
    public ResponseEntity<String> confirmPasswordReset(
        @RequestBody PasswordResetDTO passwordResetDTO) {
        boolean isTokenValid = authService.verifyToken(passwordResetDTO.getToken());

        if (isTokenValid) {
            authService.updatePassword(passwordResetDTO.getToken(),
                passwordResetDTO.getNewPwd());
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            throw new BizException(ErrorCode.EXPIRED_TOKEN);
        }
    }


}

