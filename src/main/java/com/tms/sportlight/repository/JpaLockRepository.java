package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaLockRepository extends JpaRepository<AttendCourse, Long> {
  @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
  void getLock(String key);

  @Query(value = "select release_lock(:key)", nativeQuery = true)
  void releaseLock(String key);
}