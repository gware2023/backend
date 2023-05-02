package com.dev.gware.customboard.post.dto.request;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddPostReq {
    @NotNull
    @Min(1L)
    long boardId;

    @NotBlank
    String title;

    @NotNull
    String content;
}
