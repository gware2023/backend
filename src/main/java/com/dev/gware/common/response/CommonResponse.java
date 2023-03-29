package com.dev.gware.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
}
