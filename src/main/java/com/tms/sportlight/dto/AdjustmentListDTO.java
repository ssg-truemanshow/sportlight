package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Adjustment;
import com.tms.sportlight.domain.AdjustmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdjustmentListDTO {

    private double totalAmount;
    private String bankName;
    private String depositor;
    private LocalDateTime reqDate;
    private AdjustmentStatus status;

    public static AdjustmentListDTO fromEntity(Adjustment adjustment) {
        return AdjustmentListDTO.builder()
                .totalAmount(adjustment.getTotalAmount())
                .bankName(adjustment.getBankName())
                .depositor(adjustment.getDepositor())
                .reqDate(adjustment.getReqDate())
                .status(adjustment.getStatus())
                .build();
    }

}
