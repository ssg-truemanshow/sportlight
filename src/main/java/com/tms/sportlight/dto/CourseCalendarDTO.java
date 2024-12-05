package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCalendarDTO {

    private int courseId;
    private String courseTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
