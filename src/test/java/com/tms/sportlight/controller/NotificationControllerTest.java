package com.tms.sportlight.controller;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Log4j2
@SpringBootTest
public class NotificationControllerTest {

  @Autowired
  NotificationController notificationController;


  @Test
  public void subscribeTest() {
    SseEmitter sseEmitter = notificationController.subscribe();
    log.info("test!! : " +sseEmitter.toString());
  }


}
