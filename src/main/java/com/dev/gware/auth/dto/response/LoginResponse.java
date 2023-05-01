package com.dev.gware.auth.dto.response;

import com.dev.gware.user.domain.Users;
import lombok.Getter;

@Getter
public class LoginResponse {

    private Long userId;

    private String loginId;

    private String name;

    private String email;

    private String accessToken;

    private String refreshToken;

    public LoginResponse(Users user, String accessToken, String refreshToken) {
        this.userId = user.getId();
        this.loginId = user.getLoginId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
