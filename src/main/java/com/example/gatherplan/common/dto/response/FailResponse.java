package com.example.gatherplan.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FailResponse {

    private String message;
    private int code;

    public static FailResponse of(String message, int code) {
        return FailResponse.builder()
                .message(message)
                .code(code)
                .build();
    }
}
