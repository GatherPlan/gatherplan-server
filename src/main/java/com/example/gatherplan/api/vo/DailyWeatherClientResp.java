package com.example.gatherplan.api.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherClientResp {
    private List<DailyWeatherInfo> daily;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyWeatherInfo {
        @JsonProperty("wx_text")
        private String weatherState;
        @JsonProperty("mon")
        private String month;
        private String day;
        @JsonProperty("tmin")
        private String minTemporary;
        @JsonProperty("tmax")
        private String maxTemporary;
    }
}
