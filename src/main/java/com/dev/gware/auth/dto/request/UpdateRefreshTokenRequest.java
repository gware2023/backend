package com.dev.gware.auth.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateRefreshTokenRequest {

    private final long userId;

    private final String refreshToken;
}
