package com.tms.sportlight.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.CourseLevel;
import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.QAttendCourse;
import com.tms.sportlight.domain.QCategory;
import com.tms.sportlight.domain.QCourse;
import com.tms.sportlight.domain.QCourseSchedule;
import com.tms.sportlight.domain.QHostInfo;
import com.tms.sportlight.domain.QInterest;
import com.tms.sportlight.domain.QReview;
import com.tms.sportlight.domain.QUploadFile;
import com.tms.sportlight.domain.QUser;
import com.tms.sportlight.domain.QUserInterests;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.SortType;
import com.tms.sportlight.dto.CourseCardDTO;
import com.tms.sportlight.dto.CourseDetailDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        .where(course.status.eq(CourseStatus.APPROVED))
        .groupBy(course.id)
        .orderBy(attendCourse.participantNum.sum().desc())
        .limit(10)
        .fetch();
  }

  public List<CourseCardDTO> findBeginnerCourses() {
    QCourse course = QCourse.course;
    QCourseSchedule schedule = QCourseSchedule.courseSchedule;
    QAttendCourse attendCourse = QAttendCourse.attendCourse;
    QCategory category = QCategory.category;
    QUser user = QUser.user;
    QReview review = QReview.review;

    return queryFactory
        .select(Projections.fields(CourseCardDTO.class,
            course.id.as("id"),
            user.userNickname.as("nickname"),
            course.title.as("title"),
            course.address.as("address"),
            course.tuition.as("tuition"),
            course.discountRate.as("discountRate"),
            course.time.as("time"),
            course.level.as("level"),
            category.name.as("category"),
            MathExpressions.round(review.rating.avg(), 2).as("rating"),
            review.rating.count().as("reviewCount")
        ))
        .from(course)
        .leftJoin(course.category, category)
        .leftJoin(course.user, user)
        .leftJoin(review).on(course.id.eq(review.course.id))
        .where(course.status.eq(CourseStatus.APPROVED))
        .groupBy(course.id)
        .orderBy(review.rating.count().desc(), review.rating.avg().desc())
        .limit(10)
        .fetch();
  }

//  public List<CourseCardDTO> findRecommendCourses() {
//    Long userId = 13L; // 테스트용 하드코딩된 사용자 ID
//
//    QCourse course = QCourse.course;
//    QUserInterests userInterests = QUserInterests.userInterests;
//    QInterest interest = QInterest.interest;
//    QReview review = QReview.review;
//    QUser host = QUser.user;
//    QCategory category = QCategory.category;
//
//    // 메인 쿼리 작성
//    return queryFactory
//        .select(Projections.fields(CourseCardDTO.class,
//            course.id,
//            host.userNickname.as("nickname"),
//            course.title,
//            course.address,
//            course.tuition,
//            course.discountRate,
//            course.time,
//            course.level,
//            category.name.as("category"),
//            MathExpressions.round(review.rating.avg(), 2).as("rating"),
//            review.rating.count().as("reviewCount"),
//            course.title.as("imgUrl")
//        ))
//        .from(course)
//        .leftJoin(userInterests)
//        .on(course.category.id.eq(userInterests.category.id)
//            .and(userInterests.user.id.eq(userId)))
//        .leftJoin(category).on(course.category.id.eq(category.id))
//        .leftJoin(host).on(course.user.id.eq(host.id))
//        .leftJoin(review).on(course.id.eq(review.course.id))
//        .orderBy(
//            userInterests.category.id.desc(), // 선호 카테고리
//            new CaseBuilder()
//                .when(course.user.id.in(
//                    JPAExpressions.select(course.user.id)
//                        .from(course)
//                        .join(interest.course, course)
//                        .where(interest.user.id.eq(userId))
//                        .groupBy(course.user.id)
//                        .orderBy(course.user.id.count().desc())
//                ))
//                .then(1).otherwise(2).asc(), // 강사 인기도
//            new CaseBuilder()
//                .when(course.category.id.in(
//                    JPAExpressions.select(course.category.id)
//                        .from(course)
//                        .join(interest.course, course)
//                        .where(interest.user.id.eq(userId))
//                        .groupBy(course.category.id)
//                        .orderBy(course.category.id.count().desc())
//                ))
//                .then(1).otherwise(2).asc() // 카테고리 인기도
//        )
//        .fetch();
//  }


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
      SortType sortType,
      Pageable pageable
  ) {
    QCourse course = QCourse.course;
    QCategory category = QCategory.category;
    QReview review = QReview.review;
    QCourseSchedule schedule = QCourseSchedule.courseSchedule;
    QAttendCourse attendCourse = QAttendCourse.attendCourse;
    QUser user = QUser.user;
    QUploadFile uploadFile = QUploadFile.uploadFile;

    BooleanBuilder whereClause = new BooleanBuilder();
    BooleanBuilder whereClauseUrl = new BooleanBuilder();

    whereClause.and(course.status.eq(CourseStatus.APPROVED));

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

    whereClauseUrl.and(uploadFile.identifier.eq(course.id));
    whereClauseUrl.and(uploadFile.type.eq(FileType.COURSE_THUMB));
    whereClauseUrl.and(uploadFile.deleted.isFalse());

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
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  public Long searchCoursesCount(
      List<Integer> categories,
      List<CourseLevel> levels,
      Double minPrice,
      Double maxPrice,
      Integer participants,
      LocalDate startDate,
      LocalDate endDate,
      Double latitude,
      Double longitude,
      String searchText
  ) {
    QCourse course = QCourse.course;
    QCategory category = QCategory.category;
    QCourseSchedule schedule = QCourseSchedule.courseSchedule;
    QAttendCourse attendCourse = QAttendCourse.attendCourse;

    BooleanBuilder whereClause = new BooleanBuilder();
    BooleanBuilder whereClauseUrl = new BooleanBuilder();

    whereClause.and(course.status.eq(CourseStatus.APPROVED));

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

    return queryFactory
        .select(course.countDistinct())
        .from(course)
        .leftJoin(schedule).on(course.id.eq(schedule.course.id))
        .leftJoin(attendCourse).on(schedule.id.eq(attendCourse.courseSchedule.id))
        .leftJoin(course.category, category)
        .where(whereClause)
        .fetchOne();
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
    QUploadFile thumbImg = new QUploadFile("thumb");
    QUploadFile hostImg = new QUploadFile("host");

    CourseDetailDTO courseDetailDTO = queryFactory.select(Projections.fields(CourseDetailDTO.class,
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
            hostInfo.facebook.as("facebook"),
            hostInfo.twitter.as("twitter"),
            hostInfo.youtube.as("youtube"),
            thumbImg.path.as("imgUrl"),
            hostImg.path.as("hostProfile")
        ))
        .from(course)
        .leftJoin(course.category, category)
        .leftJoin(hostInfo).on(course.user.id.eq(hostInfo.user.id))
        .leftJoin(thumbImg).on(thumbImg.identifier.eq(courseId)
            .and(thumbImg.type.eq(FileType.COURSE_THUMB))
            .and(thumbImg.deleted.isFalse())
        )
        .leftJoin(hostImg).on(hostImg.identifier.eq(hostInfo.user.id.intValue())
            .and(hostImg.type.eq(FileType.USER_PROFILE_ICON))
        )
        .where(course.id.eq(courseId))
        .fetchOne();
    System.out.println("==================================================================");
    System.out.println(courseDetailDTO);
    System.out.println("==================================================================");
    return courseDetailDTO;
  }

  public int save(Course course) {
    return jpaCourseRepository.save(course).getId();
  }
}
