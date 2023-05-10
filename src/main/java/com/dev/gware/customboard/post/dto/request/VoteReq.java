package com.dev.gware.customboard.post.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoteReq {
    @NotNull
    @Min(1L)
    long surveyId;
    @NotNull
    @Size(min = 1)
    List<Long> votedQuestionIdList;
}
