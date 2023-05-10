package com.dev.gware.global.handler;


import com.dev.gware.auth.exception.RefreshTokenNotSameException;
import com.dev.gware.common.response.CommonResponse;
import com.dev.gware.common.response.ResponseService;
import com.dev.gware.message.exception.MessageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public CommonResponse handleException(final Exception e) {
        e.printStackTrace();
        return responseService.getErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            MessageNotFoundException.class
    })
    @ResponseStatus(NOT_FOUND)
    public CommonResponse notFoundException(final Exception e) {
        e.printStackTrace();
        return responseService.getErrorResponse(NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(RefreshTokenNotSameException.class)
    @ResponseStatus(BAD_REQUEST)
    public CommonResponse badRequestException(final Exception e) {
        e.printStackTrace();
        return responseService.getErrorResponse(BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    public CommonResponse badCredentialsException(final Exception e) {
        e.printStackTrace();
        return responseService.getErrorResponse(BAD_REQUEST.value(), "로그인에 실패했습니다.");
    }
}
