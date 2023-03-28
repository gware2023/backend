package com.dev.gware.auth.dto.request;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class LoginRequest {

    private String usrId;

    private String usrPwd;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(usrId, usrPwd);
    }
}
