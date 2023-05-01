package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;

import java.util.List;

public interface BoardService {
    void addBoard(AddBoardReq req, long userId);

    List<GetBoardListRes> getBoardList();

    void deleteBoard(long boardId, long userId);

}
