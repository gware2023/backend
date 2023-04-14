package com.dev.gware.customboard.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class GetPostListReq {
    @NotNull
    @Min(1L)
    long boardId;

    @NotNull
    @Range(min = 1, max = 1000)
    int pageNum;
}
