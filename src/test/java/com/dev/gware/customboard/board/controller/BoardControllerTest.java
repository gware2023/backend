package com.dev.gware.customboard.board.controller;

import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.service.AuthService;
import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.service.BoardService;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class BoardControllerTest {

    private final String JWT_HEADER_PREFIX = "Bearer ";
    private String JWT_ACCESS_TOKEN;

    private long boardId;

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

    @BeforeEach
    void init() {
        Users user = userService.addUser(new CreateUserReq("loginId", "asd123", "노호준", "email@email.com"));
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginId("loginId");
        loginRequest.setPassword("asd123");
        LoginResponse loginResponse = authService.login(loginRequest);
        JWT_ACCESS_TOKEN = loginResponse.getAccessToken();

        Board board = boardService.addBoard(new AddBoardReq("게시판 이름"), user.getId());
        boardId = board.getBoardId();
    }


    @Test
    void addBoard() throws Exception {
        AddBoardReq req = new AddBoardReq();
        req.setName("게시판 이름");

        mockMvc.perform(post("/api/boards")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(document("addBoard",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getBoardList() throws Exception {
        mockMvc.perform(get("/api/boards")
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("getBoardList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deleteBoard() throws Exception {
        mockMvc.perform(delete("/api/boards/{boardId}", boardId)
                        .header(HttpHeaders.AUTHORIZATION, JWT_HEADER_PREFIX + JWT_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(document("deleteBoard",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}