package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteTempAppointmentReqDto {

    private String appointmentCode;


    private DeleteTempAppointmentReqDto.TempUserInfo tempUserInfo;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TempUserInfo {
        private String nickname;

        private String password;
    }
}
