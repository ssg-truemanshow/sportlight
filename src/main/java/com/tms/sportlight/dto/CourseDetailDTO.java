package com.tms.sportlight.dto;

import com.tms.sportlight.domain.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailDTO {

  private Integer id;
  private String title;
  private String content;
  private String category;
  private Double tuition;
  private Double discountRate;
  private CourseLevel level;
  private String address;
  private String detailAddress;
  private Double latitude;
  private Double longitude;
  private int time;
  private int maxCapacity;
  private int minDaysPriorToReservation;
  private Integer views;
  private Integer hostId;
  private String nickname;
  private String bio;
  private String instar;
  private String facebook;
  private String twitter;
  private String youtube;
  private String imgUrl;
}

