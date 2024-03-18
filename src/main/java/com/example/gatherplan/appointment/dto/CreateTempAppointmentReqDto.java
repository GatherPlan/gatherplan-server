package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateTempAppointmentReqDto {
    private String appointmentName;

    private CandidateTimeType candidateTimeType;

    private List<CandidateTime> candidateTimeList;

    private Address address;

    private String notice;

    private List<LocalDate> candidateDateList;

    private String name;

    private String password;
}
