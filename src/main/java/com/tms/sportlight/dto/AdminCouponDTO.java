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
public class AdminCouponDTO {
    private Integer couponId;
    private Integer eventId;
    private String couponName;
    private String eventName;
    private double discountRate;
    private Integer couponNum;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
