package com.tms.sportlight.service;

import com.tms.sportlight.domain.YearlySale;
import com.tms.sportlight.dto.YearlySaleDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.YearlySaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YearlySaleService {
    private final YearlySaleRepository yearlySaleRepository;
    private static final double VAT_RATE = 0.1;

    @Transactional
    public YearlySaleDTO saveYearlySale(YearlySaleDTO yearlySaleDTO) {
        if (yearlySaleDTO.getTotalAmount() <= 0) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "총 금액은 0보다 커야 합니다.");
        }
        double vat = yearlySaleDTO.getTotalAmount() * VAT_RATE;
        double netRevenue = yearlySaleDTO.getTotalAmount() - vat;

        YearlySale yearlySale = YearlySale.builder()
                .dsdate(yearlySaleDTO.getDsdate())
                .totalAmount(yearlySaleDTO.getTotalAmount())
                .vat(vat)
                .additionalRevenue(yearlySaleDTO.getAdditionalRevenue())
                .netRevenue(netRevenue)
                .build();
        YearlySale savedYearlySale = yearlySaleRepository.save(yearlySale);
        return YearlySaleDTO.fromEntity(savedYearlySale);
    }

    @Transactional(readOnly = true)
    public List<YearlySaleDTO> getAllYearlySales() {
        return yearlySaleRepository.findAll().stream()
                .map(YearlySaleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public YearlySaleDTO getYearlySaleById(Integer id) {
        return yearlySaleRepository.findById(id)
                .map(YearlySaleDTO::fromEntity)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE, "해당 ID로 연간 매출 내역을 찾을 수 없습니다: " + id));
    }

    @Transactional(readOnly = true)
    public BigDecimal getAverageYearlySaleAmount() {
        return yearlySaleRepository.findAverageYearlyAmount();
    }
}
