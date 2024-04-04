package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAppointmentInfoDto {
    private String hostName;
    private String appointmentCode;
    private String appointmentName;
    private AppointmentState appointmentState;

}
