package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.SelectedDateTime;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTempAppointmentParticipationReqDto {
    String appointmentCode;
    List<SelectedDateTime> selectedDateTimeList;
    private TempUserInfo tempUserInfo;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TempUserInfo {
        private String nickname;
        private String password;
    }
}
