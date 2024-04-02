package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAppointmentListRespDto {

    private String appointmentName;
    private String hostName;
    private AppointmentState appointmentState;
    private String appointmentCode;

}
