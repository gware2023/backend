package com.dev.gware.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserReq {
    private Long userId;

    private String username;

    private String email;
}
