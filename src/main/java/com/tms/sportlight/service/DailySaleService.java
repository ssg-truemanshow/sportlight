package com.tms.sportlight.service;

import com.tms.sportlight.domain.DailySale;
import com.tms.sportlight.dto.DailySaleDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.DailySaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailySaleService {
    private final DailySaleRepository dailySaleRepository;
    private static final double VAT_RATE = 0.1;

    @Transactional
    public DailySaleDTO saveDailySale(DailySaleDTO dailySaleDTO) {
        if (dailySaleDTO.getTotalAmount() <= 0) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "총 금액은 0보다 커야 합니다.");
        }
        double vat = dailySaleDTO.getTotalAmount() * VAT_RATE;
        double netRevenue = dailySaleDTO.getTotalAmount() - vat;

        DailySale dailySale = DailySale.builder()
                .dsdate(dailySaleDTO.getDsdate())
                .totalAmount(dailySaleDTO.getTotalAmount())
                .vat(vat)
                .additionalRevenue(dailySaleDTO.getAdditionalRevenue())
                .netRevenue(netRevenue)
                .build();
        DailySale savedDailySale = dailySaleRepository.save(dailySale);
        return DailySaleDTO.fromEntity(savedDailySale);
    }

    @Transactional(readOnly = true)
    public List<DailySaleDTO> getAllDailySales() {
        return dailySaleRepository.findAll().stream()
                .map(DailySaleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DailySaleDTO getDailySaleById(Integer id) {
        return dailySaleRepository.findById(id)
                .map(DailySaleDTO::fromEntity)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE, "해당 ID로 일일 매출 내역을 찾을 수 없습니다: " + id));
    }

    @Transactional(readOnly = true)
    public BigDecimal getAverageDailySaleAmount() {
        return dailySaleRepository.findAverageDailySaleAmount();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalDailySaleAmount() {
        return dailySaleRepository.findTotalDailySaleAmount();
    }
}
