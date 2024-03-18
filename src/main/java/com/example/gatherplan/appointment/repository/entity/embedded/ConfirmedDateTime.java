package com.example.gatherplan.appointment.repository.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ConfirmedDateTime {
    private LocalTime confirmedStartTime;
    private LocalTime confirmedEndTime;
    private LocalDate confirmedDate;
}
