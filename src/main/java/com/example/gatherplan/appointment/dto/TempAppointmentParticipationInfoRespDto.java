package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.common.unit.ParticipationInfo;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentParticipationInfoRespDto {
    private List<ParticipationInfo> participationInfoList;
    private List<TimeType> candidateTimeTypeList;
    private List<LocalDate> candidateDateList;
}
