package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.dto.request.*;
import com.dev.gware.customboard.post.dto.response.*;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface PostService {
    void addPost(AddPostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, SurveyReq surveyReq) throws IOException;

    GetPostRes getPost(long postId);

    List<GetAttachedFileListRes> getAttachedFileList(long postId);

    List<GetImgFileListRes> getImgFileList(long postId);

    UrlResource downloadAttachedFile(String storeFileName) throws MalformedURLException;

    UrlResource downloadImgFile(String storeFileName) throws MalformedURLException;

    GetSurveyRes getSurvey(long postId, long userId);

    List<GetPostListRes> getPostList(GetPostListReq req);

    void updatePost(long postId, UpdatePostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, SurveyReq surveyReq) throws IOException;

    void deletePost(long postId);

    List<SearchPostsRes> searchPosts(SearchPostsReq req);

    void recommendPost(long postId, Long usrKey);

    void cancelPostRecommendation(long postId, Long usrKey);
}
