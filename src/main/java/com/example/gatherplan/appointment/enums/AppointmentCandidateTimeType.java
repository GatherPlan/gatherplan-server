package com.example.gatherplan.appointment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentCandidateTimeType {

    MORNING("오전"),
    AFTERNOON("오후"),
    EVENING("저녁"),
    CUSTOM("사용자 설정");

    private final String description;

}
