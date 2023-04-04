package com.dev.gware.customboard.post.controller;

import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.RegistPostServey;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.response.GetAttachedFileListRes;
import com.dev.gware.customboard.post.dto.response.GetImgFileListRes;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;
import com.dev.gware.customboard.post.repository.AttachedFileRepository;
import com.dev.gware.customboard.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    AttachedFileRepository attachedFileRepository;

    @PostMapping
    public ResponseEntity<Object> registPost(@RequestPart RegistPostReq req,
                                             @RequestPart @Nullable List<MultipartFile> attachedFiles,
                                             @RequestPart @Nullable List<MultipartFile> imgFiles,
                                             @RequestPart @Nullable RegistPostServey surveyReq) throws IOException {

        postService.registPost(req, attachedFiles, imgFiles, surveyReq);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Object> getPost(@PathVariable long postId) {

        GetPostRes res = postService.getPost(postId);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{postId}/attached-files")
    public ResponseEntity<Object> getAttachedFileList(@PathVariable long postId) {

        List<GetAttachedFileListRes> resList = postService.getAttachedFileList(postId);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/{postId}/img-files")
    public ResponseEntity<Object> getImgFileList(@PathVariable long postId) {

        List<GetImgFileListRes> resList = postService.getImgFileList(postId);

        return ResponseEntity.ok().body(resList);
    }

    @GetMapping("/attached-files/{storeFileName}")
    public ResponseEntity<Object> downloadAttachedFile(@PathVariable String storeFileName) throws MalformedURLException, UnsupportedEncodingException {

        UrlResource res = postService.downloadAttachedFile(storeFileName);

        String downloadFileName = attachedFileRepository.findByStoreFileName(storeFileName).getUploadFileName();
        downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
                .body(res);
    }

    @GetMapping("/img-files/{storeFileName}")
    public ResponseEntity<Object> downloadImgFile(@PathVariable String storeFileName) throws MalformedURLException {

        UrlResource res = postService.downloadImgFile(storeFileName);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping
    public ResponseEntity<Object> getPostList(@RequestBody GetPostListReq req) {

        List<GetPostListRes> resList = postService.getPostList(req);

        return ResponseEntity.ok().body(resList);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(@PathVariable long postId, @RequestBody UpdatePostReq req) {

        postService.updatePost(postId, req);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable long postId) {

        postService.deletePost(postId);

        return ResponseEntity.ok().build();
    }
}
