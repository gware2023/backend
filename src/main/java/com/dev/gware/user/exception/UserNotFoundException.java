package com.dev.gware.user.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotFoundException extends UsernameNotFoundException {

    public UserNotFoundException() {
        super("유저를 찾을 수 없습니다.");
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
