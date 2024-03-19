package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


@Schema(description = "성공 여부 응답 객체")
@Getter
@Builder
public class BooleanResp {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    public static BooleanResp of(boolean isSuccess) {
        return BooleanResp.builder()
                .success(isSuccess)
                .build();
    }
}
