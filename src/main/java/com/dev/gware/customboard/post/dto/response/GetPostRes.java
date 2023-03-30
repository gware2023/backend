package com.dev.gware.customboard.post.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPostRes {

    long postId;

    long boardId;

    String title;

    String content;

    long userId;

    String userName;

    String time;

    long viewCount;

    long recommendationCount;
}
