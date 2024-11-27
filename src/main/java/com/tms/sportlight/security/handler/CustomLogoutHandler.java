package com.tms.sportlight.security.handler;

import com.tms.sportlight.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTUtil jwtUtil;

    public CustomLogoutHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    try {
                        String refreshToken = cookie.getValue();
                        Claims claims = jwtUtil.getClaims(refreshToken);
                        String loginId = claims.get("loginId", String.class);
                        jwtUtil.deleteRefreshToken(loginId);
                        log.debug("리프레시 토큰 삭제 완료 - 사용자: {}", loginId);
                    } catch (Exception e) {
                        log.error("리프레시 토큰 삭제 실패", e);
                    }

                    Cookie expiredCookie = new Cookie("refresh", "");
                    expiredCookie.setHttpOnly(true);
                    expiredCookie.setSecure(false);
                    expiredCookie.setPath("/");
                    expiredCookie.setMaxAge(0);
                    expiredCookie.setDomain("");
                    response.addCookie(expiredCookie);
                    break;
                }
            }
        }
    }
}
