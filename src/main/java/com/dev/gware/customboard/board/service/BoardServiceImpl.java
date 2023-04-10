package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;
import com.dev.gware.customboard.board.repository.BoardRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Override
    public void addBoard(AddBoardReq req) {
        Board board = new Board();
        BeanUtils.copyProperties(req, board);
        boardRepository.save(board);
    }

    @Override
    public GetBoardListRes getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        GetBoardListRes res = new GetBoardListRes();
        res.setBoardList(boardList);

        return res;
    }

    @Override
    public void deleteBoard(long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }

}
