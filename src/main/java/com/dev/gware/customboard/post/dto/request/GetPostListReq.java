package com.dev.gware.customboard.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
public class GetPostListReq {
    @Min(1L)
    long boardId;
    @Range(min = 1, max = 1000)
    int pageNum;
}
