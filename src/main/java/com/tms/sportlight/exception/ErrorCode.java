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
    LESS_THAN_MIN_REQUEST_AMOUNT(HttpStatus.BAD_REQUEST, -402, "최소 정산액 미만입니다"),
    OVER_POSSIBLE_ADJUSTMENT_AMOUNT(HttpStatus.BAD_REQUEST, -403, "가능 정산액을 초과했습니다"),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, -404, "인증 토큰이 만료되었습니다."),
    EXCEED_COMMUNITY_MAX_CAPACITY(HttpStatus.BAD_REQUEST, -405, "커뮤니티 최대 수용 인원을 초과했습니다"),
    NOT_COMMUNITY_PARTICIPANT(HttpStatus.BAD_REQUEST, -406, "해당 커뮤니티 참가자가 아닙니다"),
    EXIST_TWO_OR_MORE_PARTICIPANTS(HttpStatus.BAD_REQUEST, -407, "2명 이상의 참가자가 존재합니다"),
    INVALID_REQUEST_UPDATE(HttpStatus.BAD_REQUEST, -408, "관리자에 의해 처리된 요청은 수정할 수 없습니다."),
    INVALID_REQUEST_DELETE(HttpStatus.BAD_REQUEST, -409, "관리자에 의해 처리된 요청은 삭제할 수 없습니다."),
    INVALID_USER_INFO(HttpStatus.BAD_REQUEST, -410, "사용자 정보가 일치하지 않습니다."),
    INVALID_CODE(HttpStatus.BAD_REQUEST, -411, "인증번호가 유효하지 않습니다."),
    INVALID_REQUEST_LOGIN(HttpStatus.BAD_REQUEST, -412, "아이디 혹은 비밀번호를 입력해주세요."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, -413, "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, -414, "토큰이 유효하지 않습니다."),
    NOT_VALID_ID_TOKEN(HttpStatus.BAD_REQUEST, -415, "ID 토큰 서명 검증에 실패했습니다."),
    INVALID_JWT_FORMAT(HttpStatus.BAD_REQUEST, -416, "ID 토큰의 형식이 올바르지 않습니다."),
    INVALID_CANCEL_REQUEST(HttpStatus.BAD_REQUEST, -417, "취소할 수 없는 상태입니다."),
    MISSING_ADDITIONAL_INFO(HttpStatus.BAD_REQUEST, -418, "추가 정보가 제공되지 않았습니다."),
    TERMS_AGREEMENT_REQUIRED(HttpStatus.BAD_REQUEST, -419, "이용 약관 동의는 필수입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, -420, "올바르지 않은 이메일 형식입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, -421, "올바르지 않은 요청입니다."),
    INVALID_SOCIAL_PROVIDER(HttpStatus.BAD_REQUEST, -422, "지원하지 않는 프로바이더입니다."),
    INVALID_SOCIAL_CREDENTIALS(HttpStatus.BAD_REQUEST, -423, "유효하지 않은 응답입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, -440, "인증을 실패했습니다"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, -441, "유효하지 않은 액세스 토큰입니다"),
    RE_AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, -442, "재인증이 필요합니다"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, -443, "유효하지 않은 인증 정보입니다"),

    /* 403 FORBIDDEN : 사용자가 권한이 없어 요청이 거부됨 */
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, -450, "접근 권한이 없는 사용자입니다"),

    /* 404 NOT_FOUND : 요청 리소스를 찾을 수 없음 */
    NOT_FOUND_COURSE(HttpStatus.NOT_FOUND, -460, "존재하지 않는 클래스 입니다"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, -461, "존재하지 않는 카테고리 입니다"),
    NOT_FOUND_COMMUNITY(HttpStatus.NOT_FOUND, -462, "존재하지 않는 커뮤니티 입니다"),
    NOT_FOUND_COURSE_SCHEDULE(HttpStatus.NOT_FOUND, -463, "존재하지 않는 클래스 스케줄 입니다"),
    NOT_FOUND_ADJUSTMENT(HttpStatus.NOT_FOUND, -464, "존재하지 않는 정산 내역 입니다"),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, -465, "존재하지 않는 파일 입니다"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, -466, "존재하지 않는 회원 입니다"),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, -467, "존재하지 않는 리뷰 입니다"),
    NOT_FOUND_REQUEST(HttpStatus.NOT_FOUND, -468, "존재하지 않는 요청 입니다"),
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, -469, "존재하지 않는 쿠폰 입니다."),

    /* 405 METHOD_NOT_ALLOWED : 허용하지 않는 method 요청 */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, -480, "허용되지 않은 method 입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, -485, "이미 존재하는 아이디 입니다"),
    DUPLICATE_PASSWORD(HttpStatus.CONFLICT, -486, "새 비밀번호가 기존 비밀번호와 동일합니다"),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, -487, "이미 존재하는 닉네임 입니다"),
    ALREADY_EXISTS_PARTICIPANT(HttpStatus.CONFLICT, -488, "이미 해당 커뮤니티에 참여하고 있는 회원입니다"),
    DUPLICATE_HOST_REQUEST(HttpStatus.CONFLICT, -489, "이미 작성된 요청서가 존재합니다."),

    /* 415 UNSUPPORTED_MEDIA_TYPE : 지원하지 않는 media type 요청 */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, -499, "지원하지 않는 미디어 타입입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -500, "서버 내부 오류"),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -501, "프로필 이미지 저장 실패"),
    LOGIN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -502, "로그인 실패"),

    TRANSMISSION_FAILED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -501, "메시지 전송에 실패했습니다");

    private final HttpStatus status;
    private final int code;
    private final String defaultMessage;
}