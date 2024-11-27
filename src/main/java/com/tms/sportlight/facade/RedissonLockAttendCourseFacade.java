package com.tms.sportlight.facade;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.repository.AttendCourseRepository;
import com.tms.sportlight.repository.CourseRepository;
import com.tms.sportlight.repository.CourseScheduleRepository;
import com.tms.sportlight.service.AttendCourseService;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedissonLockAttendCourseFacade {
  private final RedissonClient redissonClient;
  private final AttendCourseService attendCourseService;

  public void lockCourse(Integer scheduleId, User user, Integer userCouponId, int participantNum, double finalAmount, LocalDateTime requestDateTime, LocalDateTime completeDate) {
    RLock lock = redissonClient.getLock(scheduleId.toString());

    try {
      boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
      if (!available) {
        throw new RuntimeException("정원이 다 찼습니다.");
      }

      attendCourseService.applyCourse(scheduleId, user, userCouponId, participantNum, finalAmount, requestDateTime, completeDate);

    } catch (InterruptedException e) {
      throw new RuntimeException("Thread interrupted while trying to acquire lock.", e);
    } finally {
      lock.unlock();
    }
  }
}
