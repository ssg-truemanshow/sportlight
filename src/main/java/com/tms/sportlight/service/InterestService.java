package com.tms.sportlight.service;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.Interest;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.InterestRepository;
import com.tms.sportlight.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final CourseRepository courseRepository;

    // 찜 추가/삭제
    @Transactional
    public boolean toggleInterest(User user, int courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));

        return interestRepository.findByUserIdAndCourseId(user.getId(), courseId)
            .map(interest -> {
                interestRepository.delete(interest);
                return false;
            })
            .orElseGet(() -> {
                Interest newInterest = Interest.builder()
                    .user(user)
                    .course(course)
                    .build();
                interestRepository.save(newInterest);
                return true;
            });
    }

    @Transactional(readOnly = true)
    public List<Course> getInterests(User user) {
        return interestRepository.findByUserId(user.getId()).stream()
            .map(Interest::getCourse)
            .collect(Collectors.toList());
    }
}
