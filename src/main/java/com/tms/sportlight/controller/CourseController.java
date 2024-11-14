package com.tms.sportlight.controller;

import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.dto.CourseCreateDTO;
import com.tms.sportlight.dto.CourseScheduleDTO;
import com.tms.sportlight.dto.CourseUpdateDTO;
import com.tms.sportlight.dto.Id;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.CourseService;
import com.tms.sportlight.service.FileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final FileService fileService;

    @PostMapping("/courses")
    public DataResponse<Id> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @Valid CourseCreateDTO createDTO) {
        int id = courseService.saveCourse(userDetails.getUser(), createDTO);
        fileService.saveCourseMainImageFile(id, createDTO.getMainImage());
        fileService.saveCourseImageFiles(id, createDTO.getImages());
        return DataResponse.of(new Id(id));
    }

    @PostMapping("/courses/{id}/schedules")
    public DataResponse<Void> createCourseSchedules(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Id id, @Valid List<CourseScheduleDTO> createDTOList) {
        courseService.saveCourseSchedules(id.getId(), userDetails.getUser(), createDTOList);
        return DataResponse.empty();
    }

    @GetMapping("/courses/{id}/schedules")
    public DataResponse<List<CourseScheduleDTO>> getCourseSchedules(@PathVariable Id id) {
        List<CourseScheduleDTO> scheduleList = courseService.getScheduleListByCourseId(id.getId());
        return DataResponse.of(scheduleList);
    }

    @PatchMapping("/courses/{id}")
    public DataResponse<Void> modify(@PathVariable Id id, @AuthenticationPrincipal CustomUserDetails userDetails,
                                     @Valid CourseUpdateDTO updateDTO) {
        courseService.updateCourse(id.getId(), userDetails.getUser(), updateDTO);
        fileService.saveCourseMainImageFile(id.getId(), updateDTO.getNewMainImage());
        updateDTO.getDeletedImages().forEach(fileService::deleteFile);
        fileService.saveCourseImageFiles(id.getId(), updateDTO.getNewImages());
        return DataResponse.empty();
    }

    @PatchMapping("/courses/{id}/approval")
    public DataResponse<Void> approve(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        courseService.updateCourseStatus(id.getId(), userDetails.getUser(), CourseStatus.APPROVED);
        return DataResponse.empty();
    }

    @PatchMapping("/courses/{id}/dormancy")
    public DataResponse<Void> dormancy(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        courseService.updateCourseStatus(id.getId(), userDetails.getUser(), CourseStatus.DORMANCY);
        return DataResponse.empty();
    }

    @DeleteMapping("/courses/{id}/request")
    public DataResponse<Void> requestDeletion(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        courseService.updateCourseStatus(id.getId(), userDetails.getUser(), CourseStatus.DELETION_REQUEST);
        return DataResponse.empty();
    }

    @DeleteMapping("/courses/{id}")
    public DataResponse<Void> delete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        courseService.updateCourseStatus(id.getId(), userDetails.getUser(), CourseStatus.DELETED);
        return DataResponse.empty();
    }

    @DeleteMapping("/schedules/{id}")
    public DataResponse<Void> deleteCourseSchedule(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Id id) {
        courseService.deleteCourseSchedule(id.getId(), userDetails.getUser());
        return DataResponse.empty();
    }

}
