package com.tms.sportlight.controller;

import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.security.oauth.dto.AdditionalSocialUserInfoDTO;
import com.tms.sportlight.security.oauth.service.CustomOidcUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final CustomOidcUserService customOidcUserService;

    /**
     * 소셜 로그인 사용자의 추가 정보 업데이트
     *
     * @param userId 사용자 ID
     * @param additionalInfo 추가 정보 DTO
     * @param userDetails 인증된 사용자 정보
     * @return 성공 메시지
     */
    @PostMapping("/additional-info/")
    public DataResponse<String> updateAdditionalInfo(
        @PathVariable Long userId,
        @Valid @RequestBody AdditionalSocialUserInfoDTO additionalInfo,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if (!userDetails.getUser().getId().equals(userId)) {
            throw new BizException(ErrorCode.AUTHENTICATION_ERROR);
        }

        customOidcUserService.updateAdditionalInfo(userId, additionalInfo);

        return DataResponse.of("추가 정보가 성공적으로 업데이트되었습니다.");
    }
}