package com.dev.gware.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dev.gware.auth.dto.request.LoginRequest;
import com.dev.gware.auth.dto.request.UpdateRefreshTokenRequest;
import com.dev.gware.auth.dto.response.LoginResponse;
import com.dev.gware.auth.dto.response.RefreshResponse;
import com.dev.gware.auth.exception.RefreshTokenNotSameException;
import com.dev.gware.auth.jwt.JwtProvider;
import com.dev.gware.auth.mapper.AuthMapper;
import com.dev.gware.user.domain.UsrInfo;
import com.dev.gware.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest loginRequest) {
        String usrId = loginRequest.getUsrId();
        String usrPwd = loginRequest.getUsrPwd();

        log.info("{} attempt to login with {}", usrId, usrPwd);
        // Login ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        // 실제로 검증이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 AuthUserDetailService에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UsrInfo findUsrInfo = authMapper.findById(authentication.getName());

        // Token 발급 + refresh token 갱신
        String accessToken = jwtProvider.createAccessToken(findUsrInfo.getUsrId());
        String refreshToken = jwtProvider.createRefreshToken(findUsrInfo.getUsrId());
        updateRefreshToken(findUsrInfo.getUsrKey(), refreshToken);

        return new LoginResponse(findUsrInfo, accessToken, refreshToken);
    }

    public RefreshResponse refresh(String refreshToken) {

        // Refresh Token 유효성 검사
        DecodedJWT decodedJWT = jwtProvider.getDecodedJWT(refreshToken);

        // Access Token 재발급
        UsrInfo findUsrInfo = authMapper.findById(decodedJWT.getSubject());
        if (findUsrInfo == null) {
            throw new UserNotFoundException();
        }
        if (!refreshToken.equals(findUsrInfo.getRefreshToken())) {
            throw new RefreshTokenNotSameException();
        }

        // Access token 갱신
        String accessToken = jwtProvider.createAccessToken(findUsrInfo.getUsrId());
        RefreshResponse response = new RefreshResponse(accessToken, null);

        // 리프레시 토큰 남은 일수가 하루 미만이면 재발급
        if (jwtProvider.renewalRefreshToken(decodedJWT)) {
            String newRefreshToken = jwtProvider.createRefreshToken(findUsrInfo.getUsrId());

            // refresh token DB에 저장
            updateRefreshToken(findUsrInfo.getUsrKey(), newRefreshToken);
            response.setRefreshToken(newRefreshToken);
        }
        return response;
    }

    public void updateRefreshToken(long usrKey, String refreshToken) {
        authMapper.updateRefreshTokenById(new UpdateRefreshTokenRequest(usrKey, refreshToken));
    }
}
