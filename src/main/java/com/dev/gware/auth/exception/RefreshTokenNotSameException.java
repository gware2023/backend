package com.dev.gware.auth.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class RefreshTokenNotSameException extends JWTVerificationException {

    public RefreshTokenNotSameException() {
        super("유효하지 않은 Refresh Token 입니다.");
    }
}
