package com.tms.sportlight.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AvailableCouponDTO {

  private Integer id;
  private LocalDateTime issDate;
  private LocalDateTime expDate;
  private String name;
  private double discountRate;
}
