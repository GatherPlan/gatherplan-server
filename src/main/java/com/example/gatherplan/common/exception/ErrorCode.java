package com.example.gatherplan.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // COMMON ERROR
    PARAMETER_VALIDATION_FAIL(HttpStatus.BAD_REQUEST, 2201, "조건에 맞지 않은 값이 존재합니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 2202, "조건에 맞는 결과가 존재하지 않습니다."),
    RESOURCE_CONFLICT(HttpStatus.CONFLICT, 2203, "이미 존재하는 값입니다."),
    RESOURCE_ACCESS_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, 2204, "접근 권한이 없습니다."),
    AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, 2205, "인증에 실패했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 2206, "서비스가 일시적으로 중단되었습니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 2207, "토큰이 만료되었습니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, 2208, "토큰이 존재하지 않습니다."),
    SEVER_NOT_SUPPORT(HttpStatus.INTERNAL_SERVER_ERROR, 9999, "알 수 없는 예외입니다."),

    // USER ERROR
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 3000, "존재하지 않은 사용자입니다."),
    USER_NOT_HOST(HttpStatus.NOT_ACCEPTABLE, 3003, "해당 약속의 호스트가 아닙니다."),
    TEMP_USER_CONFLICT(HttpStatus.CONFLICT, 3004, "이미 존재하는 임시회원입니다."),
    USER_NOT_GUEST(HttpStatus.NOT_ACCEPTABLE, 3003, "해당 약속의 게스트가 아닙니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, 3004, "이미 사용 중인 이메일입니다."),

    // APPOINTMENT ERROR (WITH USER)
    USER_NOT_JOINED_APPOINTMENT(HttpStatus.NOT_FOUND, 4000, "참여하지 않은 약속입니다."),
    USER_ALREADY_JOINED_APPOINTMENT(HttpStatus.BAD_REQUEST, 4001, "이미 참여한 약속입니다."),
    APPOINTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 4002, "존재하지 않은 약속입니다."),
    HOST_NOT_FOUND_IN_APPOINTMENT(HttpStatus.NOT_FOUND, 4003, "약속의 호스트를 찾을 수 없습니다."),
    APPOINTMENT_ALREADY_CONFIRMED(HttpStatus.CONFLICT, 4004, "이미 확정된 약속입니다. 참여 및 변경이 불가능합니다."),
    USER_NOT_RELATED_TO_APPOINTMENT(HttpStatus.NOT_FOUND, 4005, "해당 약속에 접근이 불가능한 사용자입니다."),
    USER_NAME_DUPLICATED_IN_APPOINTMENT(HttpStatus.CONFLICT, 4006, "해당 약속의 기존 회원과 이름이 중복됩니다."),

    // REGION ERROR
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, 5000, "존재하지 않는 지역입니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}