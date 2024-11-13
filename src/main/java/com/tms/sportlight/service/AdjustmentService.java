package com.tms.sportlight.service;

import com.tms.sportlight.domain.Adjustment;
import com.tms.sportlight.domain.AdjustmentStatus;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.AdjustmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Value("adjustment.min_request_amount")
    private double MIN_REQUEST_AMOUNT;

    /**
     * 정산 내역 생성
     * 요청 회원이 강사인지 확인
     * 정산 요청액과 실제 가능액 검증
     *
     * @param user 요청 회원
     * @param requestDTO 정산 요청 DTO
     * @return 저장된 정산 id
     */
    public int save(User user, AdjustmentRequestDTO requestDTO) {
        if(!user.getRoles().contains(UserRole.HOST)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        // TODO 정산 가능액 검증

        double requestAmount = requestDTO.getRequestAmount();
        double adjustedCharge = requestAmount * ADJUSTED_CHARGE_RATE;
        double vat = requestAmount * VAT_RATE;
        double withholdingTax = requestAmount * WITHHOLDING_TAX_RATE;
        double totalAmount = requestAmount - adjustedCharge - vat - withholdingTax;

        Adjustment adjustment = Adjustment.builder()
                .user(user)
                .requestAmount(requestAmount)
                .applicant(requestDTO.getApplicant())
                .rrn(requestDTO.getRrn())
                .bankName(requestDTO.getBankName())
                .depositor(requestDTO.getDepositor())
                .accountNumber(requestDTO.getAccountNumber())
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
    public List<AdjustmentListDTO> getAdjustmentListByUser(User user, PageRequestDTO<Void> pageRequestDTO) {
        if(!user.getRoles().contains(UserRole.HOST)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        List<Adjustment> adjustmentList = adjustmentRepository.findByUserId(user.getId(), pageRequestDTO.getPageable());
        return adjustmentList.stream()
                .map(AdjustmentListDTO::fromEntity)
                .toList();
    }

    public int getAdjustmentCountByUser(User user) {
        return adjustmentRepository.findCountByUserId(user.getId());
    }

    /**
     * 정산 내역 상세 조회
     *
     * @param user 요청 회원
     * @param id 정산 내역 id
     * @return 정산 상세 DTO
     */
    public AdjustmentDetailDTO getAdjustmentDetail(User user, int id) {
        Adjustment adjustment = get(id);
        if(!adjustment.getUser().getId().equals(user.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return AdjustmentDetailDTO.fromEntity(adjustment);
    }

    /**
     * 정산 요청 변경
     * 정산 상태가 REQUEST 상태일 때만 가능
     *
     * @param requestDTO 변경 요청 DTO
     */
    public void updateAdjustment(User user, int id, AdjustmentRequestDTO requestDTO) {
        Adjustment adjustment = get(id);
        if(!adjustment.getUser().getId().equals(user.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        } else if(!AdjustmentStatus.REQUEST.equals(adjustment.getStatus())) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE);
        }
        adjustment.updateAdjustment(requestDTO.getRequestAmount(), requestDTO.getApplicant(), requestDTO.getRrn(),
                requestDTO.getBankName(), requestDTO.getDepositor(), requestDTO.getAccountNumber());
    }


    /**
     * 정산 상태 변경
     *
     * @param id 정산 id
     * @param status 변경할 상태
     */
    public void updateAdjustmentStatus(int id, AdjustmentStatus status) {
        Adjustment adjustment = get(id);
        adjustment.updateStatus(status);
    }

    protected Adjustment get(int id) {
        return adjustmentRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_ADJUSTMENT));
    }
}