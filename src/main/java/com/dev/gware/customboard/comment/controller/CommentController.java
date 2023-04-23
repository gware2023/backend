package com.dev.gware.customboard.comment.controller;

import com.dev.gware.auth.domain.AuthUser;
import com.dev.gware.customboard.comment.dto.request.AddCommentReq;
import com.dev.gware.customboard.comment.dto.request.GetCommentListReq;
import com.dev.gware.customboard.comment.dto.response.GetCommentListRes;
import com.dev.gware.customboard.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Object> addComment(@RequestBody @Valid AddCommentReq req,
                                             @AuthenticationPrincipal AuthUser authUser) {

        commentService.addComment(req, authUser.getUsrKey());

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Object> getCommentList(@RequestBody @Valid GetCommentListReq req) {

        List<GetCommentListRes> resList = commentService.getCommentList(req);

        return ResponseEntity.ok().body(resList);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable @Min(1L) long commentId,
                                                @AuthenticationPrincipal AuthUser authUser) {

        commentService.deleteComment(commentId, authUser.getUsrKey());

        return ResponseEntity.ok().build();
    }
}
