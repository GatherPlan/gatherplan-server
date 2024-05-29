package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.SelectedDateTime;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAppointmentJoinReqDto {

    private String appointmentCode;

    private List<SelectedDateTime> selectedDateTimeList;

    private String nickname;
}
