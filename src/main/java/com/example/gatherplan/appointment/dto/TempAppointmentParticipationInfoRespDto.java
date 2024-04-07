package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentParticipationInfoRespDto {
    private List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList;
    private List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList;
    private List<TimeType> candidateTimeTypeList;
    private List<LocalDate> candidateDateList;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "비회원 참여 정보")
    public static class UserParticipationInfo {
        private String nickname;
        private List<SelectedDateTime> selectedDateTime;
    }
}
