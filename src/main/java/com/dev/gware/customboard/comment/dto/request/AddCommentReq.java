package com.dev.gware.customboard.comment.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AddCommentReq {
    @NotBlank
    String content;
    @NotNull
    @Min(1L)
    long postId;
}
