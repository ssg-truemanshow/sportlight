package com.tms.sportlight.service;

import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.NotificationDTO;
import com.tms.sportlight.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Log4j2
@SpringBootTest
public class NotificationServiceTest {

  @Autowired
  NotificationService notificationService;
  @Autowired
  UserRepository userRepository;

  @Test
  public void insertTest() {
    User user = userRepository.getReferenceById(4l);

    NotificationDTO notificationDTO = NotificationDTO.builder()
        .userId(user.getId())
        .notiTitle("titleTest3")
        .notiContent("contentTest3")
        .notiType(NotiType.COURSE)
        .notiGrade(NotiGrade.USER)
        .createdAt(LocalDateTime.now())
        .build();

    notificationService.insertNotification(notificationDTO);

  }

  @Test
  public void findAllTest() {
    List<NotificationDTO> notificationList = notificationService.findAllNotification();

    for (NotificationDTO notification : notificationList) {
      log.info(notification);
    }
  }

  @Test
  public void findNotificationByUserIdTest() {
    List<NotificationDTO> notificationList = notificationService.findNotificationByUserId(8l);

    for (NotificationDTO notification : notificationList) {
      log.info(notification);
    }
  }


  @Test
  public void modifyTest() {
    notificationService.modifyNotification(4l);
  }

  @Test
  public void removeTest() {
    notificationService.removeNotification(4l);
  }

  @Test
  public void removeAllTest() {
    notificationService.removeAllNotification();
  }

}
