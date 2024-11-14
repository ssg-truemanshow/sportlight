package com.tms.sportlight.controller;

import com.tms.sportlight.domain.AdjustmentStatus;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.AdjustmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController("/adjustments")
@RequiredArgsConstructor
public class AdjustmentController {

    private final AdjustmentService adjustmentService;

    @GetMapping("/{id}")
    public DataResponse<AdjustmentDetailDTO> get(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        AdjustmentDetailDTO adjustmentDetail = adjustmentService.getAdjustmentDetail(userDetails.getUser(), id.getId());
        return DataResponse.of(adjustmentDetail);
    }

    @PostMapping
    public DataResponse<Id> create(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid AdjustmentRequestDTO requestDTO) {
        int id = adjustmentService.save(userDetails.getUser(), requestDTO);
        return DataResponse.of(new Id(id));
    }

    @GetMapping
    public PageResponse<AdjustmentListDTO> getList(@AuthenticationPrincipal CustomUserDetails userDetails, PageRequestDTO<Void> pageRequestDTO) {
        List<AdjustmentListDTO> dtoList = adjustmentService.getAdjustmentListByUser(userDetails.getUser(), pageRequestDTO);
        int totalCount = adjustmentService.getAdjustmentCountByUser(userDetails.getUser());
        return new PageResponse<>(pageRequestDTO, dtoList, totalCount);
    }

    @GetMapping("")
    public DataResponse<Map<String, Double>> getPossibleAdjustmentAmount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        double amount = adjustmentService.getPossibleAdjustmentAmount(userDetails.getUser());
        Map<String, Double> amountMap = new HashMap<>();
        amountMap.put("amount", amount);
        return DataResponse.of(amountMap);
    }

    @PatchMapping("/{id}")
    public DataResponse<Void> modify(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id, @Valid AdjustmentRequestDTO requestDTO) {
        adjustmentService.updateAdjustment(userDetails.getUser(), id.getId(), requestDTO);
        return DataResponse.empty();
    }

    @PatchMapping("/{id}/status")
    public DataResponse<Void> modifyStatus(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id, @Valid @NotNull AdjustmentStatus status) {
        adjustmentService.updateAdjustmentStatus(userDetails.getUser(), id.getId(), status);
        return DataResponse.empty();
    }
}
