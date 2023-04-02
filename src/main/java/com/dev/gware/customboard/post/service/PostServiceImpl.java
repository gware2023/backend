package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.domain.AttachedFile;
import com.dev.gware.customboard.post.domain.ImgFile;
import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.dto.PostInfo;
import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;
import com.dev.gware.customboard.post.repository.AttachedFileRepository;
import com.dev.gware.customboard.post.repository.ImgFileRepository;
import com.dev.gware.customboard.post.repository.PostRepository;
import com.dev.gware.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    AttachedFileRepository attachedFileRepository;

    @Autowired
    ImgFileRepository imgFileRepository;

    @Autowired
    UserMapper userMapper;

    @Value("${attached.file.dir}")
    private String attachedFileDir;

    @Value("${img.file.dir}")
    private String imgFileDir;

    @Override
    public void registPost(RegistPostReq req, List<MultipartFile> attchedFiles, List<MultipartFile> imgFiles) throws IOException {
        Post savedPost = savePost(req);

        if (attchedFiles != null) {
            saveFiles(attchedFiles, attachedFileRepository, attachedFileDir, savedPost.getPostId());
        }

        if (imgFiles != null) {
            saveFiles(imgFiles, imgFileRepository, imgFileDir, savedPost.getPostId());
        }
    }

    private Post savePost(RegistPostReq req) {
        Post post = new Post();
        BeanUtils.copyProperties(req, post);

        return postRepository.save(post);
    }

    private void saveFiles(List<MultipartFile> files, JpaRepository repository, String fileDir, long postId) throws IOException {
        for (MultipartFile file : files) {
            String uploadFileName = file.getOriginalFilename();
            String ext = extractExt(uploadFileName);
            String saveFileName = UUID.randomUUID().toString() + "." + ext;

            saveToDB(repository, saveFileName, uploadFileName, ext, postId);

            saveToFolder(file, fileDir, saveFileName, ext);
        }
    }

    private String extractExt(String uploadFileName) {
        int index = uploadFileName.indexOf('.');
        String ext = null;
        if (index != -1) {
            ext = uploadFileName.substring(index + 1);
        }
        return ext;
    }

    private void saveToDB(JpaRepository repository, String saveFileName, String uploadFileName, String ext, long postId) {
        if (repository instanceof AttachedFileRepository) {
            repository.save(new AttachedFile(saveFileName, uploadFileName, ext, postId));
        } else if (repository instanceof ImgFileRepository) {
            repository.save(new ImgFile(saveFileName, uploadFileName, ext, postId));
        }
    }

    private void saveToFolder(MultipartFile file, String fileDir, String saveFileName, String ext) throws IOException {
        String fullPath = fileDir + saveFileName + "." + ext;
        file.transferTo(new File(fullPath));
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
