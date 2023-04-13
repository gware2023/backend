package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.post.domain.*;
import com.dev.gware.customboard.post.dto.request.AddPostReq;
import com.dev.gware.customboard.post.dto.request.GetPostListReq;
import com.dev.gware.customboard.post.dto.request.SurveyReq;
import com.dev.gware.customboard.post.dto.request.UpdatePostReq;
import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionReq;
import com.dev.gware.customboard.post.dto.response.*;
import com.dev.gware.customboard.post.dto.response.element.SurveyQuestionRes;
import com.dev.gware.customboard.post.repository.*;
import com.dev.gware.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AttachedFileRepository attachedFileRepository;
    private final ImgFileRepository imgFileRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyVoteRepository surveyVoteRepository;
    private final UserMapper userMapper;

    @Value("${attached.file.dir}")
    private String attachedFileDir;
    @Value("${img.file.dir}")
    private String imgFileDir;

    @Override
    @Transactional
    public void addPost(AddPostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, SurveyReq surveyReq) throws IOException {
        Post savedPost = savePost(req);
        long postId = savedPost.getPostId();

        if (attachedFiles != null) {
            saveFiles(attachedFiles, attachedFileRepository, attachedFileDir, postId);
        }
        if (imgFiles != null) {
            saveFiles(imgFiles, imgFileRepository, imgFileDir, postId);
        }
        if (surveyReq != null) {
            saveSurveyAndSurveyQuestion(surveyReq, postId, false);
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
    public GetSurveyRes getSurvey(long postId, long userId) {
        Survey survey = surveyRepository.findByPostId(postId);
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findBySurveyId(survey.getSurveyId());
        List<SurveyVote> surveyVoteList = surveyVoteRepository.findBySurveyIdAndAndUserId(survey.getSurveyId(), userId);

        GetSurveyRes res = new GetSurveyRes();
        res.setSurveyQuestionResList(new ArrayList<>());
        res.setQuestionIdListVotedByUser(new ArrayList<>());

        copyToGetSurveyRes(survey, surveyQuestionList, surveyVoteList, res);

        return res;
    }


    @Override
    public List<GetPostListRes> getPostList(GetPostListReq req) {
        Page<Post> postPage = findPostPage(req);

        List<GetPostListRes> resList = copyToGetPostListRes(postPage);

        return resList;
    }

    @Override
    @Transactional
    public void updatePost(long postId, UpdatePostReq req, List<MultipartFile> attachedFiles, List<MultipartFile> imgFiles, SurveyReq surveyReq) throws IOException {
        changePost(postId, req);

        deleteFiles(attachedFileRepository, attachedFileDir, postId);
        deleteFiles(imgFileRepository, imgFileDir, postId);
        surveyRepository.deleteByPostId(postId);

        if (attachedFiles != null) {
            saveFiles(attachedFiles, attachedFileRepository, attachedFileDir, postId);
        }
        if (imgFiles != null) {
            saveFiles(imgFiles, imgFileRepository, imgFileDir, postId);
        }
        if (surveyReq != null) {
            saveSurveyAndSurveyQuestion(surveyReq, postId, true);
        }
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteByPostId(postId);
    }


    /**
     * 추출 메서드
     **/
    private Post savePost(AddPostReq req) {
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
            repository.save(new AttachedFile(saveFileName, uploadFileName, ext, postId, null));
        } else if (repository instanceof ImgFileRepository) {
            repository.save(new ImgFile(saveFileName, uploadFileName, ext, postId, null));
        }
    }

    private void saveToFolder(MultipartFile file, String fileDir, String saveFileName, String ext) throws IOException {
        String fullPath = fileDir + saveFileName;
        file.transferTo(new File(fullPath));
    }

    private void saveSurveyAndSurveyQuestion(SurveyReq surveyReq, long postId, boolean isUpdating) {
        Survey savedSurvey = saveSurvey(surveyReq.getTitle(), postId, isUpdating);
        saveSurveyQuestion(surveyReq.getSurveyQuestionReqList(), savedSurvey.getSurveyId());
    }

    private Survey saveSurvey(String title, long postId, boolean isUpdating) {
        Survey survey = new Survey(title, postId);
        if (isUpdating) {
            survey.setModifyDatetime(null);
        }

        return surveyRepository.save(survey);
    }

    private void saveSurveyQuestion(List<SurveyQuestionReq> surveyQuestionReqList, long surveyId) {
        for (SurveyQuestionReq surveyQuestionReq : surveyQuestionReqList) {
            SurveyQuestion surveyQuestion = new SurveyQuestion(surveyQuestionReq.getQuestion(), surveyId);

            surveyQuestionRepository.save(surveyQuestion);
        }
    }

    private Page<Post> findPostPage(GetPostListReq req) {
        PageRequest pageRequest = PageRequest.of(req.getPageNum() - 1, 10, Sort.Direction.DESC, "postId");
        return postRepository.findByBoardId(req.getBoardId(), pageRequest);
    }

    private List<GetPostListRes> copyToGetPostListRes(Page<Post> postPage) {
        List<GetPostListRes> resList = new ArrayList<>();
        for (Post post : postPage) {
            GetPostListRes res = new GetPostListRes();
            BeanUtils.copyProperties(post, res);
            res.setUserName(userMapper.findByKey(post.getUserId()).getKorNm());
            resList.add(res);
        }
        return resList;
    }

    private void changePost(long postId, UpdatePostReq req) {
        Post post = postRepository.findByPostId(postId);
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setModifyDatetime(null);
        postRepository.save(post);
    }

    private void deleteFiles(JpaRepository repository, String fileDir, long postId) {
        deleteFromFolder(repository, fileDir, postId);
        deleteFromDB(repository, postId);
    }

    private void deleteFromFolder(JpaRepository repository, String fileDir, long postId) {
        if (repository instanceof AttachedFileRepository) {
            List<AttachedFile> attachedFileList = attachedFileRepository.findByPostId(postId);
            for (AttachedFile attachedFile : attachedFileList) {
                File file = new File(attachedFileDir + attachedFile.getStoreFileName());
                file.delete();
            }
        } else if (repository instanceof ImgFileRepository) {
            List<ImgFile> imgFileList = imgFileRepository.findByPostId(postId);
            for (ImgFile imgFile : imgFileList) {
                File file = new File(attachedFileDir + imgFile.getStoreFileName());
                file.delete();
            }
        }
    }

    private void deleteFromDB(JpaRepository repository, long postId) {
        if (repository instanceof AttachedFileRepository) {
            attachedFileRepository.deleteByPostId(postId);
        } else if (repository instanceof ImgFileRepository) {
            imgFileRepository.deleteByPostId(postId);
        }
    }

    private void copyToGetSurveyRes(Survey survey, List<SurveyQuestion> surveyQuestionList, List<SurveyVote> surveyVoteList, GetSurveyRes res) {
        BeanUtils.copyProperties(survey, res);
        for (SurveyQuestion surveyQuestion : surveyQuestionList) {
            SurveyQuestionRes surveyQuestionRes = new SurveyQuestionRes();
            BeanUtils.copyProperties(surveyQuestion, surveyQuestionRes);
            res.getSurveyQuestionResList().add(surveyQuestionRes);
        }
        for (SurveyVote surveyVote : surveyVoteList) {
            res.getQuestionIdListVotedByUser().add(surveyVote.getQuestionId());
        }
    }
}
