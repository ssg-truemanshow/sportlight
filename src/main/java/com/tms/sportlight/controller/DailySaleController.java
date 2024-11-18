package com.tms.sportlight.controller;

import com.tms.sportlight.dto.DailySaleDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.DailySaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/daily-sales")
@RequiredArgsConstructor
public class DailySaleController {
    private final DailySaleService dailySaleService;

    @PostMapping
    public DataResponse<DailySaleDTO> createDailySale(@RequestBody DailySaleDTO dailySaleDTO) {
        DailySaleDTO savedDailySale = dailySaleService.saveDailySale(dailySaleDTO);
        return DataResponse.of(savedDailySale);
    }

    @GetMapping
    public DataResponse<List<DailySaleDTO>> getAllDailySales() {
        List<DailySaleDTO> dailySales = dailySaleService.getAllDailySales();
        return DataResponse.of(dailySales);
    }

    @GetMapping("/{id}")
    public DataResponse<DailySaleDTO> getDailySaleById(@PathVariable Integer id) {
        DailySaleDTO dailySale = dailySaleService.getDailySaleById(id);
        return DataResponse.of(dailySale);
    }
}