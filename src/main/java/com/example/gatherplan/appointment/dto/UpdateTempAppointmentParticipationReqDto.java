package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTempAppointmentParticipationReqDto {

    private String appointmentCode;

    private TempUserInfo tempUserInfo;

    private List<@Valid SelectedDateTime> selectedDateTimeList;
}
