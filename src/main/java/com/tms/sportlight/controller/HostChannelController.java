package com.tms.sportlight.controller;

import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.dto.HostCourseListDTO;
import com.tms.sportlight.dto.Id;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hosts")
@RequiredArgsConstructor
public class HostChannelController {

    private final CourseService courseService;

    @GetMapping("/courses")
    public DataResponse<List<HostCourseListDTO>> getCourses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<HostCourseListDTO> courseList = courseService.getCourseListByCreator(userDetails.getUser());
        return DataResponse.of(courseList);
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
}


