package com.example.gatherplan.appointment.dto;

import com.example.gatherplan.appointment.enums.UserType;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateTempAppointmentRespDto {
    private String appointmentName;
    private Address address;
    private String notice;

    private String hostName;
    private UserType userType;
    private List<CandidateTime> candidateTimeList;

    private List<LocalDate> candidateDateList;
}
