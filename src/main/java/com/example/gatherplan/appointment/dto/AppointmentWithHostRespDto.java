package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentWithHostRespDto {
    private String hostName;
    private String appointmentCode;
    private String appointmentName;
    private AppointmentState appointmentState;


}
