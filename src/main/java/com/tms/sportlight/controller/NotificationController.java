package com.tms.sportlight.controller;

import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.service.NotificationService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
  private final UserRepository userRepository;

  /**
   * SSE 구독
   * @return
   */
  //@CrossOrigin(origins = "http://localhost:5173")
  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe() {
    SseEmitter emitter = new SseEmitter(0L);
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
    log.info("subscribe emitter!: " + emitter);
    return emitter;
  }

  /**
   * 전달받은 메시지를 SseEmitter에 전송
   */
  private void sendNotification(Notification notification) {
    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(SseEmitter.event().name("notification").data(notification));
      } catch (IOException e) {
        emitters.remove(emitter);
      }
    }
  }

  /**
   * 알림 메시지 생성
   * @param user  유저
   * @param title 알림 제목
   * @param content 알림 내용
   * @param type 알림타입
   * @param target_grade 알림 대상 등급
   */
  @PostMapping("/")
  public void createNotification(User user, String title, String content, NotiType type, NotiGrade target_grade){
    NotificationDTO notificationDTO = NotificationDTO.builder()
        .userId(user)
        .notiTitle(title)
        .notiContent(content)
        .notiType(type)
        .notiGrade(target_grade)
        .createdAt(LocalDateTime.now())
        .build();

    Notification notification = notificationService.insertNotification(notificationDTO);
    sendNotification(notification);
  }

  @PostMapping("/test")
  public ResponseEntity<Notification> createNotification(@RequestBody Notification noti){

    long id = (long)(Math.random()*11);
    User user1 = userRepository.findById(id).get();

    NotificationDTO notificationDTO = NotificationDTO.builder()
        .userId(user1)
        .notiTitle(noti.getNotiTitle())
        .notiContent(noti.getNotiContent())
        .notiType(NotiType.QUESTION)
        .notiGrade(NotiGrade.USER)
        .createdAt(LocalDateTime.now())
        .build();

    Notification notification = notificationService.insertNotification(notificationDTO);
    sendNotification(notification);

    return ResponseEntity.ok(notification);
  }


  @GetMapping
  public List<Notification> readAllNotification() {
    return notificationService.findAllNotification();
  }

  @PatchMapping("/{id}")
  public void updateNotification(@PathVariable long id) {
      log.info("change noti ID : " + id);
      Notification notification = notificationService.modifyNotification(id);
      sendNotification(notification);
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

}
