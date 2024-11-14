package com.tms.sportlight.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.QAttendCourse;
import com.tms.sportlight.domain.QCategory;
import com.tms.sportlight.domain.QCourse;
import com.tms.sportlight.domain.QCourseSchedule;
import com.tms.sportlight.domain.QReview;
import com.tms.sportlight.domain.QUser;
import com.tms.sportlight.dto.CourseCardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepository {

  private final JpaCourseRepository jpaCourseRepository;
  private final JPAQueryFactory queryFactory;

  public Optional<Course> findById(int id) {
    return jpaCourseRepository.findById(id);
  }

  public List<Course> findByUserId(long userId) {
    return jpaCourseRepository.findByUserId(userId);
  }

  public List<CourseCardDTO> findPopularCourses() {
    QCourse course = QCourse.course;
    QCourseSchedule schedule = QCourseSchedule.courseSchedule;
    QAttendCourse attendCourse = QAttendCourse.attendCourse;
    QCategory category = QCategory.category;
    QUser user = QUser.user;

    return queryFactory
        .select(Projections.fields(CourseCardDTO.class,
            course.id,
            user.userNickname.as("nickname"),
            course.title,
            course.address,
            course.tuition,
            course.discountRate,
            course.time,
            course.level,
            category.name.as("category")
        ))
        .from(course)
        .leftJoin(schedule).on(course.id.eq(schedule.course.id))
        .leftJoin(attendCourse).on(schedule.id.eq(attendCourse.courseSchedule.id))
        .leftJoin(course.category, category)
        .leftJoin(course.user, user)
        .groupBy(course.id)
        .orderBy(attendCourse.participantNum.sum().desc())
        .limit(10)
        .fetch();
  }

  public int save(Course course) {
    return jpaCourseRepository.save(course).getId();
  }
}
