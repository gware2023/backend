package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.RegistBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;

import java.util.HashMap;
import java.util.List;

public interface BoardService {
    void registBoard(RegistBoardReq req);

    GetBoardListRes getBoardList();

    void deleteBoard(long boardId);

}
