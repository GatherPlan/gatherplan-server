package com.example.gatherplan.appointment.utils.unit;

import com.example.gatherplan.appointment.utils.AppointmentUtils;
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
                .endTime(AppointmentUtils.hourToLocalEndTime(end)) // 24시 일 경우 23:59로 처리
                .userParticipationInfoList(userParticipationInfoList)
                .build();
    }

    public int getEndTimeHour() {
        return this.endTime.getHour() == 23 && this.endTime.getMinute() == 59 ? 24 : this.endTime.getHour();
    }
}
