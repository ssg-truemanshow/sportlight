package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseSchedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseScheduleDetailDTO {

  private Integer id;
  private Integer courseId;
  private String imgUrl;
  private String courseTitle;
  private String hostNickname;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String address;
  private String detailAddress;
  private double tuition;
  private double discountRate;
  private double latitude;
  private double longitude;

  public static CourseScheduleDetailDTO fromEntity(CourseSchedule entity) {
    return CourseScheduleDetailDTO.builder()
        .id(entity.getId())
        .courseId(entity.getCourse().getId())
        .courseTitle(entity.getCourse().getTitle())
        .hostNickname(entity.getCourse().getUser().getUserNickname())
        .startTime(entity.getStartTime())
        .endTime(entity.getEndTime())
        .address(entity.getCourse().getAddress())
        .detailAddress(entity.getCourse().getDetailAddress())
        .tuition(entity.getCourse().getTuition())
        .discountRate(entity.getCourse().getDiscountRate())
        .latitude(entity.getCourse().getLatitude())
        .longitude(entity.getCourse().getLongitude())
        .build();
  }
}
