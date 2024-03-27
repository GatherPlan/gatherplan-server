package com.example.gatherplan.appointment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public enum TimeType {

    MORNING(LocalTime.of(6, 0), LocalTime.of(12, 0), "오전"),
    AFTERNOON(LocalTime.of(12, 0), LocalTime.of(18, 0), "오후"),
    EVENING(LocalTime.of(18, 0), LocalTime.of(23, 59), "저녁");

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String description;

}
