
package com.tms.sportlight.controller;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.LoginIdFindRequestDTO;
import com.tms.sportlight.dto.PasswordFindRequestDTO;
import com.tms.sportlight.dto.PasswordResetDTO;
import com.tms.sportlight.dto.PasswordVerifyRequestDTO;
import com.tms.sportlight.dto.VerificationCodeDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.security.oauth.dto.AdditionalSocialUserInfoDTO;
import com.tms.sportlight.security.oauth.service.CustomOidcUserService;
import com.tms.sportlight.service.AuthService;
import com.tms.sportlight.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;
    private final CustomOidcUserService customOidcUserService;

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

    @PostMapping("/password-reset/request")
    public DataResponse<String> requestVerificationCode(
        @RequestBody PasswordFindRequestDTO request) {
        authService.sendVerificationCode(request);
        return DataResponse.of("인증번호가 이메일로 발송되었습니다.");
    }

    @PostMapping("/verify-code")
    public DataResponse<String> verifyCode(@RequestBody VerificationCodeDTO verificationCodeDTO) {
        authService.verifyAndStoreLoginId(verificationCodeDTO);
        return DataResponse.of("인증번호 검증이 완료되었습니다.");
    }

    @PostMapping("/password-reset/confirm")
    public DataResponse<String> confirmPasswordReset(
        @RequestBody PasswordResetDTO passwordResetDTO) {
        authService.resetPassword(passwordResetDTO);
        return DataResponse.of("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PostMapping("/reissue")
    public DataResponse<String> refreshAccessToken(@CookieValue("refresh") String refreshToken, HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BizException(ErrorCode.RE_AUTHENTICATION_REQUIRED);
        }

        Claims claims = jwtUtil.getClaims(refreshToken);
        String loginId = claims.get("loginId", String.class);

        if (!jwtUtil.validateRefreshToken(loginId, refreshToken)) {
            jwtUtil.deleteRefreshToken(loginId);
            throw new BizException(ErrorCode.RE_AUTHENTICATION_REQUIRED);
        }

        String newAccessToken = jwtUtil.createJwt(loginId, List.of("ROLE_USER"), Duration.ofMinutes(15).toMillis());
        String newRefreshToken = jwtUtil.createRefreshToken(loginId, Duration.ofDays(7).toMillis());

        jwtUtil.storeRefreshToken(loginId, newRefreshToken, Duration.ofDays(7).toMillis());

        Cookie cookie = new Cookie("refresh", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.ofDays(7).getSeconds());
        response.addCookie(cookie);

        return DataResponse.of(newAccessToken);
    }

    @PostMapping("/verify-password")
    public DataResponse<String> verifyPassword(@RequestBody PasswordVerifyRequestDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        authService.verifyCurrentPassword(user, request.getCurrentPassword());
        return DataResponse.of("비밀번호가 확인되었습니다.");
    }

    @PostMapping("/change-password")
    public DataResponse<String> changePassword(@RequestBody PasswordResetDTO request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        authService.changePassword(user, request.getNewPwd());
        return DataResponse.of("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PatchMapping("/oauth/additional-info")
    public DataResponse<String> updateAdditionalInfo(
        @Valid @RequestBody AdditionalSocialUserInfoDTO additionalInfo,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUser().getId();
        customOidcUserService.updateAdditionalInfo(userId, additionalInfo);

        return DataResponse.of("추가 정보가 성공적으로 업데이트되었습니다.");
    }

}

