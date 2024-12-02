package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Coupon;
import com.tms.sportlight.domain.MyCouponStatus;
import com.tms.sportlight.domain.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCouponDTO {

    private Integer id;
    private String eventName;
    private String name;
    private Integer discountPercentage;
    private String status;
    private String issueDate;
    private String expirationDate;

    public static MyCouponDTO fromEntity(UserCoupon userCoupon, MyCouponStatus status) {
        Coupon coupon = userCoupon.getCoupon();
        return MyCouponDTO.builder()
            .id(coupon.getId())
            .eventName(coupon.getEvent().getName())
            .name(coupon.getName())
            .discountPercentage((int) (coupon.getDiscountRate()))
            .status(status.getDescription())
            .issueDate(userCoupon.getIssDate().toString())
            .expirationDate(userCoupon.getExpDate().toString())
            .build();
    }
}
