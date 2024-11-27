package com.tms.sportlight.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String loginId;
        String loginPwd;

        try {
            loginId = obtainUsername(request);
            loginPwd = obtainPassword(request);

            if (loginId == null || loginId.isEmpty()) {
                throw new BizException(ErrorCode.INVALID_REQUEST_LOGIN);
            }

            if (loginPwd == null || loginPwd.isEmpty()) {
                throw new BizException(ErrorCode.INVALID_REQUEST_LOGIN);
            }

            loginId = loginId.trim();
            loginPwd = loginPwd.trim();

            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginId, loginPwd);

            setDetails(request, authRequest);

            return this.authenticationManager.authenticate(authRequest);

        } catch (AuthenticationServiceException e) {
            throw e;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException {


        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String loginId = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        String accessToken = jwtUtil.createJwt(loginId, roles, Duration.ofMinutes(15).toMillis());
        String refreshToken = jwtUtil.createRefreshToken(loginId, Duration.ofDays(7).toMillis());

        jwtUtil.storeRefreshToken(loginId, refreshToken, Duration.ofDays(7).toMillis());

        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) Duration.ofDays(7).getSeconds());
        response.addCookie(cookie);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 200);
        responseBody.put("message", "로그인 성공");
        responseBody.put("token", accessToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 401);

        String errorMessage;
        if (failed instanceof AuthenticationServiceException) {
            errorMessage = failed.getMessage();
        } else {
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
        }

        responseBody.put("message", errorMessage);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}

