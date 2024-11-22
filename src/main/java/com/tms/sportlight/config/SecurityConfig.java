package com.tms.sportlight.config;

import com.tms.sportlight.security.filter.JWTFilter;
import com.tms.sportlight.security.filter.LoginFilter;
import com.tms.sportlight.repository.UserRepository;
import com.tms.sportlight.security.handler.CustomAccessDeniedHandler;
import com.tms.sportlight.security.handler.CustomAuthenticationFailureHandler;
import com.tms.sportlight.util.JWTUtil;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;


    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationFailureHandler authenticationFailureHandler, AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

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
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With", "Origin", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
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

        //경로별 인가작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login","/" ,"/join", "/api/auth/**").permitAll()
                .requestMatchers("/admin").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            );

        http
            .addFilterBefore(new JWTFilter(jwtUtil, userRepository), LoginFilter.class);

        http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
            .sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http
            .exceptionHandling((exceptions) -> exceptions
                .accessDeniedHandler(accessDeniedHandler));


        return http.build();
    }
}
