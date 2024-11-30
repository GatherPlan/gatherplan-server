package com.example.gatherplan.common.config;

import com.example.gatherplan.common.config.jwt.filter.JWTFilter;
import com.example.gatherplan.common.config.jwt.JWTUtil;
import com.example.gatherplan.common.config.jwt.filter.LoginFilter;
import com.example.gatherplan.common.config.jwt.filter.JwtExceptionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private static final String[] authExcludePath = {
            "/api/v1/users/password:reset",
            "/api/v1/users/password:auth",
            "/api/v1/users/login",
            "/api/v1/users/auth",
            "/api/v1/users/join",
            "/api/v1/temporary/**",
            "/api/v1/region/**",
            "/api/v1/appointments/preview",
            "/api/v1/users/oauth/authorize",
            "/api/v1/users/oauth/authorize/redirect",
            "/api/v1/users/oauth/token",
            "/api/v1/users/oauth/check",
            "/api/v1/users/oauth/join",
            "/api/v1/users/oauth/login",
            "/", "/swagger-ui/**", "/v3/api-docs/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authExcludePath).permitAll()
                        .anyRequest().authenticated());

        httpSecurity
                .addFilterBefore(jwtExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    private Filter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }

    private Filter jwtFilter() {
        return new JWTFilter(jwtUtil,  Arrays.asList(authExcludePath));
    }

    private Filter loginFilter() throws Exception {
        return new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, objectMapper);
    }
}
