package com.example.gatherplan.controller.exception;

import com.example.gatherplan.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResp> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.PARAMETER_VALIDATION_FAIL;

        BindingResult bindingResult = exception.getBindingResult();

        String exceptionMessage =
                Optional.ofNullable(bindingResult.getFieldError())
                        .orElseGet(() -> {
                            log.error("Get Binding Result Error - Field Error is null.");
                            throw new NullPointerException("Get Binding Result Error - Field Error is null.");
                        })
                        .getDefaultMessage();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(errorCode.getCode(), exceptionMessage));
    }

    @ExceptionHandler({BusinessException.class})
    protected ResponseEntity<ErrorResp> handleBusinessException(BusinessException exception){
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(
                        errorCode.getCode(), exception.getMessage()));
    }
}
