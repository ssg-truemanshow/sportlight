package com.tms.sportlight.service;


import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.repository.JpaNotificationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class NotificationServiceImpl implements NotificationService{

  public final JpaNotificationRepository jpaNotificationRepository;



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
