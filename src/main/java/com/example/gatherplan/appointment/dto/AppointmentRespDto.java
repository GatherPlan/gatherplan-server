package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AppointmentRespDto {
    private String appointmentName;
    private Address address;
    private String hostName;
    private String notice;
    private List<LocalDate> candidateDateList;
}
