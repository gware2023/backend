package com.dev.gware.customboard.post.controller;

import com.dev.gware.customboard.post.dto.request.*;
import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class PostControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addPost() throws Exception {
        AddPostReq req = new AddPostReq();
        req.setBoardId(1L);
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
                        .file(surveyReqFile))
                .andExpect(status().isOk())
                .andDo(document("addPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPost() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}", 1))
                .andExpect(status().isOk())
                .andDo(document("getPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getAttachedFileList() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/attached-files", 1))
                .andExpect(status().isOk())
                .andDo(document("getAttachedFileList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getImgFileList() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/img-files", 1))
                .andExpect(status().isOk())
                .andDo(document("getImgFileList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void downloadAttachedFile() throws Exception {
        mockMvc.perform(get("/api/posts/attached-files/{storeFileName}", "1df3dff0-1cbc-4bf9-8367-44ed37fb032b.txt"))
                .andExpect(status().isOk())
                .andDo(document("downloadAttachedFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void downloadImgFile() throws Exception {
        mockMvc.perform(get("/api/posts/img-files/{storeFileName}", "a70f9a71-f207-4b7d-ad70-8ac0b8996cf9.jpg"))
                .andExpect(status().isOk())
                .andDo(document("downloadImgFile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getSurvey() throws Exception {
        mockMvc.perform(get("/api/posts/{postId}/survey", 1))
                .andExpect(status().isOk())
                .andDo(document("getSurvey",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPostList() throws Exception {
        GetPostListReq req = new GetPostListReq();
        req.setBoardId(1L);
        req.setPageNum(1);

        mockMvc.perform(get("/api/posts")
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
        req.setBoardId(1L);
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

        mockMvc.perform(multipart(HttpMethod.PUT, "/api/posts/{postId}", 1)
                        .file(reqFile)
                        .file(attachedFiles)
                        .file(imgFiles)
                        .file(surveyReqFile))
                .andExpect(status().isOk())
                .andDo(document("updatePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deletePost() throws Exception {
        mockMvc.perform(delete("/api/posts/{postId}", 1))
                .andExpect(status().isOk())
                .andDo(document("deletePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void searchPosts() throws Exception {
        SearchPostsReq req = new SearchPostsReq();
        req.setBoardId(1L);
        req.setType(0);
        req.setKeyword("제");
        req.setPage(1);

        mockMvc.perform(get("/api/posts/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("searchPosts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void recommendPost() throws Exception {
        mockMvc.perform(patch("/api/posts/{postId}/recommend", 1))
                .andExpect(status().isOk())
                .andDo(document("recommendPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void cancelRecommendationPost() throws Exception {
        mockMvc.perform(patch("/api/posts/{postId}/cancel-recommendation", 1))
                .andExpect(status().isOk())
                .andDo(document("cancelRecommendationPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void vote() throws Exception {
        VoteReq req = new VoteReq();
        req.setSurveyId(1L);
        req.setVotedQuestionIdList(new ArrayList<>());
        req.getVotedQuestionIdList().add(1L);
        req.getVotedQuestionIdList().add(2L);

        mockMvc.perform(patch("/api/posts/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("vote",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}