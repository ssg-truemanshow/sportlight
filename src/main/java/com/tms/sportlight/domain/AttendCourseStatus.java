package com.tms.sportlight.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendCourseStatus {
  APPROVED("승인"),
  REJECTED("거절");

  private final String value;
}
