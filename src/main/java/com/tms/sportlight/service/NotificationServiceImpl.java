package com.tms.sportlight.service;


import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.repository.JpaNotificationRepository;
import jakarta.transaction.Transactional;
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
  public List<Notification> findAllNotification() {
    return jpaNotificationRepository.findAll();
  }

  @Override
  public Notification modifyNotification(long id) {
    Notification notification = jpaNotificationRepository.findById(id).get();
    notification.changeReadState();
    return jpaNotificationRepository.save(notification);
  }

  @Override
  public void removeNotification(long id) {
    jpaNotificationRepository.deleteById(id);
  }

  @Override
  public void removeAllNotification() {
  jpaNotificationRepository.deleteAll();
  }


}
