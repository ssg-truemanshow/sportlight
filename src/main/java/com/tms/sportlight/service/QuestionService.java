package com.tms.sportlight.service;

import com.tms.sportlight.dto.CourseQuestionDTO;
import com.tms.sportlight.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;

  @Transactional(readOnly = true)
  public List<CourseQuestionDTO> getQuestions(Integer courseId) {
    return questionRepository.findByCourseId(courseId);
  }

}
