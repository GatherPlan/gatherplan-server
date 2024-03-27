package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 장소 날씨 검색 응답 객체")
public class SearchWeatherResp {
    @Schema(description = "월", example = "3")
    private String month;

    @Schema(description = "일", example = "27")
    private String day;

    @Schema(description = "날씨 상태", example = "비온 뒤 맑음")
    private String weatherState;

    @Schema(description = "하루 중 최고 기온", example = "27")
    private String minTemporary;

    @Schema(description = "하루 중 최저 기온", example = "12")
    private String maxTemporary;
}
