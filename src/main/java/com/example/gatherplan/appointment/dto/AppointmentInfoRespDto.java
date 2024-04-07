package com.example.gatherplan.appointment.dto;


import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentInfoRespDto {
    private Address address;
    private ConfirmedDateTime confirmedDateTime;
    private String appointmentName;
    private String hostName;
    private AppointmentState appointmentState;
    private String appointmentCode;

}
