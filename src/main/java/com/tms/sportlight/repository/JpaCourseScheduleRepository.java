package com.tms.sportlight.repository;

import com.tms.sportlight.domain.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaCourseScheduleRepository extends JpaRepository<CourseSchedule, Integer> {

    List<CourseSchedule> findByCourseId(int courseId);

    @Query("SELECT cs FROM CourseSchedule cs WHERE cs.course.id=:courseId AND date(cs.startTime)=:startDate ORDER BY cs.startTime")
    List<CourseSchedule> findByCourseIdAndStartDate(@Param("courseId") int courseId, @Param("startDate") LocalDate startDate);

    @Query(value = "SELECT DATE(cs.start_time) FROM course_schedule cs WHERE cs.course_id=:courseId AND cs.start_time BETWEEN :startDate AND :endDate GROUP BY DATE(cs.start_time) ORDER BY cs.start_time", nativeQuery = true)
    List<Date> findDateListByCourseId(@Param("courseId") int courseId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT cs FROM CourseSchedule cs WHERE cs.course.user.id=:userId AND date(cs.startTime) BETWEEN :startDate AND :endDate ORDER BY cs.startTime")
    List<CourseSchedule> findByUserIdAndPeriod(@Param("userId") long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT cs FROM CourseSchedule cs WHERE cs.course.user.id=:userId ORDER BY cs.startTime")
    List<CourseSchedule> findByUserId(@Param("userId") long id);
}
