package com.tms.sportlight.service;

import com.tms.sportlight.domain.Adjustment;
import com.tms.sportlight.domain.AdjustmentStatus;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.dto.AdjustmentCreateDTO;
import com.tms.sportlight.dto.AdjustmentDetailDTO;
import com.tms.sportlight.dto.AdjustmentListDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.AdjustmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;
    @Value("adjustment.adjusted_charge_rate")
    private double ADJUSTED_CHARGE_RATE;
    @Value("adjustment.vat_rate")
    private double VAT_RATE;
    @Value("adjustment.withholding_tax_rate")
    private double WITHHOLDING_TAX_RATE;

    /**
     * 정산 내역 생성
     * 요청 회원이 강사인지 확인
     * 정산 요청액과 실제 가능액 검증
     *
     * @param user 요청 회원
     * @param createDTO 정산 생성 DTO
     * @return 저장된 정산 id
     */
    public int save(User user, AdjustmentCreateDTO createDTO) {
        if(!user.getRoles().contains(UserRole.HOST)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        // TODO 정산 가능액 검증

        double requestAmount = createDTO.getRequestAmount();
        double adjustedCharge = requestAmount * ADJUSTED_CHARGE_RATE;
        double vat = requestAmount * VAT_RATE;
        double withholdingTax = requestAmount * WITHHOLDING_TAX_RATE;
        double totalAmount = requestAmount - adjustedCharge - vat - withholdingTax;

        Adjustment adjustment = Adjustment.builder()
                .user(user)
                .requestAmount(requestAmount)
                .applicant(createDTO.getApplicant())
                .rrn(createDTO.getRrn())
                .bankName(createDTO.getBankName())
                .depositor(createDTO.getDepositor())
                .accountNumber(createDTO.getAccountNumber())
                .adjustedCharge(adjustedCharge)
                .vat(vat)
                .withholdingTax(withholdingTax)
                .totalAmount(totalAmount)
                .reqDate(LocalDateTime.now())
                .status(AdjustmentStatus.REQUEST)
                .build();
        return adjustmentRepository.save(adjustment);
    }

    /**
     * 정산 내역 목록 조회
     * 요청 회원이 강사인지 확인
     *
     * @param user 요청 회원
     * @return 정산 목록 DTO 리스트
     */
    public List<AdjustmentListDTO> getAdjustments(User user) {
        if(!user.getRoles().contains(UserRole.HOST)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        List<Adjustment> adjustmentList = adjustmentRepository.findByUserId(user.getId());
        return adjustmentList.stream()
                .map(AdjustmentListDTO::fromEntity)
                .toList();
    }

    /**
     * 정산 내역 상세 조회
     *
     * @param user 요청 회원
     * @param id 정산 내역 id
     * @return 정산 상세 DTO
     */
    public AdjustmentDetailDTO getAdjustment(User user, int id) {
        Adjustment adjustment = adjustmentRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_ADJUSTMENT));
        if(!adjustment.getUser().getId().equals(user.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return AdjustmentDetailDTO.fromEntity(adjustment);
    }
}
