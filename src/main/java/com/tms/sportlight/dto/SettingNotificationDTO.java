package com.tms.sportlight.dto;


import com.tms.sportlight.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettingNotificationDTO {

  private long userId;
  private User user;
  private boolean emailAgreement;
  private boolean SMSAgreement;
  private boolean pushAgreement;
}
