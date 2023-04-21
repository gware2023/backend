package com.dev.gware.customboard.comment.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class GetCommentListReq {
    @NotNull
    @Min(1L)
    long postId;
}
