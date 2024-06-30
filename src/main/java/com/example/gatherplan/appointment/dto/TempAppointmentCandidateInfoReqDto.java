package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentCandidateInfoReqDto {
    private String appointmentCode;
    private TempUserInfo tempUserInfo;
    private int page;
    private int size;
}
