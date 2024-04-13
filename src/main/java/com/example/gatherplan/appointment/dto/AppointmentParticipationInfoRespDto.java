package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.common.unit.SelectedDateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentParticipationInfoRespDto {

    private List<ParticipationInfo> tempUserParticipationInfoList;
    private List<ParticipationInfo> userParticipationInfoList;
    private List<TimeType> candidateTimeTypeList;
    private List<LocalDate> candidateDateList;

}
