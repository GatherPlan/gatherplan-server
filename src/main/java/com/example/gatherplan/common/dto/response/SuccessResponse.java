package com.example.gatherplan.common.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse {

    private boolean success;

    public static SuccessResponse of() {
        return SuccessResponse.builder()
                .success(true)
                .build();
    }
}
