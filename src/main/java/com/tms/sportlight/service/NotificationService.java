package com.tms.sportlight.service;

import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

  /**
   * SSE 구독
   */
  public SseEmitter subscribe();

  /**
   * 알림 메시지 생성
   * @user_id 대상 유저 아이디
   * @title 알림 제목
   * @content 알림 내용
   * @type 알림 타입 (NOTIFICATION, REVIEW, QUESTION, INTEREST, COUPON, COURSE, MEMBER, ADJUSTMENT)
   * @target_grade 알림 대상 권한 (USER, HOST, ADMINISTRATOR, MEMBER)
   */
  public void insertNotification(long user_id, String title, String content, NotiType type, NotiGrade target_grade);
  public List<NotificationDTO> findAllNotification();
  public List<NotificationDTO> findNotificationByUserId(long userId);
  public Notification modifyNotification(long noti_Id);
  public void removeNotification(long id);
  public void removeAllNotification();
  public void removeSelectedNotification(List<Long> idList);
  public void deleteData();

}
