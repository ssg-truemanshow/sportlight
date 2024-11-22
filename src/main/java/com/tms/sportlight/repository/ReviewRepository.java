package com.tms.sportlight.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.QCourse;
import com.tms.sportlight.domain.QReview;
import com.tms.sportlight.domain.QUser;
import com.tms.sportlight.dto.CourseReviewDTO;
import com.tms.sportlight.dto.ReviewDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    private final JpaCourseRepository jpaCourseRepository;
    private final JPAQueryFactory queryFactory;

    public List<CourseReviewDTO> findByCourseId(Integer courseId) {
        QReview review = QReview.review;
        QUser user = QUser.user;

        return queryFactory.select(Projections.fields(CourseReviewDTO.class,
                user.id.as("userId"),
                user.userNickname.as("nickname"),
                review.content,
                review.regDate,
                review.rating
            ))
            .from(review)
            .leftJoin(user).on(review.user.id.eq(user.id))
            .where(review.course.id.eq(courseId))
            .fetch();
    }

}