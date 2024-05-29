package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TempCheckHostReqDto {

    private TempUserInfo tempUserInfo;

    private String appointmentCode;
}
