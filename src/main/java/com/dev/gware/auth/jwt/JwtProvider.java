package com.dev.gware.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dev.gware.auth.domain.AuthUser;
import com.dev.gware.auth.mapper.AuthMapper;
import com.dev.gware.user.domain.UsrInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    public static final String JWT_SECRET = "dHlwZS1maS1zZWNyZXQ="; // "type-fi-secret" base64 encoding

    public static final String JWT_HEADER_PREFIX = "Bearer ";

    public static final String REFRESH_TOKEN_HEADER = "refresh_token";

    public static final long ONE_DAYS_MILLI_SEC = 1000 * 60 * 60 * 24; // 1일

    public static final long ACCESS_TOKEN_EXP_TIME = 1000 * 60 * 30; // 30분

    public static final long REFRESH_TOKEN_EXP_TIME = ONE_DAYS_MILLI_SEC * 7; // 7일

    // 리프레쉬 토큰 만료일이 이 날짜보다 안남았으면 갱신
    public static final long REFRESH_TOKEN_RENEWAL_DAYS = 1;

    private final Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());

    private final AuthMapper authMapper;

    public JwtProvider(AuthMapper authMapper) {
        this.authMapper = authMapper;
    }

    public boolean hasJwtHeader(String authorizationHeader) {
        return authorizationHeader == null || !authorizationHeader.startsWith(JWT_HEADER_PREFIX);
    }

    public String createAccessToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_TIME))
                .withClaim("roles", new ArrayList<>())
                .sign(algorithm);
    }

    public String createRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_TIME))
                .sign(algorithm);
    }

    // jwt 인증 객체 가져오기
    public Authentication getAuthentication(final String accessToken) {
        DecodedJWT decodedJWT = getDecodedJWT(accessToken);
        String usrId = decodedJWT.getSubject();

        UsrInfo findUsrInfo = authMapper.findById(usrId);
        AuthUser authUser = AuthUser.builder()
                .usrKey(findUsrInfo.getUsrKey())
                .usrId(findUsrInfo.getUsrId())
                .usrPwd(findUsrInfo.getUsrPwd())
                .build();

        return new UsernamePasswordAuthenticationToken(authUser, "", authUser.getAuthorities());
    }

    /**
     * Decoded jwt 토큰을 가져오고,
     * 토큰이 유효한지 검사한다.
     * @param token decode, 검증 대상의 토큰
     * @return Decoded 된 jwt 토큰
     */
    public DecodedJWT getDecodedJWT(String token) {

        // jwt header 제거
        if (token.contains(JWT_HEADER_PREFIX)) {
            token = token.substring(JWT_HEADER_PREFIX.length());
        }

        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public boolean renewalRefreshToken(final DecodedJWT decodedJWT) {
        long now = System.currentTimeMillis();
        long refreshExpiredTime = decodedJWT.getClaim("exp").asLong() * 1000;
        long diffTime = refreshExpiredTime - now;
        double diffDay = ((double) diffTime) / ONE_DAYS_MILLI_SEC;

        log.info("[refresh token] expired time: {}", refreshExpiredTime);
        log.info("[refresh token] current time: {}", now);
        log.info("[refresh token] diff time: {}", diffTime);
        log.info("[refresh token] diff day: {}", diffDay);

        return diffDay < REFRESH_TOKEN_RENEWAL_DAYS;
    }
}
