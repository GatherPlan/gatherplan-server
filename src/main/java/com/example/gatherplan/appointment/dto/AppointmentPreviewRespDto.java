package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentPreviewRespDto {

    private String appointmentName;

    private String hostName;

    private String appointmentCode;

    private Address address;

    private String notice;

    private List<LocalDate> candidateDateList;

    private AppointmentState appointmentState;
}
