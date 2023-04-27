package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

    SurveyQuestion findByQuestionId(long questionId);

    List<SurveyQuestion> findBySurveyId(long surveyId);
}
