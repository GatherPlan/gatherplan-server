package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateAppointmentReqDto {
    private String appointmentCode;
    private String appointmentName;
    private String notice;
    private Address address;
    private List<LocalDate> candidateDateList;
}
