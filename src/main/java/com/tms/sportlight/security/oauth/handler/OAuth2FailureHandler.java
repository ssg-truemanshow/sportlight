package com.tms.sportlight.security.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());

        String htmlResponse = String.format("""
            <!DOCTYPE html>
            <html>
            <body>
            <script>
            try {
                window.opener.postMessage({
                    error: '%s'
                }, 'http://localhost:5173');
            } finally {
                window.close();
            }
            </script>
            </body>
            </html>
            """, "로그인에 실패했습니다: " + exception.getMessage());

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(htmlResponse);
    }
}