package com.tms.sportlight.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationSuccessHandler authenticationSuccessHandler;


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
        Authentication authResult) throws IOException, ServletException {

        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
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

