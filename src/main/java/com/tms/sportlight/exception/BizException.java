package com.tms.sportlight.exception;

import lombok.Getter;

/**
 * 비지니스 로직 예외
 * ErrorCode로 예외를 정의
 */
@Getter
public class BizException extends RuntimeException {

    private final ErrorCode code;

    public BizException(ErrorCode code) {
        super(code.getDefaultMessage());
        this.code = code;
    }

    public BizException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
