package com.example.gatherplan.controller.exception;

import com.example.gatherplan.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolationException;
import lombok.*;
import org.springframework.validation.FieldError;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(NON_NULL)
public class ErrorResp {
    private int code;
    private String message;
    private List<ErrorDetail> errors;

    public static ErrorResp of(ErrorCode errorCode) {
        return ErrorResp.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResp of(ErrorCode errorCode, String customMessage) {
        return ErrorResp.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(List.of(
                        ErrorDetail.builder()
                                .reason(customMessage)
                                .build()
                ))
                .build();
    }

    public static ErrorResp of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        List<ErrorDetail> errorDetails = fieldErrors.stream()
                .map(f -> ErrorDetail.builder()
                        .object(f.getObjectName())
                        .field(f.getField())
                        .validation(f.getCode())
                        .reason(f.getDefaultMessage())
                        .build()
                )
                .toList();

        return ErrorResp.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errorDetails)
                .build();
    }

    public static ErrorResp of(ErrorCode errorCode, ConstraintViolationException exception) {
        List<ErrorDetail> errorDetails = exception.getConstraintViolations().stream()
                .map(v -> ErrorDetail.builder()
                        .object(v.getRootBeanClass().getSimpleName())
                        .field(v.getPropertyPath().toString())
                        .reason(v.getMessage())
                        .build())
                .toList();

        return ErrorResp.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errorDetails)
                .build();
    }

    @JsonInclude(NON_NULL)
    @Builder
    public record ErrorDetail(
            String object,
            String field,
            String validation,
            String reason) {
    }
}
