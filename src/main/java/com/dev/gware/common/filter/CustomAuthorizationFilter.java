package com.dev.gware.common.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dev.gware.auth.jwt.JwtProvider;
import com.dev.gware.common.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (excludePath(request)) {
            // 회원가입, 로그인, 리프레시 토큰 요청은 검사 x
            filterChain.doFilter(request, response);
        } else if (jwtProvider.hasJwtHeader(authorizationHeader)) {
            // 토큰 없거나 정상적이지 않으면 401
            log.info("[CustomAuthorizationFilter] JWT Token이 존재하지 않습니다.");
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            new ObjectMapper().writeValue(response.getWriter(), new CommonResponse<>(false, SC_UNAUTHORIZED, "JWT Token이 존재하지 않습니다."));
        } else {
            try {
                // jwt 검증 + authentication 객체 가져오기
                Authentication authentication = jwtProvider.getAuthentication(authorizationHeader);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("[CustomAuthorizationFilter] Security Context 에 '{}' 인증 정보를 저장했습니다, uri", authentication.getName(), request.getRequestURI());

                // 인증 처리 후 다음 filter 수행
                filterChain.doFilter(request, response);
            } catch (TokenExpiredException e) {
                log.info("[CustomAuthorizationFilter] Access Token이 만료되었습니다.");
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(), new CommonResponse<>(false, SC_UNAUTHORIZED, "Access Token이 만료되었습니다."));
            } catch (Exception e) {
                log.info("[CustomAuthorizationFilter] JWT Token 이 잘못되었습니다. message: {}", e.getMessage());
                e.printStackTrace();
                response.setStatus(SC_UNAUTHORIZED);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("utf-8");
                new ObjectMapper().writeValue(response.getWriter(), new CommonResponse<>(false, SC_UNAUTHORIZED, "잘못된 JWT Token 입니다."));
            }
        }
    }

    private boolean excludePath(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        log.info("[CustomAuthorizationFilter] path: {}, method: {}", requestPath, requestMethod);

        // 회원 가입, 인증 관련 url 예외 처리
        return ("/api/users".equals(requestPath) && "POST".equals(requestMethod)) || requestPath.matches("/api/auth.*");
    }
}
