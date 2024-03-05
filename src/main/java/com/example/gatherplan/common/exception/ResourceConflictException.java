package com.example.gatherplan.common.exception;

public class ResourceConflictException extends BusinessException{
    public ResourceConflictException(ExceptionInfo exceptionInfo, String message) {
        super(exceptionInfo, message);
    }
}
