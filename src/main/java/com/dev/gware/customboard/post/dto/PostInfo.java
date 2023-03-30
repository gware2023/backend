package com.dev.gware.customboard.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostInfo {
    long postId;
    String title;
    String userName;
    String time;
    long viewCount;
    long recommendationCount;
}
