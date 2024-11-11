package com.tms.sportlight.dto;

import com.tms.sportlight.domain.CourseLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateDTO {

    private String title;
    private String content;
    private double tuition;
    private double discountRate;
    private CourseLevel level;
    private String address;
    private String detailAddress;
    private double latitude;
    private double longitude;
    private int time;
    private int maxCapacity;
    private int minDaysPriorToReservation;
}
