package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentCandidateInfoReqDto {
    private String appointmentCode;
    private int page;
    private int size;
}
