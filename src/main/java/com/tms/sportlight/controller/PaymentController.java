package com.tms.sportlight.controller;

import com.tms.sportlight.dto.ReserveCourseDTO;
import com.tms.sportlight.facade.RedissonLockAttendCourseFacade;
import com.tms.sportlight.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
  private final RedissonLockAttendCourseFacade redissonLockAttendCourseFacade;
  @PostMapping("/confirm/widget")
  public ResponseEntity<JSONObject> confirmPayment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReserveCourseDTO reserveCourseDTO) {
    String jsonBody = reserveCourseDTO.getJsonBody();
    return redissonLockAttendCourseFacade.decrease(jsonBody, userDetails.getUser(), reserveCourseDTO);
  }
}
