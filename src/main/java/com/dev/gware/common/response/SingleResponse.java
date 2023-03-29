package com.dev.gware.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleResponse<T> extends CommonResponse {

    private final T data;

    @Builder
    public SingleResponse(boolean success, int code, String message, T data) {
        super(success, code, message);
        this.data = data;
    }
}
