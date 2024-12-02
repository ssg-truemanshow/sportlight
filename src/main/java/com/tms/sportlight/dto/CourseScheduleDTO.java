package com.tms.sportlight.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseSchedule;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    public CourseSchedule toEntity(Course course) {
        if(!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 이전이어야 합니다.");
        }
        return CourseSchedule.builder()
                .id(id)
                .course(course)
                .startTime(startTime)
                .endTime(endTime)
                .remainedNum(course.getMaxCapacity())
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
