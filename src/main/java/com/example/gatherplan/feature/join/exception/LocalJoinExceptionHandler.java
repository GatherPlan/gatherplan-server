package com.example.gatherplan.feature.join.exception;

import com.example.gatherplan.common.dto.response.FailResponse;
import com.example.gatherplan.common.exception.AuthenticationFailException;
import com.example.gatherplan.common.exception.ExceptionInfo;
import com.example.gatherplan.common.exception.ResourceConflictException;
import com.example.gatherplan.common.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class LocalJoinExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<FailResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ExceptionInfo exceptionInfo = ExceptionInfo.PARAMETER_VALIDATION_FAIL;

        BindingResult bindingResult = exception.getBindingResult();
        Optional<FieldError> fieldError = Optional.ofNullable(bindingResult.getFieldError());

        if (fieldError.isEmpty()){
            throw new RuntimeException();
        }

        String exceptionMessage = fieldError.get().getDefaultMessage();

        return ResponseEntity
                .status(exceptionInfo.getHttpStatus())
                .body(FailResponse.of(exceptionMessage, exceptionInfo.getCode()));
    }

    @ExceptionHandler({ResourceConflictException.class})
    protected ResponseEntity<FailResponse> handleResourceConflictException(ResourceConflictException exception){
        ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        String exceptionMessage = exception.getCustomMessage();

        return ResponseEntity
                .status(exceptionInfo.getHttpStatus())
                .body(FailResponse.of(exceptionMessage,exceptionInfo.getCode()));
    }

    @ExceptionHandler({AuthenticationFailException.class})
    protected ResponseEntity<FailResponse> handleAuthenticationFailException(AuthenticationFailException exception){
        ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        String exceptionMessage = exception.getCustomMessage();

        return ResponseEntity
                .status(exceptionInfo.getHttpStatus())
                .body(FailResponse.of(exceptionMessage,exceptionInfo.getCode()));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<FailResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        ExceptionInfo exceptionInfo = exception.getExceptionInfo();
        String exceptionMessage = exception.getCustomMessage();

        return ResponseEntity
                .status(exceptionInfo.getHttpStatus())
                .body(FailResponse.of(exceptionMessage,exceptionInfo.getCode()));
    }
}
