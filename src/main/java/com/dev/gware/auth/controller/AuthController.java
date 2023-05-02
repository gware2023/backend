package com.dev.gware.auth.controller;

import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.dto.response.RefreshResponse;
import com.dev.gware.auth.service.AuthService;
import com.dev.gware.common.response.ResponseService;
import com.dev.gware.common.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.dev.gware.auth.jwt.JwtProvider.REFRESH_TOKEN_HEADER;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final ResponseService responseService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        return responseService.getSingleResponse(authService.login(loginRequest), HttpStatus.OK, "로그인에 성공했습니다.");
    }

    @GetMapping("/refresh")
    public SingleResponse<RefreshResponse> refreshToken(@RequestParam(REFRESH_TOKEN_HEADER) String refreshToken) {

        return responseService.getSingleResponse(authService.refresh(refreshToken), HttpStatus.OK, "토큰 발급 성공");
    }

}
