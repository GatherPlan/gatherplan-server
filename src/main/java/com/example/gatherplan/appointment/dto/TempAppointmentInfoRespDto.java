package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempAppointmentInfoRespDto {

    private String appointmentName;

    private String hostName;

    private AppointmentState appointmentState;

    private String appointmentCode;

    private Address address;

    private ConfirmedDateTime confirmedDateTime;
}
