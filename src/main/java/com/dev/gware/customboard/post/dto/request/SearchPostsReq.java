package com.dev.gware.customboard.post.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
