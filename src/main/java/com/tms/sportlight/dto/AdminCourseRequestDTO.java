package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCourseRequestDTO {
    private Integer courseId;
    private String categoryName;
    private String courseTitle;
    private double courseTuition;
    private int maxCapacity;
    private String status;
    private String regDate;
}
