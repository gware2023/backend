package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.dto.request.*;
import com.dev.gware.customboard.post.dto.response.*;
import com.dev.gware.customboard.post.exception.QuestionNotIncludedInSurveyException;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface PostService {
    Post addPost(AddPostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, SurveyReq surveyReq, long userId) throws IOException;

    GetPostRes getPost(long postId);

    List<GetAttachedFileListRes> getAttachedFileList(long postId);

    List<GetImgFileListRes> getImgFileList(long postId);

    UrlResource downloadAttachedFile(String storeFileName) throws MalformedURLException;

    UrlResource downloadImgFile(String storeFileName) throws MalformedURLException;

    GetSurveyRes getSurvey(long postId, long userId);

    List<GetPostListRes> getPostList(GetPostListReq req);

    void updatePost(long postId, UpdatePostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, SurveyReq surveyReq, long userId) throws IOException;

    void deletePost(long postId, long userId);

    List<SearchPostsRes> searchPosts(SearchPostsReq req);

    void recommendPost(long postId, long userId);

    void cancelPostRecommendation(long postId, long userId);

    void vote(VoteReq req, long userId) throws QuestionNotIncludedInSurveyException;
}
