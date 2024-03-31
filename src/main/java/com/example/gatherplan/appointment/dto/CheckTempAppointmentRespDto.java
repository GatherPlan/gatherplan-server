package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.ConfirmedDateTime;
import com.example.gatherplan.common.unit.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckTempAppointmentRespDto {

    private String appointmentName;

    private Address address;

    private String hostName;

    private String appointmentCode;

    private AppointmentState appointmentState;

    private List<TimeType> candidateTimeTypeList;

    private List<LocalDate> candidateDateList;

    private ConfirmedDateTime confirmedDateTime;
}
