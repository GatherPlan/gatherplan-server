package com.example.gatherplan.appointment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentState {
    CONFIRMED("확정"),
    UNCONFIRMED("미확정");

    private final String description;

    public String getName() {
        return this.name();
    }
}
