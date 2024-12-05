package com.tms.sportlight.controller;

import com.tms.sportlight.domain.AttendCourseStatus;
import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.dto.common.PageRequestDTO;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.AttendCourseService;
import com.tms.sportlight.service.CourseService;
import com.tms.sportlight.service.FileService;
import com.tms.sportlight.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hosts")
@RequiredArgsConstructor
public class HostChannelController {

    private final CourseService courseService;
    private final AttendCourseService attendCourseService;
    private final UserService userService;
    private final FileService fileService;

    @GetMapping("/info")
    public DataResponse<HostInfoDTO> getHostInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HostInfoDTO hostInfo = userService.getHostInfoDTO(userDetails.getUser());
        return DataResponse.of(hostInfo);
    }

    @PatchMapping("/info")
    public DataResponse<Void> getHostInfo(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid HostInfoDTO hostInfo) {
        userService.updateHostInfo(userDetails.getUser(), hostInfo);
        return DataResponse.empty();
    }

    @GetMapping("/courses/{id}")
    public DataResponse<CourseInfoDTO> getCourses(@PathVariable Id id) {
        CourseInfoDTO courseInfo = courseService.getCourseInfo(id.getId());
        return DataResponse.of(courseInfo);
    }

    @GetMapping("/courses")
    public DataResponse<List<HostCourseListDTO>> getCourses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<HostCourseListDTO> courseList = courseService.getCourseListByCreator(userDetails.getUser());
        return DataResponse.of(courseList);
    }


    @PostMapping("/courses")
    public DataResponse<Id> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @Valid CourseCreateDTO createDTO) {
        log.info("{}", createDTO);
        int id = courseService.saveCourse(userDetails.getUser(), createDTO);
        fileService.saveCourseMainImageFile(id, createDTO.getMainImage());
        fileService.saveCourseImageFiles(id, createDTO.getImages());
        return DataResponse.of(new Id(id));
    }

    @PostMapping("/courses/{id}/schedules")
    public DataResponse<Void> createCourseSchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Id id, @Valid @RequestBody List<CourseScheduleDTO> schedules) {
        log.info("{}", schedules);
        courseService.saveCourseSchedules(id.getId(), userDetails.getUser(), schedules);
        return DataResponse.empty();
    }

    @PatchMapping("/courses/{id}/status")
    public DataResponse<Void> modifyStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Id id,
                                           @RequestBody CourseStatus status) {
        log.info("modifyStatus={}", status);
        if(status == null) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE);
        }
        courseService.updateCourseStatus(id.getId(), userDetails.getUser(), status);
        return DataResponse.empty();
    }

    @GetMapping("/schedules")
    public DataResponse<List<CourseCalendarDTO>> getScheduledDate(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CourseCalendarDTO> scheduleList = courseService.getScheduleListByUser(userDetails.getUser());
        return DataResponse.of(scheduleList);
    }

    @GetMapping("/courses/{id}/scheduled-date")
    public DataResponse<List<LocalDate>> getScheduledDate(@PathVariable Id id,
                                                          @RequestParam LocalDate startDate,
                                                          @RequestParam LocalDate endDate) {
        log.info("startDate={}, endDate={}", startDate, endDate);
        return DataResponse.of(courseService.getSchduledDateList(id.getId(), startDate, endDate));
    }

    @GetMapping("/courses/{id}/schedules")
    public DataResponse<List<CourseScheduleDTO>> getSchedules(@PathVariable Id id, @RequestParam LocalDate date) {
        return DataResponse.of(courseService.getScheduleListByDate(id.getId(), date));
    }

    @GetMapping("/schedules/{id}/users")
    public PageResponse<CourseApplicantDTO> getCourseApplicantList(@PathVariable Id id,
                                                                   @Valid PageRequestDTO<CourseApplicantSearchCond> pageRequestDTO,
                                                                   AttendCourseStatus status) {
        CourseApplicantSearchCond searchCond = new CourseApplicantSearchCond(status);
        pageRequestDTO.setSearchCond(searchCond);
        List<CourseApplicantDTO> dtoList = attendCourseService.getCourseApplicantList(id.getId(), pageRequestDTO);
        int total = attendCourseService.getCountByCourseScheduleId(id.getId(), pageRequestDTO);
        return new PageResponse<>(pageRequestDTO, dtoList, total);
    }

    @PostMapping("/schedules/{id}/close")
    public DataResponse<Void> closeCourseSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        courseService.closeSchedule(userDetails.getUser(), id.getId());
        return DataResponse.empty();
    }

}





