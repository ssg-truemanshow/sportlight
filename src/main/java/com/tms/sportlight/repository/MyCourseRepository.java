package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT COUNT(c) FROM Course c WHERE c.user = :user AND c.status = :status")
    int countByUserAndStatus(@Param("user") User user, @Param("status") CourseStatus status);


}