package com.tms.sportlight.service;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.AttendCourseStatus;
import com.tms.sportlight.domain.CourseSchedule;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserCoupon;
import com.tms.sportlight.repository.AttendCourseRepository;
import com.tms.sportlight.repository.CourseScheduleRepository;
import com.tms.sportlight.repository.UserCouponRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendCourseService {

  private final CourseScheduleRepository scheduleRepository;
  private final AttendCourseRepository attendCourseRepository;
  private final UserCouponRepository userCouponRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void decrease(Integer scheduleId, User user, Integer userCouponId, int participantNum, double finalAmount) {
    CourseSchedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new RuntimeException("Schedule not found."));

    // Update the course capacity
    schedule.decreaseRemainedNum(participantNum);

    UserCoupon userCoupon = null;
    if (userCouponId != null) {
      userCoupon = userCouponRepository.findById(userCouponId)
          .orElseThrow(() -> new RuntimeException("UserCoupon not found."));
    }

    // Save the attend course details
    attendCourseRepository.saveAndFlush(
        AttendCourse.builder()
            .courseSchedule(schedule)
            .user(user)
            .userCoupon(userCoupon)
            .participantNum(participantNum)
            .finalAmount(finalAmount)
            .totalAmount(finalAmount)
            .requestDate(LocalDateTime.now())
            .completeDate(LocalDateTime.now())
            .paymentFee(finalAmount * 25 / 1000)
            .regDate(LocalDateTime.now())
            .status(AttendCourseStatus.APPROVED)
            .build()
    );
  }
}