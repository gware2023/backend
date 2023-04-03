package com.dev.gware.customboard.post.dto.response;

import com.dev.gware.customboard.post.dto.response.element.PostInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GetPostListRes {

    List<PostInfo> postInfoList;
}
