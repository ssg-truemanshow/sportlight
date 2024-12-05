package com.tms.sportlight.controller;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.NotiGrade;
import com.tms.sportlight.domain.NotiType;
import com.tms.sportlight.dto.ReserveCourseDTO;
import com.tms.sportlight.facade.RedissonLockAttendCourseFacade;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.CourseService;
import com.tms.sportlight.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
  private final RedissonLockAttendCourseFacade redissonLockAttendCourseFacade;
  private final NotificationService notificationService;
  private final CourseService courseService;

  @PostMapping("/confirm/widget")
  public ResponseEntity<JSONObject> confirmPayment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReserveCourseDTO reserveCourseDTO) {
    String jsonBody = reserveCourseDTO.getJsonBody();
    ResponseEntity<JSONObject> decreaseResult = redissonLockAttendCourseFacade.decrease(jsonBody, userDetails.getUser(), reserveCourseDTO);
    Course course = courseService.getCourseSchedule(reserveCourseDTO.getScheduleId()).getCourse();
    createAttendCourseNotification(course);
    return decreaseResult;
  }

  private void createAttendCourseNotification(Course course) {
    long courseHostId = course.getUser().getId();
    notificationService.insertNotification(courseHostId, "수강 신청", "[" + course.getTitle() + "] 클래스를 회원이 수강 신청하였습니다.", NotiType.COURSE, NotiGrade.HOST);
  }
}
