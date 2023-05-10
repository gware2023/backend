package com.dev.gware.auth.service;

import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.dto.response.RefreshResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    RefreshResponse refresh(String refreshToken);

    void updateRefreshToken(long userId, String refreshToken);
}
