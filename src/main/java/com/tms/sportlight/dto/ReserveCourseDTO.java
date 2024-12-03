package com.tms.sportlight.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ReserveCourseDTO {
  private String paymentKey;
  private String orderId;
  private String amount;
  private Integer scheduleId;
  private Integer userCouponId;
  private Integer participantNum;
  private double finalAmount;
  private LocalDateTime requestDateTime;

  public String getJsonBody() {
    return "{\"paymentKey\":\"" + this.paymentKey
        + "\",\"orderId\":\"" + this.orderId
        + "\",\"amount\":\"" + this.amount
        + "\"}";
  }
}
