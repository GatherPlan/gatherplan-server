package com.example.gatherplan.appointment.utils;

import com.example.gatherplan.common.unit.UserParticipationInfo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentCandidateInfo {
    private LocalDate candidateDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<UserParticipationInfo> userParticipationInfoList;

    protected static AppointmentCandidateInfo of(LocalDate candidateDate, int start, int end, List<UserParticipationInfo> userParticipationInfoList) {
        return AppointmentCandidateInfo.builder()
                .candidateDate(candidateDate)
                .startTime(LocalTime.of(start, 0))
                .endTime(LocalTime.of(end, 0))
                .userParticipationInfoList(userParticipationInfoList)
                .build();
    }
}