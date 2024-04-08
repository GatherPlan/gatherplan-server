package com.example.gatherplan.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentWithHostDto {
    private String hostName;
    private Long appointmentId;
}
