package com.tms.sportlight.controller;

import com.tms.sportlight.dto.AvailableCouponDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.UserCouponService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {

  private final UserCouponService userCouponService;

  @GetMapping("/coupons/available")
  public DataResponse<List<AvailableCouponDTO>>  getAvailableCoupons(@AuthenticationPrincipal CustomUserDetails userDetails) {
    System.out.println(userCouponService.getAvailableCouponByUser(userDetails.getUser()).get(0).toString());
    return DataResponse.of(userCouponService.getAvailableCouponByUser(userDetails.getUser()));
  }
}
