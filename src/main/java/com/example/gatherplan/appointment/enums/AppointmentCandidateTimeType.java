package com.example.gatherplan.appointment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public enum AppointmentCandidateTimeType {

    MORNING("오전", LocalTime.of(6,0), LocalTime.of(12,0)),
    AFTERNOON("오후", LocalTime.of(12,0), LocalTime.of(18,0)),
    EVENING("저녁", LocalTime.of(18,0), LocalTime.of(0,0)),
    CUSTOM("사용자 설정", null, null);

    private final String description;
    private final LocalTime startTime;
    private final LocalTime endTime;
}
