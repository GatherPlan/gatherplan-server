package com.example.gatherplan.api.whethernews;

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
public class WhetherNewsClientResp {
    private List<WhetherNewsClientRespDocument> daily;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WhetherNewsClientRespDocument {
        @JsonProperty("wx_text")
        private String whetherState;
        @JsonProperty("mon")
        private String month;
        private String day;
        @JsonProperty("tmin")
        private String minTemporary;
        @JsonProperty("tmax")
        private String maxTemporary;
    }
}
