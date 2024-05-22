package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempAppointmentCandidateDateInfoReq {
    private String appointmentCode;
    private TempUserInfo tempUserInfo;
}
