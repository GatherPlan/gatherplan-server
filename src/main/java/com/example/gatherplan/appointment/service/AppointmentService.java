package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;

public interface AppointmentService {
    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto);
}
