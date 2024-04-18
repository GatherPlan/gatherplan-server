package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempConfirmedAppointmentParticipantsReqDto {

    private String appointmentCode;

    private ConfirmedDateTime confirmedDateTime;

    private TempUserInfo tempUserInfo;
}
