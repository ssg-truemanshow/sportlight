package com.tms.sportlight.controller;

import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.DailySaleService;
import com.tms.sportlight.service.MonthlySaleService;
import com.tms.sportlight.service.YearlySaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/main")
@RequiredArgsConstructor
public class AdminMainController {
    private final DailySaleService dailySaleService;
    private final MonthlySaleService monthlySaleService;
    private final YearlySaleService yearlySaleService;

    @GetMapping
    public DataResponse<Map<String, BigDecimal>> getDashboardSales() {
        BigDecimal totalDailySaleAmount = dailySaleService.getTotalDailySaleAmount();
        BigDecimal averageDailySaleAmount = dailySaleService.getAverageDailySaleAmount();
        BigDecimal averageMonthlySaleAmount = monthlySaleService.getAverageMonthlySaleAmount();
        BigDecimal averageYearlySaleAmount = yearlySaleService.getAverageYearlySaleAmount();

        Map<String, BigDecimal> salesData = new HashMap<>();
        salesData.put("totalDailySale", totalDailySaleAmount);
        salesData.put("averageDailySale", averageDailySaleAmount);
        salesData.put("averageMonthlySale", averageMonthlySaleAmount);
        salesData.put("averageYearlySale", averageYearlySaleAmount);

        return DataResponse.of(salesData);
    }
}
