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
public class AdminRefundDTO {
    private Integer refundLogId;
    private double refundRate;
    private double refundAmount;
    private LocalDateTime requestDate;
    private Long userId;
    private Long courseOwnerId;
}
