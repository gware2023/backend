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
    public void addBoard(AddBoardReq req) {
        Board board = new Board();
        BeanUtils.copyProperties(req, board);
        boardRepository.save(board);
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
    public void deleteBoard(long boardId) {
        boardRepository.deleteByBoardId(boardId);
    }

}
