package com.dev.gware.user.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserReq {
    private Long usrKey;

    private String korNm;

    private String emailAddr;
}
