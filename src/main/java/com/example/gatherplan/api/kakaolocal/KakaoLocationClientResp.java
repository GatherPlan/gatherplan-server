package com.example.gatherplan.api.kakaolocal;

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
    public static class KakaoLocationClientRespDocument {
        private String place_name;
        private String address_name;
        private String place_url;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KakaoLocationClientRespMeta {
        private String total_count;
        private String pageable_count;
    }
}