package com.tms.sportlight.controller;

import com.tms.sportlight.domain.CourseLevel;
import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.dto.SortType;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.CourseCardDTO;
import com.tms.sportlight.dto.CourseCreateDTO;
import com.tms.sportlight.dto.CourseScheduleDTO;
import com.tms.sportlight.dto.CourseUpdateDTO;
import com.tms.sportlight.dto.Id;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.dto.common.PageRequestDTO;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.AdminService;
import com.tms.sportlight.service.CourseService;
import com.tms.sportlight.service.FileService;
import com.tms.sportlight.service.QuestionService;
import com.tms.sportlight.service.ReviewService;
import com.tms.sportlight.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;
  private final FileService fileService;
  private final ReviewService reviewService;
  private final QuestionService questionService;
  private final UserService userService;
  private final AdminService adminService;

  @GetMapping("/popular")
  public List<CourseCardDTO> getPopularCourses() {
    return courseService.getPopularCourses();
  }

  @GetMapping("/reviews/good")
  public DataResponse<List<ReviewCardDTO>> getGoodReviews() {
    return DataResponse.of(reviewService.getGoodReviews());
  }

  @GetMapping("/users/count")
  public DataResponse<Long> getUsersCount() {
    return DataResponse.of(userService.getUsersCount());
  }

  @GetMapping("/courses/beginner")
  public DataResponse<List<CourseCardDTO>> getBeginnerCourses() {
    return DataResponse.of(courseService.getBeginnerCourses());
  }

//  @GetMapping("/courses/recommend")
//  public DataResponse<List<CourseCardDTO>> getRecommendCourses() {
//    return DataResponse.of(courseService.getRecommendCourses());
//  }

  @GetMapping("/courses/list")
  public PageResponse<CourseCardDTO> getCourses(
      @RequestParam(required = false) List<Integer> categories,
      @RequestParam(required = false) List<String> levels,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(required = false) Integer participants,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) String searchText,
      @RequestParam(defaultValue = "POPULARITY") SortType sortType,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {

    PageRequestDTO<Object> pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();
    List<CourseLevel> levelList = null;
    if (levels != null && !levels.isEmpty()) {
      levelList = levels.stream().map(CourseLevel::valueOf).toList();
    }

    List<CourseCardDTO> dtoList = courseService.searchCourses(categories, levelList,
        minPrice, maxPrice, participants,
        startDate, endDate, latitude, longitude, searchText, sortType, pageRequestDTO);
    int totalCount = courseService.searchCoursesCount(categories, levelList, minPrice, maxPrice,
        participants, startDate, endDate, latitude, longitude, searchText).intValue();
    return new PageResponse<>(pageRequestDTO, dtoList, totalCount);
  }

  @GetMapping("/courses/{id}")
  public DataResponse<CourseDetailDTO> getCourseDetail(@PathVariable Id id) {
    return DataResponse.of(courseService.getCourseDetail(id.getId()));
  }

  @GetMapping("/courses/{id}/reviews")
  public DataResponse<List<CourseReviewDTO>> getCourseReviews(@PathVariable Id id) {
    return DataResponse.of(reviewService.getReviews(id.getId()));
  }

  @GetMapping("/courses/{id}/qnas")
  public DataResponse<List<CourseQuestionDTO>> getCourseQuestions(@PathVariable Id id) {
    return DataResponse.of(questionService.getQuestions(id.getId()));
  }

  @GetMapping("/courses/{id}/schedules")
  public DataResponse<List<CourseScheduleWithAttendDTO>> getCourseSchedules(@PathVariable Id id) {
    return DataResponse.of(courseService.getScheduleListByCourseId(id.getId()));
  }

  @GetMapping("/schedules/{id}")
  public DataResponse<CourseScheduleDetailDTO> getScheduleDetails(@PathVariable Id id) {
    return DataResponse.of(courseService.getCourseScheduleDetail(id.getId()));
  }

  @PatchMapping("/courses/{id}")
  public DataResponse<Void> modify(@PathVariable Id id,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid CourseUpdateDTO updateDTO) {
    courseService.updateCourse(id.getId(), userDetails.getUser(), updateDTO);
    fileService.saveCourseMainImageFile(id.getId(), updateDTO.getNewMainImage());
    updateDTO.getDeletedImages().forEach(fileService::deleteFile);
    fileService.saveCourseImageFiles(id.getId(), updateDTO.getNewImages());
    return DataResponse.empty();
  }

  @DeleteMapping("/courses/{id}")
  public DataResponse<Void> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Id id) {
    courseService.updateCourseStatus(id.getId(), userDetails.getUser(), CourseStatus.DELETED);
    return DataResponse.empty();
  }

  @DeleteMapping("/schedules/{id}")
  public DataResponse<Void> deleteCourseSchedule(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
    courseService.deleteCourseSchedule(id.getId(), userDetails.getUser());
    return DataResponse.empty();
  }
}
