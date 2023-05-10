package com.dev.gware.auth.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.dto.response.RefreshResponse;
import com.dev.gware.auth.exception.RefreshTokenNotSameException;
import com.dev.gware.auth.jwt.JwtProvider;
import com.dev.gware.user.domain.Users;
import com.dev.gware.user.exception.UserNotFoundException;
import com.dev.gware.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        log.info("{} attempt to login with {}", loginId, password);
        // Login ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        // 실제로 검증이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 AuthUserDetailService에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        Users findUser = userRepository.findByLoginId(authentication.getName());

        // Token 발급 + refresh token 갱신
        String accessToken = jwtProvider.createAccessToken(findUser.getLoginId());
        String refreshToken = jwtProvider.createRefreshToken(findUser.getLoginId());
        updateRefreshToken(findUser.getId(), refreshToken);

        return new LoginResponse(findUser, accessToken, refreshToken);
    }

    @Override
    public RefreshResponse refresh(String refreshToken) {
        // Refresh Token 유효성 검사
        DecodedJWT decodedJWT = jwtProvider.getDecodedJWT(refreshToken);

        // Access Token 재발급
        Users findUser = userRepository.findByLoginId(decodedJWT.getSubject());
        if (findUser == null) {
            throw new UserNotFoundException();
        }
        if (!refreshToken.equals(findUser.getRefreshToken())) {
            throw new RefreshTokenNotSameException();
        }

        // Access token 갱신
        String accessToken = jwtProvider.createAccessToken(findUser.getLoginId());
        RefreshResponse response = new RefreshResponse(accessToken, null);

        // 리프레시 토큰 남은 일수가 하루 미만이면 재발급
        if (jwtProvider.renewalRefreshToken(decodedJWT)) {
            String newRefreshToken = jwtProvider.createRefreshToken(findUser.getLoginId());

            // refresh token DB에 저장
            updateRefreshToken(findUser.getId(), newRefreshToken);
            response.setRefreshToken(newRefreshToken);
        }
        return response;
    }

    @Override
    public void updateRefreshToken(long userId, String refreshToken) {
        Optional<Users> findUserOptional = userRepository.findById(userId);
        if (findUserOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        Users findUser = findUserOptional.get();
        findUser.setRefreshToken(refreshToken);
        userRepository.save(findUser);
    }
}
