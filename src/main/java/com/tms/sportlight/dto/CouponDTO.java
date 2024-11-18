package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private Integer id;
    private String name;
    private double discountRate;
    public static CouponDTO fromEntity(Coupon coupon) {
        return CouponDTO.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .discountRate(coupon.getDiscountRate())
                .build();
    }
}
