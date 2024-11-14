package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationRepository {

  @PersistenceContext
  EntityManager em;

  JpaNotificationRepository jpaNotificationRepository;

  public void save(Notification notification) {
    em.persist(notification);
  }

  public List<Notification> findAll() {
    return em.createQuery("select n from Notification n", Notification.class).getResultList();
  }

  public void update(long id) {
    Notification noti = em.find(Notification.class, id);

  }

  public void delete(long id) {
    Notification noti = em.find(Notification.class, id);
    em.remove(noti);
  }

  public void deleteAll() {

  }
}
