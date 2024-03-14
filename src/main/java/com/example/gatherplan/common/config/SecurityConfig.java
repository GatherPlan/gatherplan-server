package com.example.gatherplan.common.config;

import com.example.gatherplan.common.jwt.JWTFilter;
import com.example.gatherplan.common.jwt.JWTUtil;
import com.example.gatherplan.common.jwt.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

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
                .csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                .formLogin(AbstractHttpConfigurer::disable);

        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable);

        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/members/**").permitAll()
                        .requestMatchers("/api/v1/appointments/**").hasRole("ADMIN")
                        .anyRequest().authenticated());

        httpSecurity
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, objectMapper);
        loginFilter.setFilterProcessesUrl("/api/v1/members/login");

        httpSecurity
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

}
