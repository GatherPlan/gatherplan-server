package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.UserType;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAppointmentRespDto {
    private String appointmentName;
    private Address address;
    private String notice;

    private String hostName;
    private UserType userType;
    private List<CandidateTime> candidateTimeList;

    private List<LocalDate> candidateDateList;
}
