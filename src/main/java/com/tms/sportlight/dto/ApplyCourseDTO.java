package com.tms.sportlight.dto;

import com.tms.sportlight.domain.AttendCourse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class ApplyCourseDTO {

  @Positive
  private Integer scheduleId;

  @Positive
  private Integer participantNum;

  @NotNull
  private LocalDateTime dateTime;

}
