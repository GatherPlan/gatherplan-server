package com.example.gatherplan.api.kakaolocal;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoLocationClientResp {
    private List<KakaoLocationClientRespDocument> documents;
    private KakaoLocationClientRespMeta meta;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoLocationClientRespDocument {
        private String placeName;
        private String addressName;
        private String placeUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoLocationClientRespMeta {
        private String totalCount;
        private String pageableCount;
    }
}