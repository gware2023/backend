package com.dev.gware.user.dto.request;

import com.dev.gware.user.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Getter
@AllArgsConstructor
public class CreateUserReq {

    private String loginId;

    private String password;

    private String name;

    private String email;

    public Users toUser() {
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return new Users(loginId, hashPassword, name, email);
    }
}
