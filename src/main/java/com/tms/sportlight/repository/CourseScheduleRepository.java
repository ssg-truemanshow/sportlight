package com.tms.sportlight.repository;

import com.tms.sportlight.domain.CourseSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseScheduleRepository {

    private final JpaCourseScheduleRepository jpaCourseScheduleRepository;

    public Optional<CourseSchedule> findById(int id) {
        return jpaCourseScheduleRepository.findById(id);
    }

    public void save(CourseSchedule courseSchedule) {
        jpaCourseScheduleRepository.save(courseSchedule);
    }

    public List<CourseSchedule> findByCourseId(int courseId) {
        return jpaCourseScheduleRepository.findByCourseId(courseId);
    }
}
