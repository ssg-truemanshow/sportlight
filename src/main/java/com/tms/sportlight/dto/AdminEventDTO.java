package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEventDTO {
    private Integer id;
    private String name;
    private boolean status;
    private String content;
    private Integer num;
    private String classLink;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String couponName;
    private double discountRate;
}
