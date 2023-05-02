package com.dev.gware.customboard.exhandler;

import com.dev.gware.customboard.post.exception.QuestionNotIncludedInSurveyException;
import com.dev.gware.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "com.dev.gware.customboard")
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse QuestionNotIncludedInSurveyExceptionHandle(QuestionNotIncludedInSurveyException e) {
        return new ErrorResponse("400", "항목ID와 설문조사ID가 일치하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse MethodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        return new ErrorResponse("400", "파라미터가 유효하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse MethodArgumentTypeMismatchExceptionHandle(MethodArgumentTypeMismatchException e) {
        return new ErrorResponse("400", "파라미터 타입이 유효하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse HttpMessageNotReadableExceptionHandle(HttpMessageNotReadableException e) {
        return new ErrorResponse("400", "JSON 형식이 유효하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UserNotFoundException.class)
    public ErrorResponse userNotFoundException(UserNotFoundException e) {
        return new ErrorResponse("404", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse exceptionHandle(Exception e) {
        return new ErrorResponse("500", "서버 에러");
    }
}
