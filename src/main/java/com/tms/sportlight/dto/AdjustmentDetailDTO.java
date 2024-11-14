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
public class AdjustmentDetailDTO {

    private double requestAmount;
    private double totalAmount;
    private String applicant;
    private String rrn;
    private String bankName;
    private String depositor;
    private String accountNumber;
    private LocalDateTime reqDate;
    private LocalDateTime approvedDate;
    private double adjustedCharge;
    private double vat;
    private double withholdingTax;
    private AdjustmentStatus status;

    public static AdjustmentDetailDTO fromEntity(Adjustment adjustment) {
        return AdjustmentDetailDTO.builder()
                .requestAmount(adjustment.getRequestAmount())
                .totalAmount(adjustment.getTotalAmount())
                .applicant(adjustment.getApplicant())
                .rrn(adjustment.getRrn())
                .bankName(adjustment.getBankName())
                .depositor(adjustment.getDepositor())
                .accountNumber(adjustment.getAccountNumber())
                .reqDate(adjustment.getReqDate())
                .approvedDate(adjustment.getApprovedDate())
                .adjustedCharge(adjustment.getAdjustedCharge())
                .vat(adjustment.getVat())
                .withholdingTax(adjustment.getWithholdingTax())
                .status(adjustment.getStatus())
                .build();
    }

}
