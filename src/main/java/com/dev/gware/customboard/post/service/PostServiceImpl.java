package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.dto.PostInfo;
import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;
import com.dev.gware.customboard.post.repository.PostRepository;
import com.dev.gware.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public void registPost(RegistPostReq req) {
        Post post = new Post();
        BeanUtils.copyProperties(req, post);

        postRepository.save(post);
    }

    @Override
    public GetPostRes getPost(long postId) {
        Post post = postRepository.findByPostId(postId);

        GetPostRes res = new GetPostRes();
        BeanUtils.copyProperties(post, res);
        res.setUserName(userMapper.findByKey(post.getUserId()).getKorNm());

        return res;
    }

    @Override
    public GetPostListRes getPostList(GetPostListReq req) {
        long boardId = req.getBoardId();
        int pageNum = req.getPageNum();
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10, Sort.Direction.DESC, "postId");
        Page<Post> postPage = postRepository.findByBoardId(boardId, pageRequest);

        List<PostInfo> postInfoList = new ArrayList<>();
        for (Post post : postPage) {
            PostInfo postInfo = new PostInfo();
            BeanUtils.copyProperties(post, postInfo);
            postInfo.setUserName(userMapper.findByKey(post.getUserId()).getKorNm());
            postInfoList.add(postInfo);
        }

        GetPostListRes res = new GetPostListRes();
        res.setPostInfoList(postInfoList);

        return res;
    }

    @Override
    public void updatePost(long postId, UpdatePostReq req) {
        Post post = postRepository.findByPostId(postId);
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());

        postRepository.save(post);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteByPostId(postId);
    }
}
