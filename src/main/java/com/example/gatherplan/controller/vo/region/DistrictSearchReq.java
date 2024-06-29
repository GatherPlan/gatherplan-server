package com.example.gatherplan.controller.vo.region;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 장소 행정구역 검색 요청 객체")
public class DistrictSearchReq {

    @NotBlank(message = "키워드는 공백이 될 수 없습니다.")
    @Size(min = 2, message = "키워드는 2자 이상이어야합니다.")
    @Schema(description = "검색 키워드", example = "성수")
    private String keyword;

    @Min(1)
    @Schema(description = "약속 장소 검색 페이지 수", example = "1", type = "int", requiredMode = REQUIRED)
    private int page;

    @Min(1)
    @Schema(description = "약속 장소 검색 페이지 당 데이터 수", example = "10", type = "int", requiredMode = REQUIRED)
    private int size;

}
