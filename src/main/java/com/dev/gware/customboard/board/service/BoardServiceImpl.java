package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Override
    public void registBoard(HashMap<String, Object> reqParams) {

        String name = (String) reqParams.get("name");

        Board board = new Board(name);
        boardRepository.save(board);
    }

    @Override
    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    @Override
    public void deleteBoard(long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }

}
