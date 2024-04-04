package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentParticipationInfoDto {

    private List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfo;
    private List<TimeType> candidateTimeTypeList;
    private List<LocalDate> candidateDateList;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserParticipationInfo {
        private String nickname;
        private List<SelectedDateTime> selectedDateTime;
    }
}
