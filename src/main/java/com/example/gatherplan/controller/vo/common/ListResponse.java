package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "응답 데이터 리스트")
public class ListResponse<T> {

    @Schema(description = "메타데이터")
    private MetaData metaData;

    @Schema(description = "데이터")
    private T data;

    public ListResponse(MetaData metaData, T data) {
        this.metaData = metaData;
        this.data = data;
    }

    public static <T> ListResponse<T> of(MetaData metaData, T data) {
        return ListResponse.<T>builder()
                .metaData(metaData)
                .data(data)
                .build();
    }

}