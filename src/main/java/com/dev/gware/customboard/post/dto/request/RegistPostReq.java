package com.dev.gware.customboard.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistPostReq {
    long boardId;
    String title;
    String content;
    long userId;
}
