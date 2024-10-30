package com.example.gatherplan.controller.exception;

import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResp> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, @AuthenticationPrincipal UserInfo userInfo, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.PARAMETER_VALIDATION_FAIL;

        log.warn("MethodArgumentNotValidException occurred: {}", getDetailLog(exception, userInfo, request), exception);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(errorCode, exception.getFieldErrors()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResp> handleConstraintViolationExceptionException(
            ConstraintViolationException exception, @AuthenticationPrincipal UserInfo userInfo, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.PARAMETER_VALIDATION_FAIL;

        log.warn("ConstraintViolationException occurred: {}", getDetailLog(exception, userInfo, request), exception);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(errorCode, exception));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResp> handleBusinessException(
            BusinessException exception, @AuthenticationPrincipal UserInfo userInfo, HttpServletRequest request) {
        ErrorCode errorCode = exception.getErrorCode();

        log.warn("BusinessException occurred: {}", getDetailLog(exception, userInfo, request), exception);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(errorCode));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResp> defaultException(
            RuntimeException exception, @AuthenticationPrincipal UserInfo userInfo, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.SEVER_NOT_SUPPORT;

        log.warn("RuntimeException (Unexpected Exception) occurred: {}", getDetailLog(exception, userInfo, request), exception);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResp.of(errorCode, exception.getMessage()));
    }

    private String getDetailLog(Exception e, UserInfo userInfo, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(request)) {
            builder.append(" [REQUEST URI]: %s %s".formatted(request.getMethod(), request.getRequestURI()));
            if (StringUtils.isNotBlank(request.getQueryString())) {
                builder.append(" [QUERY STRING]: %s".formatted(URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8)));
            }
            try {
                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                if (StringUtils.isNotBlank(body)) {
                    builder.append(" [BODY]: %s".formatted(body));
                }
            } catch (Exception exception) {
                log.warn("Exception occurred while extract request body", exception);
            }
        }
        if (Objects.nonNull(userInfo)) {
            builder.append(" [USER ID] : %s, [USER EMAIL] : %s".formatted(userInfo.getId(), userInfo.getEmail()));
            if (Objects.nonNull(request) && StringUtils.isNotBlank(request.getHeader("Authorization"))) {
                builder.append(" [USER TOKEN]: %s".formatted(request.getHeader("Authorization")));
            }
        }
        if (e instanceof BusinessException businessException) {
            builder.append(" [BUSINESS LOGIC ERROR MESSAGE] : %s".formatted(businessException.getMessage()));
        } else {
            builder.append(" [DEFAULT ERROR MESSAGE] : %s".formatted(e.getMessage()));
        }
        return builder.toString();
    }
}
