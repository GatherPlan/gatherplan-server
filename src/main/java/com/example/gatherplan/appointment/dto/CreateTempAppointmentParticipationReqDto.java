package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTempAppointmentParticipationReqDto {
    private String appointmentCode;
    private List<SelectedDateTime> selectedDateTimeList;
    private TempUserInfo tempUserInfo;
}
