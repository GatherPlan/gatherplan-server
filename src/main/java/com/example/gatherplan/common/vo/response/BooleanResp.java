package com.example.gatherplan.common.vo.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BooleanResp {

    private boolean success;

    public static BooleanResp of(boolean isSuccess) {
        return BooleanResp.builder()
                .success(isSuccess)
                .build();
    }
}
