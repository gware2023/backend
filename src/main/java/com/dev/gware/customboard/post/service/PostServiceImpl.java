package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.domain.*;
import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.RegistPostReq;
import com.dev.gware.customboard.post.dto.request.RegistPostServey;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionInfo;
import com.dev.gware.customboard.post.dto.response.GetAttachedFileListRes;
import com.dev.gware.customboard.post.dto.response.GetImgFileListRes;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetPostRes;
import com.dev.gware.customboard.post.repository.*;
import com.dev.gware.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    SurveyRepository surveyRepository;
    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    UserMapper userMapper;

    @Value("${attached.file.dir}")
    private String attachedFileDir;
    @Value("${img.file.dir}")
    private String imgFileDir;

    @Override
    @Transactional
    public void registPost(RegistPostReq req, List<MultipartFile> attchedFiles, List<MultipartFile> imgFiles, RegistPostServey surveyReq) throws IOException {
        Post savedPost = savePost(req);
        long postId = savedPost.getPostId();

        if (attchedFiles != null) {
            saveFiles(attchedFiles, attachedFileRepository, attachedFileDir, postId);
        }
        if (imgFiles != null) {
            saveFiles(imgFiles, imgFileRepository, imgFileDir, postId);
        }
        if (surveyReq != null) {
            saveSurveyAndSurveyQuestion(surveyReq, postId);
        }
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
    public List<GetAttachedFileListRes> getAttachedFileList(long postId) {
        List<AttachedFile> attachedFileList = attachedFileRepository.findByPostId(postId);

        List<GetAttachedFileListRes> resList = new ArrayList<>();
        for (AttachedFile attachedFile : attachedFileList) {
            GetAttachedFileListRes res = new GetAttachedFileListRes();
            BeanUtils.copyProperties(attachedFile, res);
            resList.add(res);
        }

        return resList;
    }

    @Override
    public List<GetImgFileListRes> getImgFileList(long postId) {
        List<ImgFile> imgFileList = imgFileRepository.findByPostId(postId);

        List<GetImgFileListRes> resList = new ArrayList<>();
        for (ImgFile imgFile : imgFileList) {
            GetImgFileListRes res = new GetImgFileListRes();
            BeanUtils.copyProperties(imgFile, res);
            resList.add(res);
        }

        return resList;
    }

    @Override
    public UrlResource downloadAttachedFile(String storeFileName) throws MalformedURLException {
        return new UrlResource("file:" + attachedFileDir + storeFileName);
    }

    @Override
    public UrlResource downloadImgFile(String storeFileName) throws MalformedURLException {
        return new UrlResource("file:" + imgFileDir + storeFileName);
    }

    @Override
    public List<GetPostListRes> getPostList(GetPostListReq req) {
        Page<Post> postPage = findPostPage(req);

        List<GetPostListRes> resList = copyToResList(postPage);

        return resList;
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


    /**
     * 추출 메서드
     **/
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
        int index = uploadFileName.lastIndexOf('.');
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
        String fullPath = fileDir + saveFileName;
        file.transferTo(new File(fullPath));
    }

    private void saveSurveyAndSurveyQuestion(RegistPostServey surveyReq, long postId) {
        Survey savedSurvey = saveSurvey(surveyReq.getTitle(), postId);
        saveSurveyQuestion(surveyReq.getSurveyQuestionInfoList(), savedSurvey.getSurveyId());
    }

    private Survey saveSurvey(String title, long postId) {
        Survey survey = new Survey(title, postId);

        return surveyRepository.save(survey);
    }

    private void saveSurveyQuestion(List<SurveyQuestionInfo> surveyQuestionInfoList, long surveyId) {
        for (SurveyQuestionInfo surveyQuestionInfo : surveyQuestionInfoList) {
            SurveyQuestion surveyQuestion = new SurveyQuestion(surveyQuestionInfo.getQuestion(), surveyId);

            surveyQuestionRepository.save(surveyQuestion);
        }
    }

    private Page<Post> findPostPage(GetPostListReq req) {
        PageRequest pageRequest = PageRequest.of(req.getPageNum() - 1, 10, Sort.Direction.DESC, "postId");
        return postRepository.findByBoardId(req.getBoardId(), pageRequest);
    }

    private List<GetPostListRes> copyToResList(Page<Post> postPage) {
        List<GetPostListRes> resList = new ArrayList<>();
        for (Post post : postPage) {
            GetPostListRes res = new GetPostListRes();
            BeanUtils.copyProperties(post, res);
            res.setUserName(userMapper.findByKey(post.getUserId()).getKorNm());
            resList.add(res);
        }
        return resList;
    }
}
