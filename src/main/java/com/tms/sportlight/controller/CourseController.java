package com.tms.sportlight.controller;

import com.tms.sportlight.dto.CourseCardDTO;
import com.tms.sportlight.service.CourseService;
import com.tms.sportlight.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

  private final CourseService courseService;
  private final UserService userService;

  @GetMapping("/popular")
  public List<CourseCardDTO> getPopularCourses() {
    return courseService.getPopularCourses();
  }

}
