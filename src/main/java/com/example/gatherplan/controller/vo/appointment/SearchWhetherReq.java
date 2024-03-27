package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "날씨 검색 요청 객체")
public class SearchWhetherReq {
    @NotBlank
    @Schema(description = "날씨 검색할 주소", example = "서울특별시 성동구")
    private String addressName;
}
