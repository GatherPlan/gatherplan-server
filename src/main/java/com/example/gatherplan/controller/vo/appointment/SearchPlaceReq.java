package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 장소 상세주소 검색 요청 객체")
public class SearchPlaceReq {
    @NotBlank
    @Schema(description = "약속 장소 검색 키워드", example = "뱃놈")
    private String keyword;

    @Min(1)
    @Schema(description = "약속 장소 검색 페이지 수", example = "1")
    private int page;

    @Min(1)
    @Schema(description = "약속 장소 검색 페이지 당 데이터 수", example = "15")
    private int size;
}
