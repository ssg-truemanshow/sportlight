package com.tms.sportlight.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "setting_notification")
public class SettingNotification {


  @Id
  @Column(name = "user_id", nullable = false)
  private long userId;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "email_agreement", nullable = false)
  private boolean emailAgreement;

  @Column(name = "SMS_agreement", nullable = false)
  private boolean SMSAgreement;

  @Column(name = "push_agreement", nullable = false)
  private boolean pushAgreement;

}
