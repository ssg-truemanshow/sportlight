package com.tms.sportlight.service;


import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.JpaNotificationRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
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
    log.info("subscribe emitter!: " + emitter);
    return emitter;
  }


  /**
   * 전달받은 메시지를 SseEmitter에 전송
   */
  private void sendNotification(NotificationDTO notificationDTO) {
    for (SseEmitter emitter : emitters) {
      try {
        log.info("send notification: " + notificationDTO);
        log.info("send emitter: " + emitter);
        emitter.send(SseEmitter.event().data(notificationDTO));
      } catch (IOException e) {
        emitters.remove(emitter);
        throw new BizException(ErrorCode.TRANSMISSION_FAILED_ERROR);
      }
    }
  }

  @Override
  public Notification insertNotification(NotificationDTO notiDTO) {
    Notification notification = Notification.builder()
        .userId(notiDTO.getUserId())
        .notiTitle(notiDTO.getNotiTitle())
        .notiContent(notiDTO.getNotiContent())
        .notiType(notiDTO.getNotiType())
        .notiGrade(notiDTO.getNotiGrade())
        .createdAt(notiDTO.getCreatedAt())
        .build();

    sendNotification(notiDTO);
    return jpaNotificationRepository.save(notification);

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
  public Notification modifyNotification(long userId) {
    Notification notification = jpaNotificationRepository.findById(userId).get();
    notification.changeReadState();

    NotificationDTO notificationDTO = NotificationDTO.builder()
        .notificationId(notification.getNotificationId())
        .userId(notification.getUserId())
        .notiTitle(notification.getNotiTitle())
        .notiContent(notification.getNotiContent())
        .notiType(notification.getNotiType())
        .notiGrade(notification.getNotiGrade())
        .createdAt(notification.getCreatedAt())
        .build();

    sendNotification(notificationDTO);
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


  private List<NotificationDTO> mapperToDTO(List<Notification> notificationList) {
    List<NotificationDTO> notificationDTOList = new ArrayList<>();
    notificationList.forEach(notification -> {
      notificationDTOList.add(NotificationDTO.builder()
          .notificationId(notification.getNotificationId())
          .userId(notification.getUserId())
          .notiTitle(notification.getNotiTitle())
          .notiContent(notification.getNotiContent())
          .notiType(notification.getNotiType())
          .notiGrade(notification.getNotiGrade())
          .createdAt(notification.getCreatedAt())
          .build());
    });
    return notificationDTOList;
  }

}
