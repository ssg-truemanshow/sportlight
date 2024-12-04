package com.tms.sportlight.mapper;

import com.tms.sportlight.dto.CourseApplicantDTO;
import com.tms.sportlight.dto.CourseApplicantSearchCond;
import com.tms.sportlight.dto.common.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttendCourseMapper {

    List<CourseApplicantDTO> findCourseApplicantList(@Param("courseScheduleId") int courseScheduleId,
                                                     @Param("pageRequestDTO") PageRequestDTO<CourseApplicantSearchCond> pageRequestDTO);

    int getCourseApplicantCount(@Param("courseScheduleId") int courseScheduleId,
                                @Param("pageRequestDTO") PageRequestDTO<CourseApplicantSearchCond> pageRequestDTO);
}
