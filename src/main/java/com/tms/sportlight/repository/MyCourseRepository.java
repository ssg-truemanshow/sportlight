package com.tms.sportlight.repository;

import com.tms.sportlight.domain.AttendCourse;
import com.tms.sportlight.domain.AttendCourseStatus;
import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCourseRepository extends JpaRepository<AttendCourse, Integer> {

    List<AttendCourse> findByUserId(Long userId);

    List<AttendCourse> findByUserIdAndStatus(Long userId, AttendCourseStatus status);


    Optional<AttendCourse> findByIdAndUserId(Integer id, Long userId);

    @Query("SELECT ac FROM AttendCourse ac " +
        "JOIN FETCH ac.courseSchedule cs " +
        "JOIN FETCH cs.course c " +
        "WHERE ac.user.id = :userId " +
        "ORDER BY cs.startTime DESC")
    List<AttendCourse> findAllWithCourseByUserId(@Param("userId") Long userId);
}