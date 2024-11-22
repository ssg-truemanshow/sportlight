package com.tms.sportlight.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCouponRequestDTO {
    private String name;
    private double discountRate;
}
