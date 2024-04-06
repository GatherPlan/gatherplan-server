package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentWithHostRespDto {
    private String hostName;
    private String appointmentCode;
    private String appointmentName;
    private AppointmentState appointmentState;


}
