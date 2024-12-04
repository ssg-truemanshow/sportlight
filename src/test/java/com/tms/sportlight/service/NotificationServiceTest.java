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

    notificationService.insertNotification(user.getId(), "titleTest3", "contentTest3", NotiType.COURSE, NotiGrade.USER);

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
  public void removeAllTest() {
    notificationService.removeAllNotification();
  }


}


