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
public class AdminPaymentDTO {
    private Integer attendCourseId;
    private double totalAmount;
    private double paymentFee;
    private LocalDateTime regDate;
    private Long userId;
    private Long courseOwnerId;
}
