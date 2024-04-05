package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentParticipationInfoReqDto {
    private String nickname;

    private String password;

    private String appointmentCode;
}
