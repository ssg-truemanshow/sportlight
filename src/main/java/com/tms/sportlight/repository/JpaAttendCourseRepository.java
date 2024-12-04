package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAttendCourseRepository extends JpaRepository<AttendCourse, Integer> {

    List<AttendCourse> findByCourseScheduleIdOrderByRequestDateDesc(int courseScheduleId, Pageable pageable);
    int countByCourseScheduleId(int courseScheduleId);

}
