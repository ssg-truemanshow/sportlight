package com.tms.sportlight.dto;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.AttendCourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseApplicantDTO {

    private int id;
    private int courseScheduleId;
    private long userId;
    private String userNickname;
    private String userName;
    private String userPhone;
    private int participantNum;
    private AttendCourseStatus status;
    private LocalDateTime requestDate;

    public static CourseApplicantDTO fromEntity(AttendCourse attendCourse) {
        return CourseApplicantDTO.builder()
                .id(attendCourse.getId())
                .courseScheduleId(attendCourse.getCourseSchedule().getId())
                .userId(attendCourse.getUser().getId())
                .userName(attendCourse.getUser().getUserName())
                .userPhone(attendCourse.getUser().getUserPhone())
                .participantNum(attendCourse.getParticipantNum())
                .status(attendCourse.getStatus())
                .requestDate(attendCourse.getRequestDate())
                .build();
    }

}
