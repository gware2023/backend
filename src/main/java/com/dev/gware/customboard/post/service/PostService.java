package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.RegistPostServey;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    void registPost(RegistPostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, RegistPostServey surveyReq) throws IOException;

    GetPostRes getPost(long postId);

    GetPostListRes getPostList(GetPostListReq req);

    void updatePost(long postId, UpdatePostReq req);

    void deletePost(long postId);
}
