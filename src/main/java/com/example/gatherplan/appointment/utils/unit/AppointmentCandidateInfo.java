package com.example.gatherplan.appointment.utils.unit;

import com.example.gatherplan.common.unit.UserParticipationInfo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentCandidateInfo {
    private LocalDate candidateDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<UserParticipationInfo> userParticipationInfoList;

    // util 에서 사용 시 of 로만 생성할 수 있도록
    public static AppointmentCandidateInfo of(LocalDate candidateDate, int start, int end, List<UserParticipationInfo> userParticipationInfoList) {
        return AppointmentCandidateInfo.builder()
                .candidateDate(candidateDate)
                .startTime(LocalTime.of(start, 0))
                .endTime((end == 24) ? LocalTime.of(23, 59) : LocalTime.of(end, 0)) // 24시 일 경우 23:59로 처리
                .userParticipationInfoList(userParticipationInfoList)
                .build();
    }
}
