package com.dev.gware.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty("code")
    private final int code;

    @JsonProperty("message")
    private final String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
