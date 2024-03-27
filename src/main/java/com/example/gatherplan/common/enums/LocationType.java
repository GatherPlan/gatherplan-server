package com.example.gatherplan.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocationType {
    DETAIL_ADDRESS("상세 주소"),
    DISTRICT("행정 구역");

    private final String description;
}
