package com.tms.sportlight.security.filter;

import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 아이디, 패스워드 추출
        String loginId = obtainUsername(request);
        String loginPwd = obtainPassword(request);

        //아이디, 패스워드를 검증하기 위해서는 token 에 담아줘야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, loginPwd, null);

        //token 에 담은 검증을 위한 AuthenticationManager 로 전달
        //검증 로직은 DB 에서 회원정보를 가져온 후 UserDetailsService 에서 검증을 진행
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 (JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //특정한 유저를 확인
        //타입 캐스팅 방법
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String loginId = customUserDetails.getUsername();

        List<String> roles = customUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        String token = jwtUtil.createJwt(loginId, roles, Duration.ofMinutes(10).toMillis());

        response.addHeader("Authorization", "Bearer " + token);

        /*response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\":\"" + token + "\"}");*/

    }

    //로그인 실패시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);

    }
}
