package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseSchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseScheduleDTO {

    private Integer id;
    private int courseId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public CourseSchedule toEntity(Course course) {
        return CourseSchedule.builder()
                .id(id)
                .course(course)
                .startTime(startTime)
                .endTime(endTime)
                .deleted(false)
                .regDate(LocalDateTime.now())
                .build();
    }

    public static CourseScheduleDTO fromEntity(CourseSchedule entity) {
        return CourseScheduleDTO.builder()
                .id(entity.getId())
                .courseId(entity.getId())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .build();
    }
}
