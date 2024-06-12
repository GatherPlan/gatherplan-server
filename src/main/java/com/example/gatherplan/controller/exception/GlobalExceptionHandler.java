package com.example.gatherplan.controller.exception;

import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResp> handleConstraintViolationExceptionException(ConstraintViolationException exception) {
        ErrorCode errorCode = ErrorCode.PARAMETER_VALIDATION_FAIL;

        String exceptionMessage = exception.getMessage();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(errorCode.getCode(), exceptionMessage));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResp> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(
                        errorCode.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResp> defaultException(RuntimeException exception) {
        ErrorCode errorCode = ErrorCode.SEVER_NOT_SUPPORT;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(
                        errorCode.getCode(), exception.getMessage()));
    }
}
