package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.domain.Board;

import java.util.HashMap;
import java.util.List;

public interface BoardService {
    void registBoard(HashMap<String, Object> reqParams);

    List<Board> getBoardList();

    void deleteBoard(long boardId);

}
