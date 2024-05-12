package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.Address;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentInfoRespDto {

    private String appointmentName;

    private String hostName;

    private String appointmentCode;

    private Address address;

    private String notice;
}
