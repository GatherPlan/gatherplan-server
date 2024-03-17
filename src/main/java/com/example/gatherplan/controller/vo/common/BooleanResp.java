package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "요청 성공 여부")
public class BooleanResp {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    public static BooleanResp of(boolean isSuccess) {
        return BooleanResp.builder()
                .success(isSuccess)
                .build();
    }
}
