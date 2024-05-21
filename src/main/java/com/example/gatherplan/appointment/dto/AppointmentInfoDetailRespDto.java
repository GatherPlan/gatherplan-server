package com.example.gatherplan.appointment.dto;


import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentInfoDetailRespDto {
    private Address address;
    private ConfirmedDateTime confirmedDateTime;
    private String appointmentName;
    private String hostName;
    private String notice;
    private AppointmentState appointmentState;
    private String appointmentCode;
    private List<LocalDate> candidateDateList;
    private boolean isParticipated;
    private boolean isHost;
}
