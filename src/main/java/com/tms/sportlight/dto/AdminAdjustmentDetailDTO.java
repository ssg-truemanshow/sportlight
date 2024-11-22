package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAdjustmentDetailDTO {
    private Integer adjustmentId;
    private Long userId;
    private double requestAmount;
    private double adjustedCharge;
    private double totalAmount;
    private String reqDate;
}
