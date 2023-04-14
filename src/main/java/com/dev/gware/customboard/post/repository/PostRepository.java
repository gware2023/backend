package com.dev.gware.customboard.post.repository;

import com.dev.gware.customboard.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByPostId(long postId);

    Page<Post> findByBoardId(long boardId, Pageable pageable);

    @Transactional
    void deleteByPostId(long postId);

    Page<Post> findByBoardIdAndTitleContaining(long boardId, String title, Pageable pageable);

    Page<Post> findByBoardIdAndContentContaining(long boardId, String content, Pageable pageable);

    Page<Post> findByBoardIdAndUserId(long boardId, long userId, Pageable pageable);
}
