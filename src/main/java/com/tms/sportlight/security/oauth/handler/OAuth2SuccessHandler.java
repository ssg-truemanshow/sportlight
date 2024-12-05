package com.tms.sportlight.security.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final long REFRESH_TOKEN_EXPIRY = 1000 * 60 * 60 * 24 * 7L; // 7일
    private static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 15L; // 15분
    private static final String ALLOWED_ORIGIN = "http://localhost:5173";

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        try {
            log.debug("OAuth2 로그인 시도 - Principal type: {}", authentication.getPrincipal().getClass());

            String provider = extractProvider(request.getRequestURI());
            String socialId = extractSocialId(authentication, provider);

            User user = userRepository.findBySocialIdAndJoinMethod(socialId, provider)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));

            String accessToken = createAccessToken(user);
            String refreshToken = createAndStoreRefreshToken(user, response);

            sendSuccessResponse(response, accessToken, user.getId(),
                user.isRequiresAdditionalInfo());

        } catch (BizException e) {
            log.error("OAuth2 로그인 처리 중 비즈니스 예외 발생: {}", e.getMessage());
            sendErrorResponse(response, e.getMessage());
        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 예외 발생", e);
            sendErrorResponse(response, "로그인 처리 중 오류가 발생했습니다");
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, String token, Long userId,
        boolean requiresAdditionalInfo) throws IOException {
        configureCors(response);

        String htmlResponse = String.format("""
            <!DOCTYPE html>
            <html>
            <body>
            <script>
            try {
                window.opener.postMessage({
                    token: '%s',
                    userId: '%d',
                    requiresAdditionalInfo: %b
                }, '%s');
            } catch (e) {
                console.error('Failed to send message:', e);
            } finally {
                window.close();
            }
            </script>
            </body>
            </html>
            """, token, userId, requiresAdditionalInfo, ALLOWED_ORIGIN);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(htmlResponse);
    }

    private String extractSocialId(Authentication authentication, String provider) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OidcUser oidcUser) {
            if ("KAKAO".equals(provider) || "GOOGLE".equals(provider)) {
                return oidcUser.getSubject();
            }
        }

        if (principal instanceof OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();

            if ("NAVER".equals(provider)) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                if (response == null || !response.containsKey("id")) {
                    throw new BizException(ErrorCode.INVALID_SOCIAL_CREDENTIALS);
                }
                return response.get("id").toString();
            }
        }

        throw new BizException(ErrorCode.INVALID_SOCIAL_PROVIDER);
    }

    private String createAccessToken(User user) {
        return jwtUtil.createJwt(
            user.getLoginId(),
            user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList()),
            ACCESS_TOKEN_EXPIRY
        );
    }

    private String createAndStoreRefreshToken(User user, HttpServletResponse response) {
        String refreshToken = jwtUtil.createRefreshToken(user.getLoginId(), REFRESH_TOKEN_EXPIRY);

        jwtUtil.storeRefreshToken(user.getLoginId(), refreshToken, REFRESH_TOKEN_EXPIRY);

        Cookie cookie = new Cookie("refresh", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (REFRESH_TOKEN_EXPIRY / 1000));
        response.addCookie(cookie);

        return refreshToken;
    }

    private String extractProvider(String uri) {
        if (uri.contains("kakao")) {
            return "KAKAO";
        } else if (uri.contains("naver")) {
            return "NAVER";
        } else if (uri.contains("google")) {
            return "GOOGLE";
        }
        throw new BizException(ErrorCode.INVALID_SOCIAL_PROVIDER);
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage)
        throws IOException {
        configureCors(response);

        String htmlResponse = String.format("""
            <!DOCTYPE html>
            <html>
            <body>
            <script>
            try {
                window.opener.postMessage({
                    error: '%s'
                }, '%s');
            } finally {
                window.close();
            }
            </script>
            </body>
            </html>
            """, errorMessage, ALLOWED_ORIGIN);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(htmlResponse);
    }

    private void configureCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
