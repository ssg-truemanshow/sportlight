package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseReviewDTO {

  private Long userId;
  private String nickname;
//  private String imgUrl;
  private String content;
  private LocalDateTime regDate;
  private int rating;

  public static CourseReviewDTO fromEntity(Review review) {
    return CourseReviewDTO.builder()
        .userId(review.getUser().getId())
        .nickname(review.getUser().getUserNickname())
//        .imgUrl()//TODO : 이미지 링크 넣어줘야함
        .content(review.getContent())
        .regDate(review.getRegDate())
        .rating(review.getRating())
        .build();
  }
}
