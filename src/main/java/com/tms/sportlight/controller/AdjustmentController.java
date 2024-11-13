package com.tms.sportlight.controller;

import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.AdjustmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
