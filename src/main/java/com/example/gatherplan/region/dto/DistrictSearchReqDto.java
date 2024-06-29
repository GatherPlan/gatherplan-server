package com.example.gatherplan.region.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DistrictSearchReqDto {
    private String keyword;
    private int page;
    private int size;
}
