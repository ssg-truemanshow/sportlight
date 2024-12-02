package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c " +
            "WHERE c.user.id=:userId AND c.status != com.tms.sportlight.domain.CourseStatus.DELETED " +
            "ORDER BY c.regDate DESC")
    List<Course> findByUserId(@Param("userId") long userId);
}
