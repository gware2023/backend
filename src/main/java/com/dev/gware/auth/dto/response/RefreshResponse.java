package com.dev.gware.auth.dto.response;

import lombok.Data;

@Data
public class RefreshResponse {

    private String accessToken;

    private String refreshToken;

    public RefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
