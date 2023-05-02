package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;
import com.dev.gware.customboard.board.repository.BoardRepository;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.dto.request.CreateUserReq;
import com.dev.gware.user.repository.UserRepository;
import com.dev.gware.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardServiceImplTest {

    private long userId = 0L;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    void clearRepository() {
        userRepository.deleteAll();
        boardRepository.deleteAll();

        Users user = userService.addUser(new CreateUserReq("loginId", "asd123", "노호준", "email@email.com"));
        userId = user.getId();
    }

    @Test
    void addBoard() {
        boardService.addBoard(new AddBoardReq("게시판 이름"), userId);

        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList.size()).isEqualTo(1);
        assertThat(boardList.get(0).getName()).isEqualTo("게시판 이름");
    }

    @Test
    void getBoardList() {
        boardService.addBoard(new AddBoardReq("게시판 이름"), userId);
        boardService.addBoard(new AddBoardReq("게시판 이름"), userId);

        List<GetBoardListRes> boardList = boardService.getBoardList();

        assertThat(boardList.size()).isEqualTo(2);
        for (GetBoardListRes getBoardListRes : boardList) {
            assertThat(getBoardListRes.getName()).isEqualTo("게시판 이름");
        }
    }

    @Test
    void deleteBoard() {
        boardService.addBoard(new AddBoardReq("게시판 이름"), userId);

        List<Board> boardList = boardRepository.findAll();
        boardService.deleteBoard(boardList.get(0).getBoardId(), userId);

        assertThat(boardRepository.findAll().size()).isEqualTo(0);
    }
}