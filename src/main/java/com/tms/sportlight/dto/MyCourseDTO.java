package com.tms.sportlight.dto;

import com.tms.sportlight.domain.AttendCourse;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MyCourseDTO {
    private Integer id;
    private Integer courseId;
    private String title;
    private String instructorName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int participantNum;
    private double totalAmount;
    private double finalAmount;
    private double refundAmount;
    private LocalDateTime refundDate;
    //private String status;
    private boolean hasReview;
    private String imgUrl;

    public static MyCourseDTO fromEntity(AttendCourse attendCourse) {
        return MyCourseDTO.builder()
            .id(attendCourse.getId())
            .courseId(attendCourse.getCourseSchedule().getCourse().getId())
            .title(attendCourse.getCourseSchedule().getCourse().getTitle())
            .instructorName(attendCourse.getCourseSchedule().getCourse().getUser().getUserNickname())
            .startTime(attendCourse.getCourseSchedule().getStartTime())
            .endTime(attendCourse.getCourseSchedule().getEndTime())
            .participantNum(attendCourse.getParticipantNum())
            .totalAmount(attendCourse.getTotalAmount())
            .finalAmount(attendCourse.getFinalAmount())
            //.status(attendCourse.getStatus())
            .build();
    }
}