package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewDTO {

    private String courseTitle;
    private String content;
    private int rating;
    private LocalDateTime regDate;

    public static MyReviewDTO fromEntity(Review review) {
        return MyReviewDTO.builder()
            .courseTitle(review.getCourse().getTitle())
            .content(review.getContent())
            .rating(review.getRating())
            .regDate(review.getRegDate())
            .build();
    }

    public Review toEntity(Course course, User user) {
        return Review.builder()
            .course(course)
            .user(user)
            .content(this.content)
            .rating(this.rating)
            .regDate(LocalDateTime.now())
            .build();
    }


}
