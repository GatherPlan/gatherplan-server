package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;

public interface AppointmentService {
    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
