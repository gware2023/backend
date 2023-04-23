package com.dev.gware.customboard.board.controller;

import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.dto.response.GetBoardListRes;
import com.dev.gware.customboard.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Validated
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Object> addBoard(@RequestBody @Valid AddBoardReq req) {

        boardService.addBoard(req);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getBoardList() {

        List<GetBoardListRes> resList = boardService.getBoardList();

        return ResponseEntity.ok().body(resList);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable @Min(1L) long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }
}
