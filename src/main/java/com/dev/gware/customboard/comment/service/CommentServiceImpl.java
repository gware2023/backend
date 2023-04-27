package com.dev.gware.customboard.comment.service;

import com.dev.gware.customboard.comment.domain.Comment;
import com.dev.gware.customboard.comment.dto.request.AddCommentReq;
import com.dev.gware.customboard.comment.dto.request.GetCommentListReq;
import com.dev.gware.customboard.comment.dto.response.GetCommentListRes;
import com.dev.gware.customboard.comment.repository.CommentRepository;
import com.dev.gware.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserMapper userMapper;

    @Override
    public void addComment(AddCommentReq req, long userId) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(req, comment);
        comment.setUserId(userId);
        String userName = userMapper.findByKey(comment.getUserId()).getKorNm();
        comment.setUserName(userName);

        commentRepository.save(comment);
    }

    @Override
    public List<GetCommentListRes> getCommentList(GetCommentListReq req) {
        List<Comment> commentList = commentRepository.findByPostId(req.getPostId());
        List<GetCommentListRes> resList = new ArrayList<>();
        for (Comment comment : commentList) {
            GetCommentListRes res = new GetCommentListRes();
            BeanUtils.copyProperties(comment, res);
            resList.add(res);
        }
        return resList;
    }

    @Override
    public void deleteComment(long commentId, long userId) {
        Comment comment = commentRepository.findByCommentId(commentId);
        if (comment != null && comment.getUserId() == userId) {
            commentRepository.deleteByCommentId(commentId);
        }
    }
}
