package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempConfirmedAppointmentReqDto {

    private String appointmentCode;

    private ConfirmedDateTime confirmedDateTime;

    private TempUserInfo tempUserInfo;

    private List<String> nicknameList;
}
