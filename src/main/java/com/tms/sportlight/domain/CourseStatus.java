package com.tms.sportlight.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseStatus {

    APPROVAL_REQUEST("승인 요청"),
    APPROVED("승인 완료"),
    DORMANCY("휴면"),
    DELETION_REQUEST("삭제 요청"),
    DELETED("삭제 완료");

    private final String value;
}
