package com.dev.gware.customboard.comment.service;

import com.dev.gware.customboard.comment.domain.Comment;
import com.dev.gware.customboard.comment.dto.request.AddCommentReq;
import com.dev.gware.customboard.comment.dto.request.GetCommentListReq;
import com.dev.gware.customboard.comment.dto.response.GetCommentListRes;
import com.dev.gware.customboard.comment.repository.CommentRepository;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.exception.UserNotFoundException;
import com.dev.gware.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Comment addComment(AddCommentReq req, long userId) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(req, comment);
        comment.setUserId(userId);
        Optional<Users> findUserOptional = userRepository.findById(comment.getUserId());
        if (findUserOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        comment.setUserName(findUserOptional.get().getName());

        return commentRepository.save(comment);
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
