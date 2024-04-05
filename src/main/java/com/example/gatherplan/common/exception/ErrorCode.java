package com.example.gatherplan.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PARAMETER_VALIDATION_FAIL(HttpStatus.BAD_REQUEST, 2201, "파라미터 값이 조건에 맞지 않습니다."),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 2202, "리소스를 찾을 수 없습니다."),
    RESOURCE_CONFLICT(HttpStatus.CONFLICT, 2203, "이미 존재하는 리소스입니다."),
    RESOURCE_ACCESS_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, 2204, "접근 권한이 없습니다."),
    AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, 2205, "인증에 실패했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 2206, "서비스가 일시적으로 중단되었습니다."),
    SEVER_NOT_SUPPORT(HttpStatus.INTERNAL_SERVER_ERROR, 9999, "알 수 없는 예외입니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 3000, "회원을 찾을 수 없습니다."),
    USER_NOT_HOST(HttpStatus.NOT_ACCEPTABLE, 3003, "해당 유저는 호스트가 아닙니다."),
    USER_NOT_PARTICIPATE_APPOINTMENT(HttpStatus.NOT_FOUND, 5001, "참여하지 않은 약속입니다."),

    NOT_FOUND_APPOINTMENT(HttpStatus.NOT_FOUND, 4002, "약속을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}