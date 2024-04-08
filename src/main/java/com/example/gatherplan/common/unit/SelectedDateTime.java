package com.example.gatherplan.common.unit;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Embeddable
public class SelectedDateTime {
    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private LocalTime selectedEndTime;
}
