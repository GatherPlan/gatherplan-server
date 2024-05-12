package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.Address;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentInfoRespDto {

    private String appointmentName;

    private String hostName;

    private String appointmentCode;

    private Address address;

    private String notice;
}
