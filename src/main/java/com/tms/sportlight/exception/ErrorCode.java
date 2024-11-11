package com.tms.sportlight.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API 요청을 처리할 때 발생한 비지니스 예외를 정의
 * API 응답의 HTTP Status Code, 자체 코드, 초기 메세지
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, -400, "입력 값이 올바르지 않습니다"),
    INVALID_INPUT_FILE(HttpStatus.BAD_REQUEST, -401, "파일 형식이 올바르지 않습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, -440, "인증을 실패했습니다"),

    /* 403 FORBIDDEN : 사용자가 권한이 없어 요청이 거부됨 */
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, -450, "접근 권한이 없는 사용자입니다"),

    /* 404 NOT_FOUND : 요청 리소스를 찾을 수 없음 */
    NOT_FOUND_COURSE(HttpStatus.NOT_FOUND, -460, "존재하지 않는 클래스 입니다"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, -461, "존재하지 않는 카테고리 입니다"),
    NOT_FOUND_COMMUNITY(HttpStatus.NOT_FOUND, -462, "존재하지 않는 커뮤니티 입니다"),
    NOT_FOUND_COURSE_SCHEDULE(HttpStatus.NOT_FOUND, -463, "존재하지 않는 클래스 스케줄 입니다"),

    /* 405 METHOD_NOT_ALLOWED : 허용하지 않는 method 요청 */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, -480, "허용되지 않은 method 입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, -485, "이미 존재하는 아이디 입니다"),
    DUPLICATE_PASSWORD(HttpStatus.CONFLICT, -486, "새 비밀번호가 기존 비밀번호와 동일합니다"),

    /* 415 UNSUPPORTED_MEDIA_TYPE : 지원하지 않는 media type 요청 */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, -499, "지원하지 않는 미디어 타입입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -500, "서버 내부 오류");

    private final HttpStatus status;
    private final int code;
    private final String defaultMessage;
}
