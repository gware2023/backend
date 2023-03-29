package com.dev.gware.customboard.board.controller;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping
    public ResponseEntity<Object> registBoard(@RequestBody HashMap<String, Object> reqParams) {

        boardService.registBoard(reqParams);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getBoardList() {

        List<Board> boardList = boardService.getBoardList();

        return ResponseEntity.ok().body(boardList);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }
}
