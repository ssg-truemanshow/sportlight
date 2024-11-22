package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminGenderAgeGroupCourseCountDTO {
    private String gender;          // 성별 구분
    private String ageGroup;        // 나이대 구분
    private Long courseCount;       // 성별 & 나이대 별 클래스 수강 갯수
}
