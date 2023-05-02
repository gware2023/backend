package com.dev.gware.customboard.post.dto.request;

import com.dev.gware.customboard.post.dto.request.element.SurveyQuestionReq;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SurveyReq {
    @NotBlank
    String title;

    @NotNull
    @Size(min = 1)
    List<SurveyQuestionReq> surveyQuestionReqList;
}
