package com.dev.gware.customboard.post.dto.request;

import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RegistPostServey {
    String title;
    List<SurveyQuestionInfo> surveyQuestionInfoList;
}
