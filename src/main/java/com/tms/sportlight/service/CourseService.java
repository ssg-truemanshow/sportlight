package com.tms.sportlight.service;

import com.tms.sportlight.domain.*;
import com.tms.sportlight.dto.CourseCardDTO;
import com.tms.sportlight.dto.CourseCreateDTO;
import com.tms.sportlight.dto.CourseScheduleDTO;
import com.tms.sportlight.dto.CourseUpdateDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CourseRepository;
import com.tms.sportlight.repository.CourseScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CategoryService categoryService;
    private final CourseRepository courseRepository;
    private final CourseScheduleRepository courseScheduleRepository;

    /**
     * 클래스 엔티티 단일 조회
     *
     * @param id 클래스 id
     * @return 클래스 엔티티
     */
    @Transactional(readOnly = true)
    protected Course getCourse(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));
    }

    /**
     * 클래스 스케줄 DTO 단일 조회
     *
     * @param id 클래스 스케줄
     * @return 클래스 스케줄 DTO
     */
    @Transactional(readOnly = true)
    public CourseScheduleDTO getCourseSchedule(int id) {
        CourseSchedule courseSchedule = courseScheduleRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE_SCHEDULE));
        return CourseScheduleDTO.fromEntity(courseSchedule);
    }

    /**
     * 클래스 id로 클래스 스케줄 DTO 리스트 조회
     *
     * @param courseId 클래스 id
     * @return 클래스 스케줄 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<CourseScheduleDTO> getScheduleListByCourseId(int courseId) {
        return courseScheduleRepository.findByCourseId(courseId).stream()
                .filter(entity -> !entity.isDeleted())
                .map(CourseScheduleDTO::fromEntity)
                .toList();
    }

    /**
     * 회원이 생성한 클래스 목록 DTO 조회
     *
     * @param user 클래스 개설 회원 id
     * @return 클래스 목록 DTO
     */
    @Transactional(readOnly = true)
    public List<Course> getCourseListByUserId(User user) {
        // TODO 목록 DTO로 변환
        return courseRepository.findByUserId(user.getId());
    }

    /**
     * 가장 인기있는 클래스 목록 DTO 조회
     *
     * @return 클래스 목록 DTO
     */
    @Transactional(readOnly = true)
    public List<CourseCardDTO> getPopularCourses() {
        return courseRepository.findPopularCourses();
    }

    /**
     * 클래스 생성
     * 요청 회원의 HOST 권한 확인
     *
     * @param user 요청 회원
     * @param createDTO 클래스 생성 DTO
     * @return 생성된 클래스의 id
     */
    public int saveCourse(User user, CourseCreateDTO createDTO) {
        if(!user.getRoles().contains(UserRole.HOST)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        Category category = categoryService.get(createDTO.getCategoryId());
        return courseRepository.save(createDTO.toEntity(user, category));
    }

    /**
     * 클래스 스케줄 생성
     * 요청 회원과 클래스 개설자 일치 여부 확인
     *
     * @param courseId 클래스 id
     * @param user 요청 회원
     * @param scheduleDTOList 클래스 스케줄 DTO
     */
    public void saveCourseSchedules(int courseId, User user, List<CourseScheduleDTO> scheduleDTOList) {
        Course course = getCourse(courseId);
        verifyCourseCreator(course, user);
        for(CourseScheduleDTO scheduleDTO : scheduleDTOList) {
            courseScheduleRepository.save(scheduleDTO.toEntity(course));
        }
    }

    /**
     * 클래스 정보 수정
     *
     * @param id 클래스 id
     * @param dto 클래스 수정 DTO
     */
    public void updateCourse(int id, User user, CourseUpdateDTO dto) {
        Course course = getCourse(id);
        verifyCourseCreator(course, user);
        course.updateCourse(dto.getTitle(), dto.getContent(), dto.getTuition(), dto.getDiscountRate(),
                dto.getLevel(), dto.getAddress(), dto.getDetailAddress(), dto.getLatitude(), dto.getLongitude(),
                dto.getTime(), dto.getMaxCapacity(), dto.getMinDaysPriorToReservation());
    }

    /**
     * 클래스 상태 수정
     *
     * @param id 클래스 id
     * @param user 요청 회원
     * @param status 변경할 클래스 상태
     */
    public void updateCourseStatus(int id, User user, CourseStatus status) {
        Course course = getCourse(id);
        // TODO 권한 확인
        course.updateStatus(status);
    }

    /**
     * 클래스 스케줄 삭제
     * 요청 회원과 클래스 개설자 일치 여부 확인
     *
     * @param id 클래스 스케줄 id
     * @param user 요청 회원
     */
    public void deleteCourseSchedule(int id, User user) {
        CourseSchedule schedule = courseScheduleRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE_SCHEDULE));
        verifyCourseCreator(schedule.getCourse(), user);
        schedule.deleteCourseSchedule();
    }

    /**
     * 회원이 클래스의 개설자인지 확인
     *
     * @param course 클래스
     * @param user 검증 회원
     */
    private void verifyCourseCreator(Course course, User user) {
        try {
            if(!Objects.equals(course.getUser().getId(), user.getId())) {
                throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
