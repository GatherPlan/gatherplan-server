package com.example.gatherplan.common.config.jwt.filter;

import com.example.gatherplan.common.config.jwt.exception.JwtTokenException;
import com.example.gatherplan.common.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtTokenException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            sendErrorResponse(response, errorCode);
        } catch (AuthenticationException ex) {
            sendErrorResponse(response, ErrorCode.AUTHENTICATION_FAIL);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format("""
            {
                "code": "%s",
                "message": "%s"
            }
            """, errorCode.getCode(), errorCode.getMessage());

        response.getWriter().write(jsonResponse);
    }
}
