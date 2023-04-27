package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.PostRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PostRecommendationRepository extends JpaRepository<PostRecommendation, Long> {
    PostRecommendation findByPostIdAndUserId(long postId, long userId);

    @Transactional
    void deleteByPostIdAndUserId(long postId, long userId);
}
