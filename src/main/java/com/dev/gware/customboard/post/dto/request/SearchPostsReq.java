package com.dev.gware.customboard.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SearchPostsReq {
    @NotNull
    long boardId;

    @NotNull
    @Range(min = 0, max = 2)
    int type;

    @NotBlank
    String keyword;

    @NotNull
    @Range(min = 1, max = 1000)
    int page;
}
