package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendCourseRepository {

  private final JpaAttendCourseRepository jpaAttendRepository;
  private final UserCouponRepository userCouponRepository;

  public void saveAndFlush(AttendCourse attendCourse) {
    UserCoupon userCoupon = attendCourse.getUserCoupon();
    if (userCoupon != null) userCouponRepository.useCoupon(userCoupon.getId());
    jpaAttendRepository.saveAndFlush(attendCourse);
  }

  public List<AttendCourse> findByCourseScheduleId(int courseScheduleId, Pageable pageable) {
    return jpaAttendRepository.findByCourseScheduleIdOrderByRequestDateDesc(courseScheduleId, pageable);
  }

  public int countByCourseScheduleId(int courseScheduleId) {
    return jpaAttendRepository.countByCourseScheduleId(courseScheduleId);
  }


}
