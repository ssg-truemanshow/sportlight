package com.tms.sportlight.service;

import com.tms.sportlight.domain.Notification;
import com.tms.sportlight.dto.NotificationDTO;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

public SseEmitter subscribe();

  public Notification insertNotification(NotificationDTO notificationDTO);
  public List<NotificationDTO> findAllNotification();
  public List<NotificationDTO> findNotificationByUserId(long userId);
  public Notification modifyNotification(long od);
  public void removeNotification(long id);
  public void removeAllNotification();
  public void removeSelectedNotification(List<Long> idList);
  public void deleteData();

}
