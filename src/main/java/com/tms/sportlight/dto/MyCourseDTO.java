package com.tms.sportlight.dto;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.AttendCourseStatus;
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
    private LocalDateTime completeDate;
    private int participantNum;
    private double totalAmount;
    private double finalAmount;
    private double refundAmount;
    private LocalDateTime refundDate;
    private String status;
    private boolean hasReview;
    private String imgUrl;

    public static MyCourseDTO fromEntity(AttendCourse attendCourse) {
        String status;
        double refundAmount = 0;
        LocalDateTime refundDate = null;

        if (attendCourse.getStatus() == AttendCourseStatus.REJECTED) {
            status = "REFUNDED";
            if (attendCourse.getRefundLog() != null) {
                refundAmount = attendCourse.getRefundLog().getRefundAmount();
                refundDate = attendCourse.getRefundLog().getRequestDate();
            }
        } else if (attendCourse.getCourseSchedule().getStartTime().isAfter(LocalDateTime.now())) {
            status = "UPCOMING";
        } else {
            status = "COMPLETED";
        }

        return MyCourseDTO.builder()
            .id(attendCourse.getId())
            .courseId(attendCourse.getCourseSchedule().getCourse().getId())
            .title(attendCourse.getCourseSchedule().getCourse().getTitle())
            .instructorName(attendCourse.getCourseSchedule().getCourse().getUser().getUserNickname())
            .startTime(attendCourse.getCourseSchedule().getStartTime())
            .endTime(attendCourse.getCourseSchedule().getEndTime())
            .completeDate(attendCourse.getCompleteDate()) // 결제일시 매핑
            .participantNum(attendCourse.getParticipantNum())
            .totalAmount(attendCourse.getTotalAmount())
            .finalAmount(attendCourse.getFinalAmount())
            .refundAmount(refundAmount)
            .refundDate(refundDate)
            .status(status)
            .build();
    }
}
