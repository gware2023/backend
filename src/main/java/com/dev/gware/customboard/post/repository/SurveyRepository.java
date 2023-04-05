package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey findByPostId(long postId);

    @Transactional
    void deleteByPostId(long postId);
}
