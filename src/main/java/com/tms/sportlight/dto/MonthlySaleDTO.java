package com.tms.sportlight.dto;

import com.tms.sportlight.domain.MonthlySale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySaleDTO {
    private Integer id;
    private LocalDateTime dsdate;
    private Double totalAmount;
    private Double vat;
    private Double additionalRevenue;
    private Double netRevenue;

    public static MonthlySaleDTO fromEntity(MonthlySale monthlySale) {
        return MonthlySaleDTO.builder()
                .id(monthlySale.getId())
                .dsdate(monthlySale.getDsdate())
                .totalAmount(monthlySale.getTotalAmount())
                .vat(monthlySale.getVat())
                .additionalRevenue(monthlySale.getAdditionalRevenue())
                .netRevenue(monthlySale.getNetRevenue())
                .build();
    }
}
