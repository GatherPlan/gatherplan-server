package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.ParticipationInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ConfirmedAppointmentParticipantsReqDto {

    private String appointmentCode;

    private ConfirmedDateTime confirmedDateTime;

    private List<ParticipationInfo> participationInfoList;
}
