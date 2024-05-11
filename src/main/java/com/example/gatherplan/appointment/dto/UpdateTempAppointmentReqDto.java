package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTempAppointmentReqDto {

    private String appointmentCode;

    private String appointmentName;

    private String notice;

    private Address address;

    private List<LocalDate> candidateDateList;

    private TempUserInfo tempUserInfo;

}
