package com.example.gatherplan.region.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyWeatherRespDto {
    private String weatherImagePath;
    private String month;
    private String day;
    private String weatherState;
    private String minTemporary;
    private String maxTemporary;
}
