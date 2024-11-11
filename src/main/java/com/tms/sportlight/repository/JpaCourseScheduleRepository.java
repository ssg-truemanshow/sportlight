package com.tms.sportlight.repository;

import com.tms.sportlight.domain.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCourseScheduleRepository extends JpaRepository<CourseSchedule, Integer> {

    List<CourseSchedule> findByCourseId(int courseId);
}
