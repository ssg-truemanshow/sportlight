package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {

  @Query("select n from Notification n order by n.createdAt desc")
  public List<Notification> findAll();

}
