package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

    private final JpaCourseRepository jpaCourseRepository;

    public Optional<Course> findById(int id) {
        return jpaCourseRepository.findById(id);
    }

    public List<Course> findByUserId(long userId) {
        return jpaCourseRepository.findByUserId(userId);
    }

    public int save(Course course) {
        return jpaCourseRepository.save(course).getId();
    }
}
