package com.tms.sportlight.controller;

import com.tms.sportlight.dto.CouponDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public DataResponse<List<CouponDTO>> getAllCoupons() {
        List<CouponDTO> coupons = couponService.getAllCoupons();
        return DataResponse.of(coupons);
    }

    @GetMapping("/{id}")
    public DataResponse<CouponDTO> getCouponById(@PathVariable int id) {
        CouponDTO coupon = couponService.getCouponById(id);
        return DataResponse.of(coupon);
    }
}
