package com.tms.sportlight.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseLevel;
import com.tms.sportlight.domain.QAttendCourse;
import com.tms.sportlight.domain.QCategory;
import com.tms.sportlight.domain.QCourse;
import com.tms.sportlight.domain.QCourseSchedule;
import com.tms.sportlight.domain.QHostInfo;
import com.tms.sportlight.domain.QReview;
import com.tms.sportlight.domain.QUser;
import com.tms.sportlight.dto.SortType;
import com.tms.sportlight.dto.CourseCardDTO;
import com.tms.sportlight.dto.CourseDetailDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    QReview review = QReview.review;

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
            category.name.as("category"),
            ExpressionUtils.as(
                JPAExpressions.select(MathExpressions.round(review.rating.avg(), 2))
                    .from(review)
                    .where(review.course.id.eq(course.id)),
                "rating"
            ),
            ExpressionUtils.as(
                JPAExpressions.select(review.rating.count())
                    .from(review)
                    .where(review.course.id.eq(course.id)),
                "reviewCount"
            )
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

  public List<CourseCardDTO> searchCourses(
      List<Integer> categories,
      List<CourseLevel> levels,
      Double minPrice,
      Double maxPrice,
      Integer participants,
      LocalDate startDate,
      LocalDate endDate,
      Double latitude,
      Double longitude,
      String searchText,
      SortType sortType
  ) {
    QCourse course = QCourse.course;
    QCategory category = QCategory.category;
    QReview review = QReview.review;
    QCourseSchedule schedule = QCourseSchedule.courseSchedule;
    QAttendCourse attendCourse = QAttendCourse.attendCourse;
    QUser user = QUser.user;

    BooleanBuilder whereClause = new BooleanBuilder();

    // 필터 조건
    if (categories != null && !categories.isEmpty()) {
      whereClause.and(course.category.id.in(categories));
    }
    if (levels != null && !levels.isEmpty()) {
      whereClause.and(course.level.in(levels));
    }
    if (minPrice != null) {
      whereClause.and(course.tuition.goe(minPrice));
    }
    if (maxPrice != null) {
      whereClause.and(course.tuition.loe(maxPrice));
    }
    if (participants != null) {
      whereClause.and(course.maxCapacity.goe(participants));
    }
    if (startDate != null && endDate != null) {
      LocalDateTime startDateTime = startDate.atStartOfDay();
      LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

      whereClause.and(schedule.startTime.between(startDateTime, endDateTime));
    }

    // 검색 조건
    OrderSpecifier<?> orderBy = null;
    if (sortType == SortType.POPULARITY) {
      orderBy = attendCourse.participantNum.sum().desc();
    } else if (sortType == SortType.NEWEST) {
      orderBy = course.regDate.desc();  // 최신순
    } else if (sortType == SortType.RATING) {
      orderBy = review.rating.avg().desc();  // TODO : 이 아래로 정렬 조건들 수정 필요 별점순
    } else if (sortType == SortType.REVIEW_COUNT) {
      orderBy = review.count().desc();  // 리뷰수 순
    } else if (sortType == SortType.DISTANCE) {
      orderBy = distanceExpression(latitude, longitude, course.latitude,
          course.longitude).asc();  // 거리순
    }

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
            category.name.as("category"),
            ExpressionUtils.as(
                JPAExpressions.select(MathExpressions.round(review.rating.avg(), 2))
                    .from(review)
                    .where(review.course.id.eq(course.id)),
                "rating"
            ),
            ExpressionUtils.as(
                JPAExpressions.select(review.rating.count())
                    .from(review)
                    .where(review.course.id.eq(course.id)),
                "reviewCount"
            )
        ))
        .from(course)
        .leftJoin(schedule).on(course.id.eq(schedule.course.id))
        .leftJoin(attendCourse).on(schedule.id.eq(attendCourse.courseSchedule.id))
        .leftJoin(course.category, category)
        .leftJoin(course.user, user)
        .where(whereClause)
        .groupBy(course.id)
        .orderBy(orderBy)
        .limit(10)
        .fetch();
  }

  // 거리 계산 Expression 예제 (QueryDSL용)
  private NumberExpression<Double> distanceExpression(double userLat, double userLng,
      NumberPath<Double> lat, NumberPath<Double> lng) {
    double earthRadius = 6371; // 지구 반지름 (km)
    return Expressions.numberTemplate(Double.class,
        "{0} * acos(cos(radians({1})) * cos(radians({2})) * cos(radians({3}) - radians({4})) + sin(radians({1})) * sin(radians({2})))",
        earthRadius, userLat, lat, lng, userLng);
  }

  public CourseDetailDTO findCourseById(Integer courseId) {
    QCourse course = QCourse.course;
    QCategory category = QCategory.category;
    QHostInfo hostInfo = QHostInfo.hostInfo;

    return queryFactory.select(Projections.fields(CourseDetailDTO.class,
            course.id.as("id"),
            course.title.as("title"),
            course.content.as("content"),
            category.name.as("category"),
            course.tuition.as("tuition"),
            course.discountRate.as("discountRate"),
            course.level.as("level"),
            course.address.as("address"),
            course.detailAddress.as("detailAddress"),
            course.latitude.as("latitude"),
            course.longitude.as("longitude"),
            course.time.as("time"),
            course.maxCapacity.as("maxCapacity"),
            course.minDaysPriorToReservation.as("minDaysPriorToReservation"),
            course.views.as("views"),
            hostInfo.id.as("hostId"),
            hostInfo.user.userNickname.as("nickname"),
            hostInfo.bio.as("bio"),
            hostInfo.instar.as("instar"),
            hostInfo.kakao.as("kakao"),
            hostInfo.blog.as("blog"),
            hostInfo.youtube.as("youtube")
        ))
        .from(course)
        .leftJoin(course.category, category)
        .leftJoin(hostInfo).on(course.user.id.eq(hostInfo.user.id))
        .where(course.id.eq(courseId))
        .fetchOne();
  }

  public int save(Course course) {
    return jpaCourseRepository.save(course).getId();
  }
}
