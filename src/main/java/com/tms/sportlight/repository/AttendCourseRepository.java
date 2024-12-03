package com.tms.sportlight.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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


}
