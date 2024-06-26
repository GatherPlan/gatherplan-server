package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "데이터 리스트 응답 객체")
public class ListResponse<T> {

    @Schema(description = "메타데이터")
    private MetaData metaData;

    @Schema(description = "데이터")
    private List<T> data;

    public static <T> ListResponse<T> of(List<T> data) {
        MetaData metaData = MetaData.builder()
                .totalCount(data.size())
                .build();

        return ListResponse.<T>builder()
                .metaData(metaData)
                .data(data)
                .build();
    }

    public static <T> ListResponse<T> of(List<T> data, MetaData metaData) {
        return ListResponse.<T>builder()
                .data(data)
                .metaData(metaData)
                .build();
    }

    public static <T> ListResponse<T> of(Page<T> page) {
        return of(page.getContent(), MetaData.of(page));
    }

}