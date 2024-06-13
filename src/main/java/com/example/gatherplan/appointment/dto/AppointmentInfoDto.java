package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.repository.entity.Appointment;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AppointmentInfoDto {
    private Appointment appointment;

    private String hostName;

    private Boolean isParticipated;

    private Boolean isHost;
}