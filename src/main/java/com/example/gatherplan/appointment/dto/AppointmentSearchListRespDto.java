package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AppointmentSearchListRespDto {

    private String appointmentCode;

    private String appointmentName;

    private AppointmentState appointmentState;

    private String notice;

    private String hostName;

    private boolean isHost;
}
