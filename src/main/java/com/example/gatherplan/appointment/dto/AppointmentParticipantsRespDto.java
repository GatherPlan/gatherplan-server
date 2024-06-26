package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.ParticipationInfo;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentParticipantsRespDto {
    private ParticipationInfo participationInfo;
}
