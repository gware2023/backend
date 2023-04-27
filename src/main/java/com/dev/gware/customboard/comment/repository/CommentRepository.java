package com.dev.gware.customboard.comment.repository;

import com.dev.gware.customboard.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByCommentId(long commentId);

    List<Comment> findByPostId(long postId);

    @Transactional
    void deleteByCommentId(long commentId);
}
