package com.tms.sportlight.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API Response 객체가 공통으로 가진 속성을 정의하는 추상 클래스
 */
@Getter
@AllArgsConstructor
public abstract class APIResponse {
    private int code;
    private String message;
}
