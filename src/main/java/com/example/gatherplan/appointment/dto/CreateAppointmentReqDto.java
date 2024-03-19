package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAppointmentReqDto {

    private String appointmentName;

    private CandidateTimeType candidateTimeType;
    private List<CandidateTime> candidateTimeList;

    private Address address;

    private String notice;

    private List<LocalDate> candidateDateList;
}
