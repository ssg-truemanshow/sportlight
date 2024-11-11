package com.tms.sportlight.service;

import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {


  public Notification insertNotification(NotificationDTO notificationDTO);
  public Notification modifyNotification(long od);
  public void removeNotification(long id);
  public void removeAllNotification();

}
