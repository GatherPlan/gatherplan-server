package com.example.gatherplan.appointment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AppointmentWithHostByKeywordDto {
    private String hostName;
    private Long appointmentId;
}
