package com.dev.gware.customboard.post.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePostReq {
    @NotBlank
    String title;

    @NotNull
    String content;
}
