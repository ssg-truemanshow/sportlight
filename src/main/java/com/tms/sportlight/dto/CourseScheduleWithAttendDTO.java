package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseSchedule;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourseScheduleWithAttendDTO {

  private Integer id;
  private Integer courseId;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Integer participantNum;
}
