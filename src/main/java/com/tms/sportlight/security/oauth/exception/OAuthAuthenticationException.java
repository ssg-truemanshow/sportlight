package com.tms.sportlight.security.oauth.exception;

import com.tms.sportlight.exception.ErrorCode;

public class OAuthAuthenticationException extends RuntimeException{

    private final ErrorCode errorCode;

    public OAuthAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public OAuthAuthenticationException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
