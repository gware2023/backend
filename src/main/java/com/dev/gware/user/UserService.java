package com.dev.gware.user;

import com.dev.gware.auth.domain.AuthUser;
import com.dev.gware.user.domain.UsrInfo;
import com.dev.gware.user.exception.UserNotFoundException;
import com.dev.gware.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usrId) throws UsernameNotFoundException {
        UsrInfo findUsrInfo = userMapper.findById(usrId);
        if (findUsrInfo == null) {
            log.error("유저를 찾을 수 없습니다. [{}]", usrId);
            throw new UserNotFoundException();
        }
        log.debug("DB에서 유저를 찾았습니다. [{}]", usrId);
        return AuthUser.builder()
                .usrKey(findUsrInfo.getUsrKey())
                .usrId(findUsrInfo.getUsrId())
                .usrPwd(findUsrInfo.getUsrPwd())
                .build();
    }
}
