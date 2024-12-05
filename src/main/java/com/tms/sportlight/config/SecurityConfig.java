package com.tms.sportlight.config;

import com.tms.sportlight.security.filter.JWTFilter;
import com.tms.sportlight.security.filter.LoginFilter;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.security.handler.CustomAccessDeniedHandler;
import com.tms.sportlight.security.handler.CustomAuthenticationFailureHandler;
import com.tms.sportlight.security.handler.CustomLoginSuccessHandler;
import com.tms.sportlight.security.handler.CustomLogoutHandler;
import com.tms.sportlight.security.handler.CustomLogoutSuccessHandler;
import com.tms.sportlight.security.oauth.handler.OAuth2FailureHandler;
import com.tms.sportlight.security.oauth.handler.OAuth2SuccessHandler;
import com.tms.sportlight.security.oauth.service.CustomOAuth2UserService;
import com.tms.sportlight.security.oauth.service.CustomOidcUserService;
import com.tms.sportlight.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomOidcUserService customOidcUserService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:5173");
        configuration.setAllowedMethods(
            List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(
            List.of("Content-Type", "Authorization", "X-Requested-With", "Origin", "Accept",
                "Cookie"));
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        UserRepository userRepository) throws Exception {

        http.
            cors()
            .configurationSource(corsConfigurationSource());

        http
            .csrf((auth) -> auth.disable());

        //form 로그인 방식
        http
            .formLogin((auth) -> auth.disable());

        //http basic 인증 방식
        http
            .httpBasic((auth) -> auth.disable());

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(
                    "/", "/auth/**", "/login", "/join", "/chatbot/**",
                    "/oauth2/**", "/oauth/**", "/oauth/**", "/login/oauth2/code/**",
                    "/auth/find-login-id", "/auth/password-reset/**", "/auth/password-reset/confirm",
                    "/auth/password-reset/request", "/auth/find-login-id", "/my/check-nickname",
                    "/reviews/good", "/users/count", "/courses/beginner","/courses/list",
                    "categories", "/courses/{id}", "/courses/{id}/reviews", "/courses/{id}/qnas"
                ).permitAll()
                .requestMatchers(
                    "/logout", "/my/**", "/auth/verify-password", "/auth/change-password",
                    "/notifications/**", "/courses/{id}/schedules", "/payments/**", "/coupons/available"
                ).hasAuthority("USER")
                .requestMatchers("/adjustments/**", "/hosts/**").hasAuthority("HOST")
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            );

        http
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOidcUserService)
                    .userService(customOAuth2UserService))

            );

        LoginFilter loginFilter = new LoginFilter(
            authenticationManager(authenticationConfiguration),
            jwtUtil,
            customLoginSuccessHandler
        );
        loginFilter.setFilterProcessesUrl("/login");

        http.addFilterBefore(new JWTFilter(jwtUtil, userRepository),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http
            .exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter()
                        .write("{\"error\": \"Unauthorized\", \"message\": \"로그인이 필요합니다.\"}");
                })
                .accessDeniedHandler(accessDeniedHandler)
            );

        http
            .logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .deleteCookies("refresh", "JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll()
            );
        return http.build();
    }
}
