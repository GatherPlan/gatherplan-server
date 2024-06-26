package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmAppointmentReqDto {

    private String appointmentCode;

    private ConfirmedDateTime confirmedDateTime;
}
