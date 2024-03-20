package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTempAppointmentReqDto {
    private String appointmentName;

    private CandidateTimeType candidateTimeType;

    private List<CandidateTime> candidateTimeList;

    private AddressDto address;

    private String notice;

    private List<LocalDate> candidateDateList;

    private String name;

    private String password;
}
