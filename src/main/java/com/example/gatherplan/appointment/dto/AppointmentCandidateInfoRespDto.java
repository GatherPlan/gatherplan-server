package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.UserAuthType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentCandidateInfoRespDto {
    private LocalDate candidateDate;
    private LocalTime startDateTime;
    private LocalTime endDateTime;
    private List<ParticipationUserInfo> participants;

    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ParticipationUserInfo {
        private String nickname;
        private UserAuthType userAuthType;
    }
}
