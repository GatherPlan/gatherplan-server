package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "성공 여부 응답 객체")
public class BooleanResp {

    @Schema(description = "성공 여부", example = "true")
    private boolean isSuccess;

    public static BooleanResp success() {
        return BooleanResp.builder()
                .isSuccess(true)
                .build();
    }

    public static BooleanResp of(boolean isSuccess) {
        return BooleanResp.builder()
                .isSuccess(isSuccess)
                .build();
    }

    public boolean getIsSuccess(){
        return this.isSuccess;
    }

}
