package com.dev.gware.customboard.comment.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetCommentListRes {
    long commentId;
    String content;
    String userName;
    String createDatetime;
}
