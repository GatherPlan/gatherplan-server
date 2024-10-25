package com.example.gatherplan.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LocationType {
    DETAIL_ADDRESS("상세 주소"),
    DISTRICT("행정 구역"),
    CUSTOM_ADDRESS("직접 입력 주소");

    private final String description;
}
