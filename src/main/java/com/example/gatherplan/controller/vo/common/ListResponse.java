package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "데이터 리스트 응답 객체")
public class ListResponse<T> {

    @Schema(description = "메타데이터")
    private MetaData metaData;

    @Schema(description = "데이터")
    private T data;

    public static <T> ListResponse<T> of(MetaData metaData, T data) {
        return ListResponse.<T>builder()
                .metaData(metaData)
                .data(data)
                .build();
    }

}