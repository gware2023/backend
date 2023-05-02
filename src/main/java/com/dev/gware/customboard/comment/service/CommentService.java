package com.dev.gware.customboard.comment.service;

import com.dev.gware.customboard.comment.domain.Comment;
import com.dev.gware.customboard.comment.dto.request.AddCommentReq;
import com.dev.gware.customboard.comment.dto.request.GetCommentListReq;
import com.dev.gware.customboard.comment.dto.response.GetCommentListRes;

import java.util.List;

public interface CommentService {

    Comment addComment(AddCommentReq req, long userId);

    List<GetCommentListRes> getCommentList(GetCommentListReq req);

    void deleteComment(long commentId, long userId);
}
