package com.tms.sportlight.domain;

import lombok.Getter;

@Getter
public enum HostRequestStatus {
    PENDING("심사중"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String message;

    HostRequestStatus(String message) {
        this.message = message;
    }

    public static String getMessage(HostRequestStatus status) {
        return status != null ? status.getMessage() : "신청가능";
    }
}
