package com.dev.gware.customboard.board.controller;

import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;
import com.dev.gware.customboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Object> addBoard(@RequestBody AddBoardReq req) {

        boardService.addBoard(req);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getBoardList() {

        GetBoardListRes res = boardService.getBoardList();

        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }
}
