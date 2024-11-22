package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Interest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Integer> {

    Optional<Interest> findByUserIdAndCourseId(Long userId, int courseId);

    List<Interest> findByUserId(Long userId);
}
