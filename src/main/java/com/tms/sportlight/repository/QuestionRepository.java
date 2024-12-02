package com.tms.sportlight.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tms.sportlight.domain.QAnswer;
import com.tms.sportlight.domain.QQuestion;
import com.tms.sportlight.domain.QUser;
import com.tms.sportlight.dto.CourseQuestionDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionRepository {

  private final JPAQueryFactory queryFactory;

  public List<CourseQuestionDTO> findByCourseId(Integer courseId) {
    QQuestion question = QQuestion.question;
    QAnswer answer = QAnswer.answer;
    QUser user = QUser.user;

    return queryFactory.select(Projections.fields(CourseQuestionDTO.class,
        question.id.as("qId"),
        question.user.id.as("qUserId"),
        question.user.userNickname.as("qUserNickname"),
        question.content.as("qContent"),
        question.regDate.as("qRegDate"),
        answer.id.as("aId"),
        answer.content.as("aContent"),
        answer.regDate.as("aRegDate")
        ))
        .from(question)
        .leftJoin(user).on(question.user.id.eq(user.id))
        .leftJoin(answer).on(question.id.eq(answer.question.id))
        .where(question.course.id.eq(courseId))
        .orderBy(question.regDate.desc())
        .fetch();
  }
}
