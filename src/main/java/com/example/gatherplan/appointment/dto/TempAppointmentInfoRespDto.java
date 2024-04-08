package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentInfoRespDto {

    private String appointmentName;

    private String hostName;

    private AppointmentState appointmentState;

    private String appointmentCode;

    private Address address;

    private ConfirmedDateTime confirmedDateTime;
}
