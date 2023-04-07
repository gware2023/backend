package com.dev.gware.customboard.post.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RegistPostReq {
    @Min(1L)
    long boardId;
    @NotBlank
    String title;
    @NotNull
    String content;
    @Min(1L)
    long userId;
}
