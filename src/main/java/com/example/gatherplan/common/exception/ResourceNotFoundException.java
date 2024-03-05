package com.example.gatherplan.common.exception;

public class ResourceNotFoundException  extends BusinessException{
    public ResourceNotFoundException(ExceptionInfo exceptionInfo, String message) {
        super(exceptionInfo, message);
    }
}
