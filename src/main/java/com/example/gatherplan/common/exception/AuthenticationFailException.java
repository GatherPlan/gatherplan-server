package com.example.gatherplan.common.exception;

public class AuthenticationFailException extends BusinessException{
    public AuthenticationFailException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthenticationFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
