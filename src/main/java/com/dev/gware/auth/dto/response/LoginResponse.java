package com.dev.gware.auth.dto.response;

import com.dev.gware.user.domain.UsrInfo;
import lombok.Getter;

@Getter
public class LoginResponse {

    private Long usrKey;

    private String usrId;

    private String korNm;

    private String emailAddr;

    private String accessToken;

    private String refreshToken;

    public LoginResponse(UsrInfo usrInfo, String accessToken, String refreshToken) {
        this.usrKey = usrInfo.getUsrKey();
        this.usrId = usrInfo.getUsrId();
        this.korNm = usrInfo.getKorNm();
        this.emailAddr = usrInfo.getEmailAddr();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
