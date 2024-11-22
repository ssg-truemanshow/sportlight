package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Integer> {

  List<Review> findByUser(User user);

  Optional<Review> findByIdAndUser(Integer id, User user);

}