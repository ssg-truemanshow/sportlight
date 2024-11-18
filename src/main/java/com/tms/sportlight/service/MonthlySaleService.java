package com.tms.sportlight.service;

import com.tms.sportlight.domain.MonthlySale;
import com.tms.sportlight.dto.MonthlySaleDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.MonthlySaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthlySaleService {
    private final MonthlySaleRepository monthlySaleRepository;
    private static final double VAT_RATE = 0.1;

    @Transactional
    public MonthlySaleDTO saveMonthlySale(MonthlySaleDTO monthlySaleDTO) {
        if (monthlySaleDTO.getTotalAmount() <= 0) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE, "총 금액은 0보다 커야 합니다.");
        }
        double vat = monthlySaleDTO.getTotalAmount() * VAT_RATE;
        double netRevenue = monthlySaleDTO.getTotalAmount() - vat;

        MonthlySale monthlySale = MonthlySale.builder()
                .dsdate(monthlySaleDTO.getDsdate())
                .totalAmount(monthlySaleDTO.getTotalAmount())
                .vat(vat)
                .additionalRevenue(monthlySaleDTO.getAdditionalRevenue())
                .netRevenue(netRevenue)
                .build();
        MonthlySale savedMonthlySale = monthlySaleRepository.save(monthlySale);
        return MonthlySaleDTO.fromEntity(savedMonthlySale);
    }

    @Transactional(readOnly = true)
    public List<MonthlySaleDTO> getAllMonthlySales() {
        return monthlySaleRepository.findAll().stream()
                .map(MonthlySaleDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MonthlySaleDTO getMonthlySaleById(Integer id) {
        return monthlySaleRepository.findById(id)
                .map(MonthlySaleDTO::fromEntity)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE, "해당 ID로 월간 매출 내역을 찾을 수 없습니다: " + id));
    }

    @Transactional(readOnly = true)
    public BigDecimal getAverageMonthlySaleAmount() {
        return monthlySaleRepository.findAverageMonthlySaleAmount();
    }
}
