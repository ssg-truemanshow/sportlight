package com.tms.sportlight.security;

import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = servletRequest.getHeader("Authorization");
            log.info("token={}", token);
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String principal = jwtUtil.getUsername(token);
                if(principal != null && !principal.isEmpty()){
                    Authentication auth = new UsernamePasswordAuthenticationToken(principal, ""); // JWT 검증
                    attributes.put("auth", auth);
                    log.info("HandshakeInterceptor 인증 완료: {}", principal);
                    return true;
                }
            }
        }
        log.info("HandshakeInterceptor 인증 실패");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
