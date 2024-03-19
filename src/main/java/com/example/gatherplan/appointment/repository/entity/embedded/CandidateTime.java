package com.example.gatherplan.appointment.repository.entity.embedded;

import com.example.gatherplan.appointment.enums.AppointmentCandidateTimeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalTime;


@Getter
@Embeddable
public class CandidateTime {
    @Enumerated(EnumType.STRING)
    private AppointmentCandidateTimeType appointmentCandidateTimeType;
    private LocalTime startTime;
    private LocalTime endTime;
}
