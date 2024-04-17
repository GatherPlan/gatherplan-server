package com.example.gatherplan.appointment.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmedAppointmentParticipantsRespDto {
    private List<String> nicknameList;
}
