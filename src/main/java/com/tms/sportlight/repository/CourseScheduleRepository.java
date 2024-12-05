package com.tms.sportlight.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.AttendCourseStatus;
import com.tms.sportlight.domain.CourseSchedule;
import com.tms.sportlight.domain.QAttendCourse;
import com.tms.sportlight.domain.QCourseSchedule;
import com.tms.sportlight.dto.CourseScheduleWithAttendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseScheduleRepository {

    private final JpaCourseScheduleRepository jpaCourseScheduleRepository;
    private final JPAQueryFactory queryFactory;

    public Optional<CourseSchedule> findById(int id) {
        return jpaCourseScheduleRepository.findById(id);
    }

    public void save(CourseSchedule courseSchedule) {
        jpaCourseScheduleRepository.save(courseSchedule);
    }

    public List<CourseSchedule> findByCourseId(int courseId) {
        return jpaCourseScheduleRepository.findByCourseId(courseId);
    }

    public List<LocalDate> findDateListByCourseId(int courseId, LocalDate startDate, LocalDate endDate) {
        return jpaCourseScheduleRepository.findDateListByCourseId(courseId, startDate, endDate).stream()
                .map(Date::toLocalDate)
                .toList();
    }

    public List<CourseSchedule> findByCourseIdAndStartDate(int courseId, LocalDate startDate) {
        return jpaCourseScheduleRepository.findByCourseIdAndStartDate(courseId, startDate);
    }

    public List<CourseSchedule> findByUserIdAndPeriod(long userId, LocalDate startDate, LocalDate endDate) {
        return jpaCourseScheduleRepository.findByUserIdAndPeriod(userId, startDate, endDate);
    }

    public List<CourseScheduleWithAttendDTO> findWithAttendsByCourseId(int courseId) {
        QCourseSchedule schedule = QCourseSchedule.courseSchedule;
        QAttendCourse attendCourse = QAttendCourse.attendCourse;

        List<CourseScheduleWithAttendDTO> participantNum = queryFactory.select(
                Projections.constructor(CourseScheduleWithAttendDTO.class,
                    schedule.id,
                    schedule.course.id,
                    schedule.startTime,
                    schedule.endTime,
                    attendCourse.participantNum.sum().coalesce(0).as("participantNum"),
                    schedule.remainedNum
                ))
            .from(schedule)
            .leftJoin(attendCourse).on(schedule.id.eq(attendCourse.courseSchedule.id).and(attendCourse.status.eq(
                AttendCourseStatus.APPROVED)))
            .where(schedule.course.id.eq(courseId))
            .groupBy(schedule.id)
            .orderBy(schedule.startTime.asc())
            .fetch();
        return participantNum;
    }

    public List<CourseSchedule> findByUserId(long id) {
        return jpaCourseScheduleRepository.findByUserId(id);
    }
}
