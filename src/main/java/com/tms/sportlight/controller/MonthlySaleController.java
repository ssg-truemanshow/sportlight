package com.tms.sportlight.controller;

import com.tms.sportlight.dto.MonthlySaleDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.MonthlySaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/monthly-sales")
@RequiredArgsConstructor
public class MonthlySaleController {
    private final MonthlySaleService monthlySaleService;

    @PostMapping
    public DataResponse<MonthlySaleDTO> createMonthlySale(@RequestBody MonthlySaleDTO monthlySaleDTO) {
        MonthlySaleDTO savedMonthlySale = monthlySaleService.saveMonthlySale(monthlySaleDTO);
        return DataResponse.of(savedMonthlySale);
    }

    @GetMapping
    public DataResponse<List<MonthlySaleDTO>> getAllMonthlySales() {
        List<MonthlySaleDTO> monthlySales = monthlySaleService.getAllMonthlySales();
        return DataResponse.of(monthlySales);
    }

    @GetMapping("/{id}")
    public DataResponse<MonthlySaleDTO> getMonthlySaleById(@PathVariable Integer id) {
        MonthlySaleDTO monthlySale = monthlySaleService.getMonthlySaleById(id);
        return DataResponse.of(monthlySale);
    }
}
