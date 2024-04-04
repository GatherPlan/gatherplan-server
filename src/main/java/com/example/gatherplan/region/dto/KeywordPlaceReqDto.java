package com.example.gatherplan.region.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordPlaceReqDto {
    private String keyword;
    private int page;
    private int size;

}