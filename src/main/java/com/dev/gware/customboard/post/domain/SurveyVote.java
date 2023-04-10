package com.dev.gware.customboard.post.domain;

import lombok.*;

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
    long surveyId;
    long questionId;
    long userId;
}
