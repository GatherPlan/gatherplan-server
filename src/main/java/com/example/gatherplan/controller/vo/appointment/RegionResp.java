package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "약속 장소 행정구역 검색 응답 객체")
public class RegionResp {
    @Schema(description = "행정구역 주소", example = "서울 성동구 용답동")
    private String address;
}
