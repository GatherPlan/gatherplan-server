package com.example.gatherplan.appointment.repository.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Embeddable
public class ConfirmedDateTime {
    private LocalTime confirmedStartTime;
    private LocalTime confirmedEndTime;
    private LocalDate confirmedDate;
}
