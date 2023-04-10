package com.dev.gware.customboard.post.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SURVEY_VOTE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class SurveyVote {
    @Id
    long dummyPK;

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
