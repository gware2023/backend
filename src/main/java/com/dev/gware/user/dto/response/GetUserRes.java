package com.dev.gware.user.dto.response;

import com.dev.gware.user.domain.Users;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetUserRes {

    private Long userId;

    private String loginId;

    private String username;

    private String email;

    public GetUserRes(Users user) {
        this.userId = user.getId();
        this.loginId = user.getLoginId();
        this.username = user.getName();
        this.email = user.getEmail();
    }
}
