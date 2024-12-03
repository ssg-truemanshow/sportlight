package com.tms.sportlight.controller;

import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.NotificationService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

  private final NotificationService notificationService;


  /**
   * SSE 구독
   * @return
   */
  //@CrossOrigin(origins = "http://localhost:5173")
  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe() {
    return notificationService.subscribe();
  }


//  /**
//   * 알림 메시지 생성
//   * @param userId  유저
//   * @param title 알림 제목
//   * @param content 알림 내용
//   * @param type 알림타입(
//   * @param target_grade 알림 대상 등급
//   */
//  @PostMapping("/")
//  public void createNotification(long userId, String title, String content, NotiType type, NotiGrade target_grade){
//    NotificationDTO notificationDTO = NotificationDTO.builder()
//        .userId(userId)
//        .notiTitle(title)
//        .notiContent(content)
//        .notiType(type)
//        .notiGrade(target_grade)
//        .createdAt(LocalDateTime.now())
//        .build();
//
//    Notification notification = notificationService.insertNotification(notificationDTO);
//  }


//  @GetMapping
//  public List<NotificationDTO> readAllNotification() {
//    return notificationService.findAllNotification();
//  }

  @GetMapping
  public List<NotificationDTO> readNotification(@AuthenticationPrincipal CustomUserDetails customUserDetails ) { //long id
    return notificationService.findNotificationByUserId(customUserDetails.getUser().getId());
  }

  @PatchMapping("/{id}")
  public void updateNotification(@PathVariable long id) {
      log.info("change noti ID : " + id);
      notificationService.modifyNotification(id);
  }

  @DeleteMapping("/{id}")
  public void deleteNotification(@PathVariable long id) {
    log.info("delete noti ID : " + id);
    notificationService.removeNotification(id);
  }

  @DeleteMapping("/")
  public void deleteAllNotification() {
    notificationService.removeAllNotification();
  }

  @DeleteMapping("/select")
  public void deleteSelectedNotification(@RequestBody List<Long> notiList) {
    notificationService.removeSelectedNotification(notiList);

  }

}
