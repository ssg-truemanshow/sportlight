package com.tms.sportlight.dto;

import com.tms.sportlight.domain.DailySale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySaleDTO {
    private Integer id;
    private LocalDateTime dsdate;
    private Double totalAmount;
    private Double vat;
    private Double additionalRevenue;
    private Double netRevenue;

    public static DailySaleDTO fromEntity(DailySale dailySale) {
        return DailySaleDTO.builder()
                .id(dailySale.getId())
                .dsdate(dailySale.getDsdate())
                .totalAmount(dailySale.getTotalAmount())
                .vat(dailySale.getVat())
                .additionalRevenue(dailySale.getAdditionalRevenue())
                .netRevenue(dailySale.getNetRevenue())
                .build();
    }
}
