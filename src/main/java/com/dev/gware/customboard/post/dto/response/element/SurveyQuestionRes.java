package com.dev.gware.customboard.post.dto.response.element;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SurveyQuestionRes {
    long questionId;
    String question;
    long voteCount;
}
