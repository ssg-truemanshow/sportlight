package com.tms.sportlight.domain;

import lombok.Getter;

@Getter
public enum MyCouponStatus {

    AVAILABLE("사용 가능"),
    USED("사용 완료"),
    EXPIRED("만료됨");

    private final String description;

    MyCouponStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
