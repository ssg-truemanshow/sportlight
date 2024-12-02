package com.tms.sportlight.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCardDTO {

  private Integer courseId;
  private String userNickname;
  private String courseTitle;
  private String content;
  private LocalDateTime regDate;

}
