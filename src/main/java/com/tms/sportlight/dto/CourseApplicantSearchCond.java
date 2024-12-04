package com.tms.sportlight.dto;

import com.tms.sportlight.domain.AttendCourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseApplicantSearchCond {

    private AttendCourseStatus status;
}
