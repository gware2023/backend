package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.SurveyVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyVoteRepository extends JpaRepository<SurveyVote, Long> {
    List<SurveyVote> findBySurveyIdAndAndUserId(long surveyId, long userId);
}
