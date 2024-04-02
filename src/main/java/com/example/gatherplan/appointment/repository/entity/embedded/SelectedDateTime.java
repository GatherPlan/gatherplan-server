package com.example.gatherplan.appointment.repository.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SelectedDateTime {
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;
    private LocalDate selectedDate;
}
