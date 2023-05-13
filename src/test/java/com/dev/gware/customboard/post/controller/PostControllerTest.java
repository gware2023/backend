package com.dev.gware.customboard.post.controller;

import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.service.AuthService;
import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.service.BoardService;
import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.domain.SurveyQuestion;
import com.dev.gware.customboard.post.dto.request.*;
import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionReq;
import com.dev.gware.customboard.post.repository.SurveyQuestionRepository;
import com.dev.gware.customboard.post.repository.SurveyRepository;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class PostControllerTest {

    private final String JWT_HEADER_PREFIX = "Bearer ";
    private String JWT_ACCESS_TOKEN;

    private long boardId;
    private long postId;
    private long surveyId;
    private List<Long> questionIdList;

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
    SurveyRepository surveyRepository;
    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

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

        List<SurveyQuestionReq> surveyQuestionReqList = new ArrayList<>();
        surveyQuestionReqList.add(new SurveyQuestionReq("항목"));
        surveyQuestionReqList.add(new SurveyQuestionReq("항목"));
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, new SurveyReq("투표 제목", surveyQuestionReqList), user.getId());
        postId = post.getPostId();
        surveyId = surveyRepository.findByPostId(postId).getSurveyId();
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findBySurveyId(surveyId);
        questionIdList = new ArrayList<>();
        for (SurveyQuestion surveyQuestion : surveyQuestionList) {
            questionIdList.add(surveyQuestion.getQuestionId());
        }
    }

    @Test
    void addPost() throws Exception {
        AddPostReq req = new AddPostReq();
        req.setBoardId(boardId);
        req.setTitle("제목");
        req.setContent("내용");
        MockMultipartFile reqFile = new MockMultipartFile("req", null, "application/json", objectMapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8));

        SurveyReq surveyReq = new SurveyReq();
        surveyReq.setTitle("투표 제목");
        surveyReq.setSurveyQuestionReqList(new ArrayList<>());
        surveyReq.getSurveyQuestionReqList().add(new SurveyQuestionReq());
        surveyReq.getSurveyQuestionReqList().add(new SurveyQuestionReq());
        surveyReq.getSurveyQuestionReqList().get(0).setQuestion("항목 이름1");
        surveyReq.getSurveyQuestionReqList().get(1).setQuestion("항목 이름2");
        MockMultipartFile surveyReqFile = new MockMultipartFile("surveyReq", null, "application/json", objectMapper.writeValueAsString(surveyReq).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile attachedFiles = new MockMultipartFile("attachedFiles", "file.txt", null, "{attachedFile}".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile imgFiles = new MockMultipartFile("imgFiles", "img.png", null, "{imgFile}".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.POST, "/api/posts")
                        .file(reqFile)
                        .file(attachedFiles)
                        .file(imgFiles)
                        .file(surveyReqFile)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("addPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPost() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getAttachedFileList() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/attached-files", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getAttachedFileList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getImgFileList() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/img-files", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getImgFileList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void downloadAttachedFile() throws Exception {
        mockMvc.perform(get("/api/posts/attached-files/{storeFileName}", "1df3dff0-1cbc-4bf9-8367-44ed37fb032b.txt")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isInternalServerError())
                .andDo(document("downloadAttachedFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void downloadImgFile() throws Exception {
        mockMvc.perform(get("/api/posts/img-files/{storeFileName}", "a70f9a71-f207-4b7d-ad70-8ac0b8996cf9.jpg")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("downloadImgFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getSurvey() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/survey", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getSurvey",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPostList() throws Exception {
        GetPostListReq req = new GetPostListReq();
        req.setBoardId(boardId);
        req.setPageNum(1);

        mockMvc.perform(get("/api/posts")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("getPostList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void updatePost() throws Exception {
        AddPostReq req = new AddPostReq();
        req.setBoardId(boardId);
        req.setTitle("바꿀 제목");
        req.setContent("바꿀 내용");
        MockMultipartFile reqFile = new MockMultipartFile("req", null, "application/json", objectMapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8));

        SurveyReq surveyReq = new SurveyReq();
        surveyReq.setTitle("바꿀 투표 제목");
        surveyReq.setSurveyQuestionReqList(new ArrayList<>());
        surveyReq.getSurveyQuestionReqList().add(new SurveyQuestionReq());
        surveyReq.getSurveyQuestionReqList().add(new SurveyQuestionReq());
        surveyReq.getSurveyQuestionReqList().get(0).setQuestion("바꿀 항목 이름1");
        surveyReq.getSurveyQuestionReqList().get(1).setQuestion("바꿀 항목 이름2");
        MockMultipartFile surveyReqFile = new MockMultipartFile("surveyReq", null, "application/json", objectMapper.writeValueAsString(surveyReq).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile attachedFiles = new MockMultipartFile("attachedFiles", "changedFile.txt", null, "{changedAttachedFile}".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile imgFiles = new MockMultipartFile("imgFiles", "changedImg.png", null, "{changedImgFile}".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/{postId}", postId)
                        .file(reqFile)
                        .file(attachedFiles)
                        .file(imgFiles)
                        .file(surveyReqFile)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("updatePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deletePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void searchPosts() throws Exception {
        SearchPostsReq req = new SearchPostsReq();
        req.setBoardId(boardId);
        req.setType(0);
        req.setKeyword("제");
        req.setPage(1);

        mockMvc.perform(get("/api/posts/search")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("searchPosts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void recommendPost() throws Exception {
        mockMvc.perform(patch("/api/posts/{postId}/recommend", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("recommendPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void cancelRecommendationPost() throws Exception {
        mockMvc.perform(patch("/api/posts/{postId}/cancel-recommendation", postId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("cancelRecommendationPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void vote() throws Exception {
        VoteReq req = new VoteReq();
        req.setSurveyId(surveyId);
        req.setVotedQuestionIdList(new ArrayList<>());
        req.getVotedQuestionIdList().add(questionIdList.get(0));
        req.getVotedQuestionIdList().add(questionIdList.get(1));

        mockMvc.perform(patch("/api/posts/vote")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("vote",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}