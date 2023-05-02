package com.dev.gware.customboard.post.service;

import com.dev.gware.customboard.board.domain.Board;
import com.dev.gware.customboard.board.dto.request.AddBoardReq;
import com.dev.gware.customboard.board.repository.BoardRepository;
import com.dev.gware.customboard.board.service.BoardService;
import com.dev.gware.customboard.post.domain.Post;
import com.dev.gware.customboard.post.domain.Survey;
import com.dev.gware.customboard.post.domain.SurveyQuestion;
import com.dev.gware.customboard.post.dto.request.*;
import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionReq;
import com.dev.gware.customboard.post.dto.response.GetPostListRes;
import com.dev.gware.customboard.post.dto.response.GetSurveyRes;
import com.dev.gware.customboard.post.exception.QuestionNotIncludedInSurveyException;
import com.dev.gware.customboard.post.repository.*;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.dto.request.CreateUserReq;
import com.dev.gware.user.repository.UserRepository;
import com.dev.gware.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
class PostServiceImplTest {

    private long userId = 0L;
    private long boardId = 0L;

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostRecommendationRepository postRecommendationRepository;
    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    SurveyVoteRepository surveyVoteRepository;

    @BeforeEach
    void clearRepository() {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        postRepository.deleteAll();

        Users user = userService.addUser(new CreateUserReq("loginId", "asd123", "노호준", "email@email.com"));
        userId = user.getId();
        Board board = boardService.addBoard(new AddBoardReq("게시판 이름"), userId);
        boardId = board.getBoardId();
    }

    @Test
    void addPost() throws IOException {
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);

        assertThat(postRepository.findByPostId(post.getPostId()).getTitle()).isEqualTo("제목");
    }

    @Test
    void getPost() throws IOException {
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);

        assertThat(postService.getPost(post.getPostId()).getTitle()).isEqualTo("제목");
    }


    @Test
    void getSurvey() throws IOException {
        List<SurveyQuestionReq> surveyQuestionReqList = new ArrayList<>();
        surveyQuestionReqList.add(new SurveyQuestionReq("투표 항목1"));
        surveyQuestionReqList.add(new SurveyQuestionReq("투표 항목2"));
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, new SurveyReq("투표 제목", surveyQuestionReqList), userId);

        GetSurveyRes res = postService.getSurvey(post.getPostId(), userId);

        assertThat(res.getTitle()).isEqualTo("투표 제목");
        for (int i = 0; i < 2; i++) {
            assertThat(res.getSurveyQuestionResList().get(i).getQuestion()).contains("투표 항목");
        }
    }

    @Test
    void getPostList() throws IOException {
        for (int i = 0; i < 10; i++) {
            postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);
        }

        List<GetPostListRes> postList = postService.getPostList(new GetPostListReq(boardId, 1));
        assertThat(postList.size()).isEqualTo(10);
        for (int i = 0; i < 10; i++) {
            assertThat(postList.get(i).getTitle()).isEqualTo("제목");
        }
    }

    @Test
    void updatePost() throws IOException {
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);

        postService.updatePost(post.getPostId(), new UpdatePostReq("바꿀 제목", "바꿀 내용"), null, null, null, userId);

        assertThat(postRepository.findByPostId(post.getPostId()).getTitle()).isEqualTo("바꿀 제목");
    }

    @Test
    void deletePost() throws IOException {
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);

        postService.deletePost(post.getPostId(), userId);

        assertThat(postRepository.findByPostId(post.getPostId())).isNull();
    }

    @Test
    void searchPosts() throws IOException {
        postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);
        postService.addPost(new AddPostReq(boardId, "123", "456"), null, null, null, userId);
        postService.addPost(new AddPostReq(boardId, "abc", "def"), null, null, null, userId);

        assertThat(postService.searchPosts(new SearchPostsReq(boardId, 0, "12", 1)).size()).isEqualTo(1);
        assertThat(postService.searchPosts(new SearchPostsReq(boardId, 1, "ef", 1)).size()).isEqualTo(1);
    }

    @Test
    void recommendPost() throws IOException {
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);

        postService.recommendPost(post.getPostId(), userId);

        assertThat(postRepository.findByPostId(post.getPostId()).getRecommendationCount()).isEqualTo(1L);
        assertThat(postRecommendationRepository.findByPostIdAndUserId(post.getPostId(), userId)).isNotNull();
    }

    @Test
    void cancelPostRecommendation() throws IOException {
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, null, userId);
        postService.recommendPost(post.getPostId(), userId);

        postService.cancelPostRecommendation(post.getPostId(), userId);

        assertThat(postRepository.findByPostId(post.getPostId()).getRecommendationCount()).isEqualTo(0L);
        assertThat(postRecommendationRepository.findByPostIdAndUserId(post.getPostId(), userId)).isNull();
    }

    @Test
    void vote() throws IOException, QuestionNotIncludedInSurveyException {
        List<SurveyQuestionReq> surveyQuestionReqList = new ArrayList<>();
        surveyQuestionReqList.add(new SurveyQuestionReq("투표 항목1"));
        surveyQuestionReqList.add(new SurveyQuestionReq("투표 항목2"));
        Post post = postService.addPost(new AddPostReq(boardId, "제목", "내용"), null, null, new SurveyReq("투표 제목", surveyQuestionReqList), userId);

        Survey survey = surveyRepository.findByPostId(post.getPostId());
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findBySurveyId(survey.getSurveyId());
        List<Long> votedQuestionIdList = new ArrayList<>();
        for (SurveyQuestion surveyQuestion : surveyQuestionList) {
            votedQuestionIdList.add(surveyQuestion.getQuestionId());
        }
        postService.vote(new VoteReq(survey.getSurveyId(), votedQuestionIdList), userId);

        surveyQuestionList = surveyQuestionRepository.findBySurveyId(survey.getSurveyId());
        for (SurveyQuestion surveyQuestion : surveyQuestionList) {
            assertThat(surveyQuestion.getVoteCount()).isEqualTo(1L);
        }
        assertThat(surveyVoteRepository.findBySurveyIdAndUserId(survey.getSurveyId(), userId).size()).isEqualTo(2);

        votedQuestionIdList = new ArrayList<>();
        for (SurveyQuestion surveyQuestion : surveyQuestionList) {
            votedQuestionIdList.add(surveyQuestion.getQuestionId() + 1L);
        }
        List<Long> finalVotedQuestionIdList = votedQuestionIdList;
        assertThatExceptionOfType(QuestionNotIncludedInSurveyException.class).isThrownBy(() -> postService.vote(new VoteReq(survey.getSurveyId(), finalVotedQuestionIdList), userId));
    }
}