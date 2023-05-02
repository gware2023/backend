package com.dev.gware.user.service;

import com.dev.gware.auth.domain.AuthUser;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.dto.request.CreateUserReq;
import com.dev.gware.user.dto.request.UpdateUserReq;
import com.dev.gware.user.dto.response.GetUserRes;
import com.dev.gware.user.exception.UserNotFoundException;
import com.dev.gware.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Users findUser = userRepository.findByLoginId(loginId);
        if (findUser == null) {
            log.error("유저를 찾을 수 없습니다. [{}]", loginId);
            throw new UserNotFoundException();
        }
        log.debug("DB에서 유저를 찾았습니다. [{}]", loginId);
        return AuthUser.builder()
                .id(findUser.getId())
                .loginId(findUser.getLoginId())
                .password(findUser.getPassword())
                .build();
    }

    @Override
    public GetUserRes findById(Long userId) {
        Optional<Users> findUserOptional = userRepository.findById(userId);
        if (findUserOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        return new GetUserRes(findUserOptional.get());
    }

    @Override
    public Page<GetUserRes> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(GetUserRes::new);
    }

    @Override
    public void updateUser(UpdateUserReq updateUserReq) {
        Optional<Users> findUserOptional = userRepository.findById(updateUserReq.getUserId());
        if (findUserOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        Users findUser = findUserOptional.get();
        findUser.setName(updateUserReq.getUsername());
        findUser.setEmail(updateUserReq.getEmail());

        userRepository.save(findUser);
    }

    @Override
    public Users addUser(CreateUserReq createUserReq) {
        // TODO LoginID, Email 중복 검사, 패스워드 유효성 검사 등 추가해야됨 by.csi
        return userRepository.save(createUserReq.toUser());
    }
}
