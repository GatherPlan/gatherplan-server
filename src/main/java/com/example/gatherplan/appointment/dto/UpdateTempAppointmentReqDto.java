package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.common.unit.Address;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTempAppointmentReqDto {
    private String appointmentCode;
    private String appointmentName;
    private List<TimeType> candidateTimeTypeList;
    private Address address;
    private List<LocalDate> candidateDateList;

    private UpdateTempAppointmentReqDto.TempUserInfo tempUserInfo;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TempUserInfo {
        private String nickname;

        private String password;
    }
}