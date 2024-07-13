package com.example.gatherplan.common.config.jwt;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Getter
@Setter
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public final AuthenticationManager authenticationManager;
    public final JWTUtil jwtUtil;
    public final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (request.getContentType() == null || !request.getContentType().equals("application/json")) {
            throw new UserException(ErrorCode.PARAMETER_VALIDATION_FAIL, "입력된 데이터가 JSON 형식이 아닙니다.");
        }

        LoginReq loginReq;

        try {
            loginReq = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), LoginReq.class);
        } catch (IOException e) {
            throw new UserException(ErrorCode.PARAMETER_VALIDATION_FAIL, "요청된 입력을 파싱하는데 실패했습니다.");
        }

        String email = loginReq.getEmail();
        String password = loginReq.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();

        Long id = userInfo.getId();
        String name = userInfo.getUsername();
        String email = userInfo.getEmail();
        UserAuthType userAuthType = userInfo.getUserAuthType();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String token = jwtUtil.createJwt(id, name, email, userAuthType, role, 23 * 60 * 60 * 1000L);

        response.addHeader("Authorization", "Bearer " + token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("name", userInfo.getUsername());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
