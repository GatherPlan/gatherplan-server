package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentParticipationInfoRespDto {

    private List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList;
    private List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList;
    private List<TimeType> candidateTimeTypeList;
    private List<LocalDate> candidateDateList;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserParticipationInfo {
        private String nickname;
        private List<SelectedDateTime> selectedDateTime;
    }


}
