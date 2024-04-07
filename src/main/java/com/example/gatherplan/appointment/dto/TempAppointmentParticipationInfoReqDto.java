package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentParticipationInfoReqDto {

    private String appointmentCode;

    private TempAppointmentParticipationInfoReqDto.TempUserInfo tempUserInfo;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TempUserInfo {
        private String nickname;

        private String password;
    }
}
