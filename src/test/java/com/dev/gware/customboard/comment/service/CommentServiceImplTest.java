package com.dev.gware.customboard.comment.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.repository.BoardRepository;
import com.dev.gware.customboard.board.service.BoardService;
import com.dev.gware.customboard.comment.domain.Comment;
import com.dev.gware.customboard.comment.dto.request.AddCommentReq;
import com.dev.gware.customboard.comment.dto.request.GetCommentListReq;
import com.dev.gware.customboard.comment.dto.response.GetCommentListRes;
import com.dev.gware.customboard.comment.repository.CommentRepository;
import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.dto.request.AddPostReq;
import com.dev.gware.customboard.post.repository.PostRepository;
import com.dev.gware.customboard.post.service.PostService;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.dto.request.CreateUserReq;
import com.dev.gware.user.repository.UserRepository;
import com.dev.gware.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceImplTest {

    private long userId = 0L;
    private long boardId = 0L;
    private long postId = 0L;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void clearRepository() throws IOException {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();

        Users user = userService.addUser(new CreateUserReq("loginId", "asd123", "노호준", "email@email.com"));
        userId = user.getId();
        Board board = boardService.addBoard(new AddBoardReq("게시판 이름"), userId);
        boardId = board.getBoardId();
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);
        postId = post.getPostId();
    }

    @Test
    void addComment() {
        Comment comment = commentService.addComment(new AddCommentReq("댓글 내용", postId), userId);

        assertThat(commentRepository.findByCommentId(comment.getCommentId()).getContent()).isEqualTo("댓글 내용");
    }

    @Test
    void getCommentList() {
        commentService.addComment(new AddCommentReq("댓글 내용", postId), userId);
        commentService.addComment(new AddCommentReq("댓글 내용", postId), userId);
        commentService.addComment(new AddCommentReq("댓글 내용", postId), userId);

        List<GetCommentListRes> commentList = commentService.getCommentList(new GetCommentListReq(postId));

        assertThat(commentList.size()).isEqualTo(3);
        for (GetCommentListRes res : commentList) {
            assertThat(res.getContent()).isEqualTo("댓글 내용");
        }
    }

    @Test
    void deleteComment() {
        Comment comment = commentService.addComment(new AddCommentReq("댓글 내용", postId), userId);

        commentService.deleteComment(comment.getCommentId(), userId);

        assertThat(commentRepository.findByCommentId(comment.getCommentId())).isNull();
    }
}