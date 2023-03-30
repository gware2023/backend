package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;

public interface PostService {
    void registPost(RegistPostReq req);

    GetPostRes getPost(long postId);

    GetPostListRes getPostList(GetPostListReq req);

    void updatePost(long postId, UpdatePostReq req);

    void deletePost(long postId);
}
