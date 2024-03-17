package com.example.gatherplan.appointment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CandidateTimeType {
    CUSTOM("직접 입력"),
    SECTION("선택 입력");

    private final String description;
}
