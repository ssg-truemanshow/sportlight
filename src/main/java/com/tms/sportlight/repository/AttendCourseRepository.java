package com.tms.sportlight.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.AttendCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendCourseRepository {

  private final JpaAttendCourseRepository jpaAttendRepository;
  private final JPAQueryFactory queryFactory;


  public int save(AttendCourse attendCourse) {
    return jpaAttendRepository.save(attendCourse).getId();
  }


}
