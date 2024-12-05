package com.tms.sportlight.security.oauth.exception;

import com.tms.sportlight.exception.ErrorCode;

public class AdditionalOAuthInfoException extends OAuthAuthenticationException{

    private final Long userId;

    public AdditionalOAuthInfoException(Long userId) {
        super(ErrorCode.INVALID_USER_INFO, "추가 정보 입력이 필요합니다.");
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
