package com.example.gatherplan.region.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordPlaceRespDto {
    private String addressName;
    private String placeUrl;
    private String placeName;
}
