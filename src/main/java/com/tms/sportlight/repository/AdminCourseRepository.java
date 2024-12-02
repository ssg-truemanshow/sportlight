package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminCourseRepository extends JpaRepository<Course, Integer> {
    @Query(value = "SELECT start_time, COUNT(*) FROM course_schedule " +
            "WHERE start_time <= NOW() AND deleted = 0 " +
            "GROUP BY start_time " +
            "ORDER BY start_time", nativeQuery = true)
    List<Object[]> getOpenCourseCountsGroupedByStartTime();

    @Query(value = "SELECT c.course_title, c.course_latitude, c.course_longitude FROM course c", nativeQuery = true)
    List<Object[]> getAllCourseTitlesAndLocations();

    @Query(value = "SELECT cat.category_name, COUNT(c.course_id) AS courseCount " +
            "FROM course c " +
            "JOIN category cat ON c.category_id = cat.category_id " +
            "GROUP BY cat.category_name", nativeQuery = true)
    List<Object[]> getCategoryCourseCounts();

    @Query(value = "SELECT c.course_id, cat.category_name, c.course_title, c.course_tuition, c.max_capacity, c.status, c.reg_date " +
            "FROM course c " +
            "JOIN category cat ON c.category_id = cat.category_id " +
            "WHERE c.status IN ('APPROVAL_REQUEST', 'DELETION_REQUEST')", nativeQuery = true)
    List<Object[]> getAllCourseRequests();
}
