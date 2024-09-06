package com.example.gatherplan.common.config.jwt;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.common.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("토큰이 없습니다.");
            request.setAttribute("exceptionType", ErrorCode.JWT_TOKEN_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("토큰을 가지고 있습니다.");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        jwtUtil.validateTokenExpired(token, request);

        RoleType roleType = RoleType.byRole(jwtUtil.getRole(token));
        UserAuthType userAuthType = UserAuthType.byUserAuthType(jwtUtil.getUserAuthType(token));

        User user = User.builder()
                .id(jwtUtil.getId(token))
                .name(jwtUtil.getNickName(token))
                .email(jwtUtil.getEmail(token))
                .userAuthType(userAuthType)
                .roleType(roleType)
                .build();

        UserInfo userInfo = new UserInfo(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}