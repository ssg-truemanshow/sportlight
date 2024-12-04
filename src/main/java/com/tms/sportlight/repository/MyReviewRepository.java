package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.regDate DESC")
    List<Review> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Review r WHERE r.id = :reviewId AND r.user.id = :userId")
    Optional<Review> findByIdAndUserId(@Param("reviewId") Integer reviewId, @Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.course.id = :courseId AND r.user.id = :userId")
    boolean existsByCourseIdAndUserId(@Param("courseId") Integer courseId, @Param("userId") Long userId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r WHERE r.course.id = :courseId")
    double getAverageRatingByCourseId(@Param("courseId") Integer courseId);

    long countByCourseId(Integer courseId);

}
