package com.tms.sportlight.dto;

import com.tms.sportlight.domain.YearlySale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YearlySaleDTO {
    private Integer id;
    private LocalDateTime dsdate;
    private Double totalAmount;
    private Double vat;
    private Double additionalRevenue;
    private Double netRevenue;

    public static YearlySaleDTO fromEntity(YearlySale yearlySale) {
        return YearlySaleDTO.builder()
                .id(yearlySale.getId())
                .dsdate(yearlySale.getDsdate())
                .totalAmount(yearlySale.getTotalAmount())
                .vat(yearlySale.getVat())
                .additionalRevenue(yearlySale.getAdditionalRevenue())
                .netRevenue(yearlySale.getNetRevenue())
                .build();
    }
}
