package com.dev.gware.customboard.post.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "SURVEY_QUESTION")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    long questionId;

    @NonNull
    @Column(name = "QUESTION")
    String question;

    @Column(name = "VOTE_COUNT")
    long voteCount;

    @NonNull
    @Column(name = "SURVEY_ID")
    long surveyId;
}
