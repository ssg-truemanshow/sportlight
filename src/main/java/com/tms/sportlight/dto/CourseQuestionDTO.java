package com.tms.sportlight.dto;

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
public class CourseQuestionDTO {
  private Integer qId;
  private Long qUserId;
  private String qUserNickname;
//  private String qImgUrl; TODO
  private String qContent;
  private LocalDateTime qRegDate;
  private Integer aId;
  private String aContent;
  private LocalDateTime aRegDate;
}
