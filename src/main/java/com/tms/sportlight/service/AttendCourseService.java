package com.tms.sportlight.service;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.AttendCourseStatus;
import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseSchedule;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserCoupon;
import com.tms.sportlight.dto.ApplyCourseDTO;
import com.tms.sportlight.repository.AttendCourseRepository;
import com.tms.sportlight.repository.CourseRepository;
import com.tms.sportlight.repository.CourseScheduleRepository;
import com.tms.sportlight.repository.UserCouponRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendCourseService {

  private final CourseRepository courseRepository;
  private final CourseScheduleRepository scheduleRepository;
  private final AttendCourseRepository attendCourseRepository;
  private final UserCouponRepository userCouponRepository;

  @Transactional
  public void applyCourse(Integer scheduleId, User user, Integer userCouponId, int participantNum, double finalAmount, LocalDateTime requestDateTime, LocalDateTime completeDate) {
    CourseSchedule schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(() -> new RuntimeException("Schedule not found."));

    // Check if the course has enough capacity
    if (schedule.getRemainedNum() < participantNum) {
      throw new RuntimeException("정원이 다 찼습니다.");
    }

    // Update the course capacity
    schedule.updateRemainedNum(schedule.getRemainedNum() - participantNum);

    // Save the attend course details
    attendCourseRepository.save(
        AttendCourse.builder()
            .courseSchedule(schedule)
            .user(user)
            .userCoupon(userCouponRepository.findById(userCouponId).orElse(null))
            .participantNum(participantNum)
            .finalAmount(finalAmount)
            .totalAmount(finalAmount)
            .requestDate(LocalDateTime.now())
            .completeDate(LocalDateTime.now())
            .paymentFee(finalAmount * 25 / 1000)
            .status(AttendCourseStatus.APPROVED)
            .build()
    );
  }
}