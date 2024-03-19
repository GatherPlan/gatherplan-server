package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;


@Schema(name = "메타데이터")
@Getter
@Builder
public class MetaData {

    @Schema(description = "총 데이터 개수", example = "10")
    private int totalCount;

    public MetaData(int totalCount) {
        this.totalCount = totalCount;
    }
}
