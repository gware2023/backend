package com.dev.gware.user.dto;

import com.dev.gware.user.domain.UsrInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetUserRes {

    private Long usrKey;

    private String usrId;

    private String korNm;

    private String emailAddr;

    public GetUserRes(UsrInfo user) {
        this.usrKey = user.getUsrKey();
        this.usrId = user.getUsrId();
        this.korNm = user.getKorNm();
        this.emailAddr = user.getEmailAddr();
    }
}
