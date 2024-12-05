package com.tms.sportlight.domain;

import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendCourseStatus {
  APPROVED("승인"),
  REJECTED("거절");

  private final String value;

  public static AttendCourseStatus fromValue(String value) {
    for (AttendCourseStatus status : AttendCourseStatus.values()) {
      if (status.getValue().equals(value)) {
        return status;
      }
    }
    throw new BizException(ErrorCode.INVALID_REQUEST);// 나중에 수정해야댐
  }
}
