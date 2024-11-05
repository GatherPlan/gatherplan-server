package com.example.gatherplan.common.config.jwt.exception;

import com.example.gatherplan.common.exception.ErrorCode;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


@Getter
public class JwtTokenException extends AuthenticationException {

    private final ErrorCode errorCode;

    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
