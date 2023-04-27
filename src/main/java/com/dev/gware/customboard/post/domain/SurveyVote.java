package com.dev.gware.customboard.post.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "SURVEY_VOTE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@DynamicInsert
public class SurveyVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SURVEY_VOTE_ID")
    long surveyVoteId;

    @NonNull
    @Column(name = "SURVEY_ID")
    long surveyId;

    @NonNull
    @Column(name = "QUESTION_ID")
    long questionId;

    @NonNull
    @Column(name = "USER_ID")
    long userId;

    @Column(name = "VOTE_DATETIME")
    String voteDatetime;
}
