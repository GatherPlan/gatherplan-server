package com.example.gatherplan.appointment.dto;


import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


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

    private AppointmentState appointmentState;

    private ConfirmedDateTime confirmedDateTime;

    private List<LocalDate> candidateDateList;

    private boolean isParticipated;

    private boolean isHost;

    private List<UserParticipationInfo> userParticipationInfoList;
}
