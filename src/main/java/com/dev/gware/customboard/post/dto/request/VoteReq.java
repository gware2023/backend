package com.dev.gware.customboard.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class VoteReq {
    @Min(1L)
    long surveyId;
    @NotNull
    @Size(min = 1)
    List<Long> votedQuestionIdList;
}
