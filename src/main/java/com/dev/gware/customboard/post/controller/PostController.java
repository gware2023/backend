package com.dev.gware.customboard.post.controller;

import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.RegistPostServey;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;
import com.dev.gware.customboard.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostService postService;

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

    @GetMapping
    public ResponseEntity<Object> getPostList(@RequestBody GetPostListReq req) {

        GetPostListRes res = postService.getPostList(req);

        return ResponseEntity.ok().body(res);
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
