package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CheckAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    void checkParticipation(CheckAppointmentReqDto checkAppointmentReqDto, String email);
}
