package com.example.gatherplan.controller.exception;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResp {
    private int code;
    private String message;

    public static ErrorResp of(int code, String message) {
        return ErrorResp.builder()
                .code(code)
                .message(message)
                .build();
    }
}
