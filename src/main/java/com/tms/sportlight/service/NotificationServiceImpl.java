package com.tms.sportlight.service;


import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.JpaNotificationRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class NotificationServiceImpl implements NotificationService{

  public final JpaNotificationRepository jpaNotificationRepository;
  private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

  @Override
  public SseEmitter subscribe() {
    SseEmitter emitter = new SseEmitter(0L);
    emitters.add(emitter);
    emitter.onCompletion(() -> emitters.remove(emitter));
    emitter.onTimeout(() -> emitters.remove(emitter));
//    log.info("subscribe emitter!: " + emitter);
    return emitter;
  }


  /**
   * 전달받은 메시지를 SseEmitter에 전송
   */
  private void sendNotification(NotificationDTO notificationDTO) {
    for (SseEmitter emitter : emitters) {
      try {
//        log.info("send notification: " + notificationDTO);
//        log.info("send emitter: " + emitter);
        emitter.send(SseEmitter.event().data(notificationDTO));
      } catch (IOException e) {
        emitters.remove(emitter);
        e.printStackTrace();
        throw new BizException(ErrorCode.TRANSMISSION_FAILED_ERROR);
      }
    }
  }


  /**
   * 알림 메시지 생성
   * @user_id 대상 유저 아이디
   * @title 알림 제목
   * @content 알림 내용
   * @type 알림 타입 (NOTIFICATION, REVIEW, QUESTION, INTEREST, COUPON, COURSE, MEMBER, ADJUSTMENT)
   * @target_grade 알림 대상 권한 (USER, HOST, ADMINISTRATOR, MEMBER)
   */
  @Override
  public void insertNotification(long user_id, String title, String content, NotiType type, NotiGrade target_grade) {
    Notification notification = Notification.builder()
        .userId(user_id)
        .notiTitle(title)
        .notiContent(content)
        .notiType(type)
        .notiGrade(target_grade)
        .notiReadOrNot(false)
        .createdAt(LocalDateTime.now())
        .build();

    sendNotification(mapperToDTO(jpaNotificationRepository.save(notification)));

  }

  @Override
  public List<NotificationDTO> findAllNotification() {
    List<Notification> notificationList = jpaNotificationRepository.findAll();

    return mapperToDTO(notificationList);
  }

  @Override
  public List<NotificationDTO> findNotificationByUserId(long userId) {
    List<Notification> notificationList = jpaNotificationRepository.findByUserId(userId);

    return mapperToDTO(notificationList);
  }

  @Override
  public Notification modifyNotification(long noti_Id) {
    Notification notification = jpaNotificationRepository.findById(noti_Id).get();
    notification.changeReadState();

/*    NotificationDTO notificationDTO = NotificationDTO.builder()
        .notificationId(notification.getNotificationId())
        .userId(notification.getUserId())
        .notiTitle(notification.getNotiTitle())
        .notiContent(notification.getNotiContent())
        .notiReadOrNot(notification.isNotiReadOrNot())
        .notiType(notification.getNotiType())
        .notiGrade(notification.getNotiGrade())
        .createdAt(notification.getCreatedAt())
        .build();
    sendNotification(notificationDTO);*/
    return jpaNotificationRepository.save(notification);
  }

  @Override
  public void removeNotification(long userId) {
    jpaNotificationRepository.deleteById(userId);
  }

  @Override
  public void removeAllNotification() {
  jpaNotificationRepository.deleteAll();
  }

  @Override
  public void removeSelectedNotification(List<Long> idList) {
    idList.forEach(id -> jpaNotificationRepository.deleteById(id));
  }

  /**
   * 자동 삭제
   */
  public void deleteData() {
    jpaNotificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(14)); //14일 이전 데이터 삭제
    NotificationDTO notificationDTO = NotificationDTO.builder()
        .notiTitle("delete data")
        .notiContent("14일 이전 데이터 삭제")
        .build();
    sendNotification(notificationDTO);
  }

  /**
   * Notification List -> NotificationDTO List 변환
   * @param notificationList
   * @return
   */
  private List<NotificationDTO> mapperToDTO(List<Notification> notificationList) {
    List<NotificationDTO> notificationDTOList = new ArrayList<>();
    notificationList.forEach(notification -> {
      notificationDTOList.add(NotificationDTO.builder()
          .notificationId(notification.getNotificationId())
          .userId(notification.getUserId())
          .notiTitle(notification.getNotiTitle())
          .notiContent(notification.getNotiContent())
          .notiReadOrNot(notification.isNotiReadOrNot())
          .notiType(notification.getNotiType())
          .notiGrade(notification.getNotiGrade())
          .createdAt(notification.getCreatedAt())
          .build());
    });
    return notificationDTOList;
  }

  /**
   * Notification -> NotificationDTO 변환
   * @param notification
   * @return
   */
  private NotificationDTO mapperToDTO(Notification notification) {
    return NotificationDTO.builder()
        .notificationId(notification.getNotificationId())
        .userId(notification.getUserId())
        .notiTitle(notification.getNotiTitle())
        .notiContent(notification.getNotiContent())
        .notiReadOrNot(notification.isNotiReadOrNot())
        .notiType(notification.getNotiType())
        .notiGrade(notification.getNotiGrade())
        .createdAt(notification.getCreatedAt())
        .build();
  }

}
