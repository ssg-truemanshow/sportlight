package com.tms.sportlight.controller;

import com.tms.sportlight.dto.YearlySaleDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.YearlySaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/yearly-sales")
@RequiredArgsConstructor
public class YearlySaleController {
    private final YearlySaleService yearlySaleService;

    @PostMapping
    public DataResponse<YearlySaleDTO> createYearlySale(@RequestBody YearlySaleDTO yearlySaleDTO) {
        YearlySaleDTO savedYearlySale = yearlySaleService.saveYearlySale(yearlySaleDTO);
        return DataResponse.of(savedYearlySale);
    }

    @GetMapping
    public DataResponse<List<YearlySaleDTO>> getAllYearlySales() {
        List<YearlySaleDTO> yearlySales = yearlySaleService.getAllYearlySales();
        return DataResponse.of(yearlySales);
    }

    @GetMapping("/{id}")
    public DataResponse<YearlySaleDTO> getYearlySaleById(@PathVariable Integer id) {
        YearlySaleDTO yearlySale = yearlySaleService.getYearlySaleById(id);
        return DataResponse.of(yearlySale);
    }
}
