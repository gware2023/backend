package com.dev.gware.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> extends CommonResponse {

    private final List<T> dataList;

    @Builder

    public ListResponse(boolean success, int code, String message, List<T> dataList) {
        super(success, code, message);
        this.dataList = dataList;
    }
}
