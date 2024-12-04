package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendCourseRepository {

  private final JpaAttendCourseRepository jpaAttendRepository;

  public int save(AttendCourse attendCourse) {
    return jpaAttendRepository.save(attendCourse).getId();
  }

  public List<AttendCourse> findByCourseScheduleId(int courseScheduleId, Pageable pageable) {
    return jpaAttendRepository.findByCourseScheduleIdOrderByRequestDateDesc(courseScheduleId, pageable);
  }

  public int countByCourseScheduleId(int courseScheduleId) {
    return jpaAttendRepository.countByCourseScheduleId(courseScheduleId);
  }


}
