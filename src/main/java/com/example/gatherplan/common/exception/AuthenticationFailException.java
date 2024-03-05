package com.example.gatherplan.common.exception;

public class AuthenticationFailException extends BusinessException{
    public AuthenticationFailException(ExceptionInfo exceptionInfo, String message) {
        super(exceptionInfo, message);
    }
}
