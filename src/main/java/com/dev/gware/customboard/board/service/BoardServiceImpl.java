package com.dev.gware.customboard.board.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;
import com.dev.gware.customboard.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Board addBoard(AddBoardReq req, long userId) {
        Board board = new Board();
        BeanUtils.copyProperties(req, board);
        board.setUserId(userId);
        return boardRepository.save(board);
    }

    @Override
    public List<GetBoardListRes> getBoardList() {
        List<Board> boardList = boardRepository.findAll();
        List<GetBoardListRes> resList = new ArrayList<>();
        for (Board board : boardList) {
            GetBoardListRes res = new GetBoardListRes();
            BeanUtils.copyProperties(board, res);
            resList.add(res);
        }

        return resList;
    }

    @Override
    public void deleteBoard(long boardId, long userId) {
        Board board = boardRepository.findByBoardId(boardId);
        if (board.getUserId() == userId) {
            boardRepository.deleteByBoardId(boardId);
        }
    }

}
