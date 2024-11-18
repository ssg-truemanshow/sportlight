package com.tms.sportlight.service;

import com.tms.sportlight.domain.Coupon;
import com.tms.sportlight.dto.CouponDTO;
import com.tms.sportlight.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(CouponDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CouponDTO getCouponById(int id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));
        return CouponDTO.fromEntity(coupon);
    }
}
