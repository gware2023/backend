package com.dev.gware.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsrInfo {

    private Long usrKey;

    private String usrId;

    private String usrPwd;

    private String korNm;

    private String emailAddr;

    private String refreshToken;
}
