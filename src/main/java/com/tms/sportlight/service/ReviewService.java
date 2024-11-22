package com.tms.sportlight.service;

import com.tms.sportlight.dto.CourseReviewDTO;
import com.tms.sportlight.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  @Transactional(readOnly = true)
  public List<CourseReviewDTO> getReviews(Integer courseId) {
    return reviewRepository.findByCourseId(courseId);
  }
}
