package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAttendCourseRepository extends JpaRepository<AttendCourse, Integer> {

}
