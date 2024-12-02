package com.tms.sportlight.service;

import com.tms.sportlight.dto.ReviewCardDTO;
import com.tms.sportlight.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tms.sportlight.dto.CourseReviewDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;

  @Transactional(readOnly = true)
  public List<CourseReviewDTO> getReviews(Integer courseId) {
    return reviewRepository.findByCourseId(courseId);
  }

  @Transactional(readOnly = true)
  public List<ReviewCardDTO> getGoodReviews() {
    return reviewRepository.findGoodReviews();
  }
}
