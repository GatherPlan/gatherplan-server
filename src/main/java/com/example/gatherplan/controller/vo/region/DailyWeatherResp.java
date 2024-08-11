package com.example.gatherplan.controller.vo.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 장소 날씨 검색 응답")
public class DailyWeatherResp {

    @Schema(description = "날씨 이미지 경로",
            example = "https://www.kr-weathernews.com/mv4/html/assets/images/weather-icon-set/icon1/dark/night/302.svg")
    private String weatherImagePath;

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
