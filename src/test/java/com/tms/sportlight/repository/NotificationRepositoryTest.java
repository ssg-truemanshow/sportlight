package com.tms.sportlight.repository;

import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.domain.User;
import java.io.Console;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Log4j2
@SpringBootTest
public class NotificationRepositoryTest {

  @Autowired
  JpaNotificationRepository jpaNotificationRepository;

  @Autowired
  UserRepository userRepository;

  @Test
  public void insertTest() {
    User user = userRepository.getReferenceById(3l);


    Notification notification = Notification.builder()
        .userId(user)
        .notiTitle("title")
        .notiContent("content")
        .notiType(NotiType.COURSE)
        .notiGrade(NotiGrade.MEMBER)
        .createdAt(LocalDateTime.now())
            .build();

    jpaNotificationRepository.save(notification);
  }

  @Test
  public void updateRest() {
    Notification notification = jpaNotificationRepository.findById(1l).get();
    notification.changeReadState();;
    jpaNotificationRepository.save(notification);
  }

  @Test
  public void deleteTest() {
    jpaNotificationRepository.deleteById(1l);
  }

  @Test
  public void deleteAllTest() {
    jpaNotificationRepository.deleteAll();
  }




}
