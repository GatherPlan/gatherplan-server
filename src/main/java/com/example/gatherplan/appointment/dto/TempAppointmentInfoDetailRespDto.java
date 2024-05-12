package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentInfoDetailRespDto {

    private String appointmentName;

    private String hostName;

    private String appointmentCode;

    private Address address;

    private String notice;

    private AppointmentState appointmentState;

    private ConfirmedDateTime confirmedDateTime;
}
