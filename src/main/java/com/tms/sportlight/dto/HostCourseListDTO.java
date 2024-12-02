package com.tms.sportlight.dto;

import com.tms.sportlight.domain.CourseStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostCourseListDTO {

    private int id;
    private String title;
    private CourseStatus status;
    private String thumbImg;
    private LocalDateTime regDate;
}
