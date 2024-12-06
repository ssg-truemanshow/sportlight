package com.tms.sportlight.security.filter;

import com.tms.sportlight.domain.User;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/auth/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("/auth/oauth/additional-info")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        try {
            if (jwtUtil.isExpired(token)) {
                throw new BizException(ErrorCode.EXPIRED_TOKEN);
            }

            String loginId = jwtUtil.getUsername(token);
            User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));

            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception e) {
            throw new BizException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        filterChain.doFilter(request, response);
    }


}
