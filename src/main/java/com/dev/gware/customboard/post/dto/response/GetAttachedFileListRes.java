package com.dev.gware.customboard.post.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetAttachedFileListRes {
    String storeFileName;
    String uploadFileName;
}
