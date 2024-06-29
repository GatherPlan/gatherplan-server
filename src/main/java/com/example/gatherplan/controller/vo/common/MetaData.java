package com.example.gatherplan.controller.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(name = "메타데이터")
public class MetaData {

    @Schema(description = "전체 데이터 개수", example = "30")
    private long totalCount;
    @Schema(description = "전체 페이지 수.", example = "3")
    private long totalPageCount;
    @Schema(description = "페이지 당 데이터 수", example = "10")
    private long pageSize;
    @Schema(description = "마지막 ID (스크롤링 전용 필드)")
    private String lastId;
    @Schema(description = "다음 데이터 존재 여부")
    private boolean hasNext;

    public static MetaData of(Page<?> page) {
        return MetaData.builder()
                .totalCount(page.getTotalElements())
                .totalPageCount(page.getTotalPages())
                .pageSize(page.getSize())
                .hasNext(page.hasNext())
                .build();
    }
}


