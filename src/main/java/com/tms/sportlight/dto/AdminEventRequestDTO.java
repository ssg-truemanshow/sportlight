package com.tms.sportlight.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEventRequestDTO {
    private String name;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String classLink;
    private Integer couponNum;
    private List<AdminCouponRequestDTO> coupons;
}
