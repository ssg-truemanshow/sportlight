package com.tms.sportlight.dto;


import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationDTO {

  private long notificationId;
  private long userId;  //회원
  private String notiTitle;
  private String notiContent;
  private NotiType notiType;
  private boolean notiReadOrNot;
  private LocalDateTime created_At;
  private NotiGrade notiGrade;
}
