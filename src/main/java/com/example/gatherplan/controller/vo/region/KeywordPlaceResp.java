package com.example.gatherplan.controller.vo.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 장소 상세주소 검색 응답 객체")
public class KeywordPlaceResp {
    @Schema(description = "행정구역 주소", example = "서울 광진구 군자동 98")
    private String addressName;

    @Schema(description = "주소 링크", example = "http://place.map.kakao.com/7949668")
    private String placeUrl;

    @Schema(description = "장소 이름", example = "세종대학교")
    private String placeName;
}
