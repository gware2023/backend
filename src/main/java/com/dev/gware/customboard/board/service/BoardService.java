package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;

public interface BoardService {
    void addBoard(AddBoardReq req);

    GetBoardListRes getBoardList();

    void deleteBoard(long boardId);

}
