package com.dev.gware.common.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    public<T> SingleResponse<T> getSingleResponse(T data, HttpStatus httpStatus, String message) {
        return (SingleResponse<T>) SingleResponse.builder()
                .data(data)
                .code(httpStatus.value())
                .success(true)
                .message(message)
                .build();
    }

    public<T> SingleResponse<Object> getSingleResponse(T data, HttpStatus httpStatus) {
        return getSingleResponse(data, httpStatus, httpStatus.getReasonPhrase());
    }

    public ListResponse<Object> getListResponse(List<Object> dataList, HttpStatus httpStatus, String message) {
        return ListResponse.builder()
                .dataList(dataList)
                .code(httpStatus.value())
                .success(true)
                .message(message)
                .build();
    }

    public ListResponse<Object> getListResponse(List<Object> dataList, HttpStatus httpStatus) {
        return getListResponse(dataList, httpStatus, httpStatus.getReasonPhrase());
    }

    /**
     * 에러 응답
     * @param code 에러 코드
     * @param message 에러 메시지
     * @return
     */
    @Builder
    public CommonResponse getErrorResponse(int code, String message) {
        return new CommonResponse(false, code, message);
    }
}
