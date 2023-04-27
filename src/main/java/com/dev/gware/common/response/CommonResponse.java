package com.dev.gware.common.response;

import lombok.Getter;

@Getter
public class CommonResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    public CommonResponse(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = (T) "";
    }

    public CommonResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
