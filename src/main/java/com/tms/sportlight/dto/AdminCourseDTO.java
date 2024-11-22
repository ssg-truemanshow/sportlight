package com.tms.sportlight.dto;

import com.tms.sportlight.domain.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCourseDTO {
    private Integer id;
    private String categoryName;
    private String courseTitle;
    private double courseTuition;
    private int maxCapacity;
    private CourseLevel courseLevel;
    private LocalDateTime regDate;
}
