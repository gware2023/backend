package com.dev.gware.customboard.post.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPostListRes {
    long postId;
    String title;
    String userName;
    String time;
    long viewCount;
    long recommendationCount;
}
