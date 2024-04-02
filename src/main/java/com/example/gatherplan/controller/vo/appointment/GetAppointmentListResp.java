package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.AppointmentState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAppointmentListResp {
    private String appointmentName;
    private String hostName;
    private AppointmentState appointmentState;
    private String appointmentCode;
}
