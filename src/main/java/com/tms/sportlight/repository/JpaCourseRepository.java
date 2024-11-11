package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByUserId(long userId);
}
