package com.dev.gware.customboard.post.controller;

import com.dev.gware.auth.domain.AuthUser;
import com.dev.gware.customboard.post.dto.request.*;
import com.dev.gware.customboard.post.dto.response.*;
import com.dev.gware.customboard.post.repository.AttachedFileRepository;
import com.dev.gware.customboard.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;
    private final AttachedFileRepository attachedFileRepository;

    @PostMapping
    public ResponseEntity<Object> addPost(@RequestPart @Valid AddPostReq req,
                                          @RequestPart @Nullable List<MultipartFile> attachedFiles,
                                          @RequestPart @Nullable List<MultipartFile> imgFiles,
                                          @RequestPart @Nullable @Valid SurveyReq surveyReq,
                                          @AuthenticationPrincipal AuthUser authUser) throws IOException {

        postService.addPost(req, attachedFiles, imgFiles, surveyReq, authUser.getUsrKey());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable @Min(1L) long postId) {

        GetPostRes res = postService.getPost(postId);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{postId}/attached-files")
    public ResponseEntity<Object> getAttachedFileList(@PathVariable @Min(1L) long postId) {

        List<GetAttachedFileListRes> resList = postService.getAttachedFileList(postId);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/{postId}/img-files")
    public ResponseEntity<Object> getImgFileList(@PathVariable @Min(1L) long postId) {

        List<GetImgFileListRes> resList = postService.getImgFileList(postId);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/attached-files/{storeFileName}")
    public ResponseEntity<Object> downloadAttachedFile(@PathVariable @NotBlank String storeFileName) throws MalformedURLException, UnsupportedEncodingException {

        UrlResource res = postService.downloadAttachedFile(storeFileName);

        String downloadFileName = attachedFileRepository.findByStoreFileName(storeFileName).getUploadFileName();
        downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
                .body(res);
    }

    @GetMapping("/img-files/{storeFileName}")
    public ResponseEntity<Object> downloadImgFile(@PathVariable @NotBlank String storeFileName) throws MalformedURLException {

        UrlResource res = postService.downloadImgFile(storeFileName);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{postId}/survey")
    public ResponseEntity<Object> getSurvey(@PathVariable @Min(1L) long postId,
                                            @AuthenticationPrincipal AuthUser authUser) {

        long userId = authUser.getUsrKey();

        GetSurveyRes res = postService.getSurvey(postId, userId);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    public ResponseEntity<Object> getPostList(@RequestBody @Valid GetPostListReq req) {

        List<GetPostListRes> resList = postService.getPostList(req);

        return ResponseEntity.ok().body(resList);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(@PathVariable @Min(1L) long postId,
                                             @RequestPart @Valid UpdatePostReq req,
                                             @RequestPart @Nullable List<MultipartFile> attachedFiles,
                                             @RequestPart @Nullable List<MultipartFile> imgFiles,
                                             @RequestPart @Nullable @Valid SurveyReq surveyReq) throws IOException {

        postService.updatePost(postId, req, attachedFiles, imgFiles, surveyReq);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable @Min(1L) long postId) {

        postService.deletePost(postId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchPosts(@RequestBody @Valid SearchPostsReq req) {

        List<SearchPostsRes> resList = postService.searchPosts(req);

        return ResponseEntity.ok().body(resList);
    }

    @PatchMapping("/{postId}/recommend")
    public ResponseEntity<Object> recommendPost(@PathVariable @Min(1L) long postId,
                                                @AuthenticationPrincipal AuthUser authUser) {

        postService.recommendPost(postId, authUser.getUsrKey());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{postId}/cancel-recommendation")
    public ResponseEntity<Object> cancelRecommendationPost(@PathVariable @Min(1L) long postId,
                                                           @AuthenticationPrincipal AuthUser authUser) {

        postService.cancelPostRecommendation(postId, authUser.getUsrKey());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{surveyId}/vote")
    public ResponseEntity<Object> vote(@PathVariable @Min(1L) long surveyId,
                                       @RequestBody VoteReq req,
                                       @AuthenticationPrincipal AuthUser authUser) {

        postService.vote(surveyId, req, authUser.getUsrKey());

        return ResponseEntity.ok().build();
    }
}

