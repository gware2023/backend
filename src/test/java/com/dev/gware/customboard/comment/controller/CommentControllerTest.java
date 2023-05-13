package com.dev.gware.customboard.comment.controller;

import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.service.AuthService;
import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.service.BoardService;
import com.dev.gware.customboard.comment.domain.Comment;
import com.dev.gware.customboard.comment.dto.request.AddCommentReq;
import com.dev.gware.customboard.comment.dto.request.GetCommentListReq;
import com.dev.gware.customboard.comment.service.CommentService;
import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.dto.request.AddPostReq;
import com.dev.gware.customboard.post.service.PostService;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.dto.request.CreateUserReq;
import com.dev.gware.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class CommentControllerTest {

    private final String JWT_HEADER_PREFIX = "Bearer ";
    private String JWT_ACCESS_TOKEN;

    private long boardId;
    private long postId;
    private long commentId;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    BoardService boardService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    @BeforeEach
    void init() throws IOException {
        Users user = userService.addUser(new CreateUserReq("loginId", "asd123", "노호준", "email@email.com"));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword("asd123");
        LoginResponse loginResponse = authService.login(loginRequest);
        JWT_ACCESS_TOKEN = loginResponse.getAccessToken();

        Board board = boardService.addBoard(new AddBoardReq("게시판 이름"), user.getId());
        boardId = board.getBoardId();

        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, user.getId());
        postId = post.getPostId();

        Comment comment = commentService.addComment(new AddCommentReq("내용", postId), user.getId());
        commentId = comment.getCommentId();
    }

    @Test
    void addComment() throws Exception {
        AddCommentReq req = new AddCommentReq();
        req.setPostId(postId);
        req.setContent("댓글");

        mockMvc.perform(post("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("addComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getCommentList() throws Exception {
        GetCommentListReq req = new GetCommentListReq();
        req.setPostId(postId);

        mockMvc.perform(get("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("getCommentList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deleteComment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}