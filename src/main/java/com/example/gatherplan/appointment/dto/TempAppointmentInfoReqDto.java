package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentInfoReqDto {

    private String nickname;

    private String password;

    private String appointmentCode;
}
