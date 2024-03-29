package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 장소 행정구역 검색 요청 객체")
public class RegionReq {

    @NotBlank(message = "키워드는 공백일 수 없습니다")
    @Schema(description = "약속 장소 검색 키워드", example = "성수동")
    private String keyword;
}
